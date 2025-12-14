package com.bread.traveler.utils;

import cn.hutool.core.lang.Assert;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.entity.TripDays;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.service.TripDaysService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务：基于 Redisson 读写锁实现的原子性分层互斥锁
 * * 机制说明：
 * 1. 采用 ReadWriteLock (读写锁) 维护层级稳定性：
 * - 当修改子组件(Item/Day)时，持有父组件的 ReadLock (共享锁)，阻止父组件被 WriteLock (独占修改)。
 * - 当修改父组件(Trip)时，持有 WriteLock，阻止任何子组件获取 ReadLock。
 * 2. 采用 MultiLock (联锁) 保证原子性：一次性获取整条链路的锁。
 * 3. 启用 Watchdog (看门狗)：自动续期，防止业务执行过长导致锁提前释放。
 */
@Component
@Slf4j
public class TripLockUtils {

    @Autowired
    private RedissonClient redissonClient;
    // 使用 @Lazy 注入 Service，防止循环依赖 (Service -> LockService -> Service)
    @Autowired
    @Lazy
    private TripDaysService tripDaysService;
    @Autowired
    @Lazy
    private TripDayItemsService tripDayItemsService;

    // 锁前缀
    private static final String LOCK_PREFIX_TRIP_RW = "lock:rw:trip:"; // Trip 是读写锁
    private static final String LOCK_PREFIX_DAY_RW = "lock:rw:day:";   // Day 是读写锁
    private static final String LOCK_PREFIX_ITEM = "lock:item:";       // Item 是普通互斥锁

    // 锁等待时间 (尝试获取锁时最多等待的时间)
    private static final long WAIT_TIME_SECONDS = 2;

    /**
     * 1. 锁定 Item (最小粒度)
     * 逻辑：Trip(Read) + Day(Read) + Item(Lock)
     * 含义：我要修改 Item，请保证 Trip 和 Day 结构稳定（只读），且 Item 独占。
     */
    public RLock lockItem(UUID itemId) {
        // 1. 准备锁对象
        RLock itemLock = redissonClient.getLock(LOCK_PREFIX_ITEM + itemId);

        // 2. 解析父级 ID
        UUID tripDayId = getTripDayIdByItemId(itemId);
        UUID tripId = getTripIdByDayId(tripDayId);

        // 3. 获取父级读写锁
        RReadWriteLock dayRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_DAY_RW + tripDayId);
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);

        // 4. 组装联锁 (Trip.Read + Day.Read + Item.Lock)
        // 注意顺序：虽然 MultiLock 会处理防死锁，但逻辑上建议从大到小
        RedissonMultiLock multiLock = new RedissonMultiLock(
                tripRwLock.readLock(),
                dayRwLock.readLock(),
                itemLock
        );

        // 5. 尝试加锁 (启用看门狗)
        tryLockMulti(multiLock, "当前日程项正在被修改");

        return multiLock;
    }

    /**
     * 批量锁定多个item锁
     * Trip(Read) + Day(Read) + Item(Lock)
     * @param itemIds
     */
    public RLock lockItems(UUID tripId, List<UUID> itemIds) {
        // 根据itemId获取dayId
        List<TripDayItems> items = tripDayItemsService.lambdaQuery().in(TripDayItems::getItemId, itemIds).list();
        List<UUID> tripDayIds = items.stream().map(TripDayItems::getTripDayId).sorted().toList();
        itemIds = items.stream().map(TripDayItems::getItemId).sorted().toList();
        // 获取Trip的读锁
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock tripReadLock = tripRwLock.readLock();
        // 构建联锁，先 Trip 读锁，再 Day 读锁，最后 Item 锁
        List<RLock> lockList = new ArrayList<>();
        lockList.add(tripReadLock);
        for (UUID tripDayId : tripDayIds) {
            RReadWriteLock rwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_DAY_RW + tripDayId);
            lockList.add(rwLock.writeLock());
        }
        for (UUID itemId : itemIds) {
            RLock itemLock = redissonClient.getLock(LOCK_PREFIX_ITEM + itemId);
            lockList.add(itemLock);
        }
        RedissonMultiLock multiLock = new RedissonMultiLock(lockList.toArray(new RLock[0]));
        // 尝试加锁
        tryLockMulti(multiLock, "日程项正在被修改");
        return multiLock;
    }


    /**
     * 2. 锁定 TripDay (中间粒度)
     * 逻辑：Trip(Read) + Day(Write)
     * 含义：我要修改 Day (如改备注、排序)，请保证 Trip 稳定，且 Day 独占（其他人不能改 Day 下的 Item）。
     */
    public RLock lockDay(UUID tripDayId) {
        // 1. 准备 Day 的写锁
        RReadWriteLock dayRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_DAY_RW + tripDayId);
        RLock dayWriteLock = dayRwLock.writeLock();

        // 2. 解析父级 ID
        UUID tripId = getTripIdByDayId(tripDayId);

        // 3. 获取 Trip 的读锁
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock tripReadLock = tripRwLock.readLock();

        // 4. 组装联锁 (Trip.Read + Day.Write)
        RedissonMultiLock multiLock = new RedissonMultiLock(tripReadLock, dayWriteLock);

        // 5. 尝试加锁
        tryLockMulti(multiLock, "当前日程正在被修改");

        return multiLock;
    }

    /**
     * 批量锁定多个tripDay锁
     * 针对交换日程项顺序、批量删除日程项时使用
     * @param tripId
     * @param tripDayIds
     * @return
     */
    public RLock lockDays(UUID tripId, List<UUID> tripDayIds) {
        // 获取Trip的读锁
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock tripReadLock = tripRwLock.readLock();
        // 组装联锁 (Trip.Read + Day.Write + Day.Write + ...)
        List<RLock> lockList = new ArrayList<>();
        lockList.add(tripReadLock);
        for (UUID tripDayId : tripDayIds) {
            RReadWriteLock rwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_DAY_RW + tripDayId);
            lockList.add(rwLock.writeLock());
        }
        RedissonMultiLock multiLock = new RedissonMultiLock(lockList.toArray(new RLock[0]));
        // 尝试加锁
        tryLockMulti(multiLock, "日程正在被修改");
        return multiLock;
    }

    /**
     * 获取 Day 的读锁
     * Trip(Read) + Day(Read)
     * @param tripDayId
     * @return
     */
    public RLock lockDayRead(UUID tripId, UUID tripDayId) {
        // 获取Trip的读锁
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock tripRLock = tripRwLock.readLock();
        // 获取Day的读锁
        RReadWriteLock dayRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_DAY_RW + tripDayId);
        RLock dayRLock = dayRwLock.readLock();
        // 组装联锁 (Trip.Read + Day.Read)
        RedissonMultiLock multiLock = new RedissonMultiLock(tripRLock, dayRLock);
        tryLockMulti(multiLock, "当前日程正在被修改");
        return multiLock;
    }

    /**
     * 3. 锁定 Trip (最大粒度)
     * 逻辑：Trip(Write)
     * 含义：我要修改 Trip，独占整个旅程（其他人不能读/写 Trip，也不能改下面的 Day/Item）。
     */
    public RLock lockTrip(UUID tripId) {
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock tripWriteLock = tripRwLock.writeLock();

        // 直接获取写锁，无需 MultiLock
        tryLockSingle(tripWriteLock, "当前旅程整体正在被修改");

        return tripWriteLock;
    }

    /**
     * 获取 Trip 的读锁
     * Trip(Read)
     * @param tripId
     * @return
     */
    public RLock lockTripRead(UUID tripId) {
        RReadWriteLock tripRwLock = redissonClient.getReadWriteLock(LOCK_PREFIX_TRIP_RW + tripId);
        RLock readLock = tripRwLock.readLock();
        tryLockSingle(readLock, "当前旅程正在被修改");
        return readLock;
    }

    /**
     * 通用解锁方法
     */
    public void unlock(RLock lock) {
        if (lock != null) {
            try {
                // 检查当前线程是否持有锁（如果是 MultiLock，它内部会检查）
                // 注意：MultiLock 不支持 isHeldByCurrentThread() 的直接调用，但 unlock() 是安全的
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                // 锁可能因为超时或被看门狗释放了，或者是当前线程没有持有锁
                log.warn("Redisson unlock skipped: lock is not held by current thread.");
            } catch (Exception e) {
                log.error("Redisson unlock failed", e);
            }
        }
    }

    /**
     * 尝试加锁 (MultiLock)
     * @param multiLock
     * @param errorMsg
     */
    private void tryLockMulti(RedissonMultiLock multiLock, String errorMsg) {
        try {
            // tryLock(waitTime, leaseTime, unit)
            // leaseTime = -1 表示启用看门狗 (Watchdog)
            boolean success = multiLock.tryLock(WAIT_TIME_SECONDS, -1, TimeUnit.SECONDS);
            if (!success) {
                throw new BusinessException(errorMsg + "，请稍后再试。");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("系统繁忙(Lock Interrupted)，请稍后再试");
        }
    }

    private void tryLockSingle(RLock lock, String errorMsg) {
        try {
            // 不传 leaseTime 也是启用看门狗的标准方式
            boolean success = lock.tryLock(WAIT_TIME_SECONDS, TimeUnit.SECONDS);
            if (!success) {
                throw new BusinessException(errorMsg + "，请稍后再试。");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("系统繁忙(Lock Interrupted)，请稍后再试");
        }
    }

    /**
     * 获取 Item 的父级 tripId
     * @param tripDayId
     * @return
     */
    private UUID getTripIdByDayId(UUID tripDayId) {
        TripDays tripDay = tripDaysService.getById(tripDayId);
        Assert.notNull(tripDay, Constant.TRIP_DAY_NOT_EXIST);
        return tripDay.getTripId();
    }

    /**
     * 获取 Item 的父级 tripDayId
     * @param itemId
     * @return
     */
    private UUID getTripDayIdByItemId(UUID itemId) {
        TripDayItems item = tripDayItemsService.getById(itemId);
        Assert.notNull(item, Constant.TRIP_DAY_ITEMS_NOT_EXIST);
        return item.getTripDayId();
    }

}
