package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.thread.lock.LockUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripAccessValidate;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.dto.ItineraryItem;
import com.bread.traveler.entity.*;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.service.TripDaysService;
import com.bread.traveler.mapper.TripDaysMapper;
import com.bread.traveler.service.TripsService;
import com.bread.traveler.utils.TripLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【trip_days】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class TripDaysServiceImpl extends ServiceImpl<TripDaysMapper, TripDays> implements TripDaysService {

    @Autowired
    private TripDayItemsService tripDayItemsService;
    @Autowired
    @Qualifier("tripPlanClient")
    private ObjectProvider<ChatClient> tripPlanClientProvider;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    @Lazy
    private TripsService tripsService;
    @Autowired
    private TripLockUtils tripLockUtils;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    @Override
    @TripAccessValidate
    public TripDays createTripDay(UUID userId, UUID tripId, LocalDate date, String note) {
        log.info("Create trip day by user {}: {}, date: {}, note: {}",userId, tripId, date, note);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(date, "date cannot be null");
        // 需要先获取trip读锁
        RLock rLock = tripLockUtils.lockTripRead(tripId);
        try {
            // 判断旅程是否存在且date是否在旅程的日期范围内
            Trips trip = tripsService.getById(tripId);
            Assert.notNull(trip, Constant.TRIP_NOT_EXIST);
            Assert.isTrue(
                    !date.isBefore(trip.getStartDate()) && !date.isAfter(trip.getEndDate()),
                    Constant.TRIP_DAY_DATE_NOT_IN_TRIP_RANGE);
            // 查看数据库中是否有该日期的旅程
            boolean exists = lambdaQuery().eq(TripDays::getTripId, tripId).eq(TripDays::getDayDate, date).exists();
            if (exists) {
                log.warn("Trip day already exists: {}, date: {}", tripId, date);
                throw new BusinessException(Constant.TRIP_DAY_EXISTS);
            }
            TripDays tripDay = new TripDays();
            tripDay.setTripDayId(UUID.randomUUID());
            tripDay.setTripId(tripId);
            tripDay.setDayDate(date);
            tripDay.setNotes(note);
            if (save(tripDay)) {
                log.info("Create trip day success: id {}", tripDay.getTripDayId());
                return tripDay;
            }
            log.error("Create trip day failed: {}", tripDay);
            throw new RuntimeException(Constant.TRIP_DAY_CREATE_FAILED);
        }finally {
            tripLockUtils.unlock(rLock);
        }
    }

    @Override
    @TripAccessValidate
    public boolean updateTripDayNote(UUID userId, UUID tripId, UUID tripDayId, String note) {
        log.info("Update trip day note: {}, note: {}", tripDayId, note);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        // 需要先获取tripDay锁
        RLock rLock = tripLockUtils.lockDay(tripDayId);
        try {
            boolean update = lambdaUpdate().eq(TripDays::getTripDayId, tripDayId)
                    .set(TripDays::getNotes, note).update();
            if (update) {
                log.info("Update trip day note success: {}", tripDayId);
                return true;
            }
            log.error("Update trip day note failed: {}", tripDayId);
            return false;
        } finally {
            tripLockUtils.unlock(rLock);
        }
    }

    @Override
    @TripVisibilityValidate
    public EntireTripDay getEntireTripDay(UUID userId, UUID tripId, UUID tripDayId) {
        log.info("Get entire trip day: {}", tripDayId);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        TripDays tripDay = getById(tripDayId);
        if (tripDay == null) {
            log.error("Trip day not exist: {}", tripDayId);
            throw new BusinessException(Constant.TRIP_DAY_NOT_EXIST);
        }
        List<EntireTripDayItem> entireItems = tripDayItemsService.getEntireItemsByTripDayId(userId, tripId, tripDayId);
        return new EntireTripDay(tripDay, entireItems);
    }

    @Override
    @TripVisibilityValidate
    public List<EntireTripDay> getEntireTripDaysByTripId(UUID userId, UUID tripId) {
        log.info("Get entire trip days by trip id: {}", tripId);
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取该旅程的所有日程
        List<TripDays> tripDays = lambdaQuery().eq(TripDays::getTripId, tripId).list();
        if (tripDays == null || tripDays.isEmpty()) {
            log.info("No trip days in trip: {}", tripId);
            return Collections.emptyList();
        }
        // 按照每个日程的日期进行升序排序，并获取它们的items（EntireTripDay）
        List<EntireTripDay> result = tripDays.stream()
                .sorted(Comparator.comparing(TripDays::getDayDate))
                .map(tripDay -> {
                    List<EntireTripDayItem> entireItems = tripDayItemsService.getEntireItemsByTripDayId(userId, tripId, tripDay.getTripDayId());
                    return new EntireTripDay(tripDay, entireItems);
                }).toList();
        log.info("Get entire trip days success: {}", tripDays);
        return result;
    }

    @Override
    @TripAccessValidate
    public EntireTripDay aiRePlanTripDay(UUID userId, UUID tripId, UUID tripDayId) {
        log.info("AI re-plan trip day: {}", tripDayId);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        // 需要先获取tripDay锁
        RLock rLock = tripLockUtils.lockDay(tripDayId);
        try {
            // 获取该日程
            TripDays tripDay = getById(tripDayId);
            Assert.notNull(tripDay, Constant.TRIP_DAY_NOT_EXIST);
            // 获取该日程的items
            List<EntireTripDayItem> entireItems = tripDayItemsService.getEntireItemsByTripDayId(userId, tripId, tripDayId);
            Assert.notEmpty(entireItems, Constant.TRIP_DAY_NO_ITEMS + "，AI无法规划");
            // 获取所有itineraryItems (poi或nonPoiItem)。对于nonPoi只保留有estimatedAddress的
            List<String> itineraryItemsJson = entireItems.stream()
                    .filter(entireItem -> {
                        ItineraryItem entity = entireItem.getEntity();
                        if (entity instanceof NonPoiItem nonPoi) {
                            // nonPoi只保留有estimatedAddress的
                            return StrUtil.isNotBlank(nonPoi.getEstimatedAddress());
                        }
                        // poi全部保留（都有address）
                        return true;
                    })
                    // 转为JSON字符串，过滤不必要的字段和空字段
                    .map(entireItem -> {
                        ItineraryItem entity = entireItem.getEntity();
                        Map<String, Object> map = new HashMap<>();
                        if (entity instanceof Pois poi) {
                            // entity是poi
                            map = BeanUtil.beanToMap(poi, map, CopyOptions.create()
                                    .setIgnoreProperties("poiId", "externalApiId", "photos", "phone", "rating", "createdAt")
                                    .ignoreNullValue());
                        } else {
                            // entity是nonPoi
                            assert entity instanceof NonPoiItem;
                            NonPoiItem nonPoi = (NonPoiItem) entity;
                            map = BeanUtil.beanToMap(nonPoi, map, CopyOptions.create()
                                    .setIgnoreProperties("id", "sourceUrl", "createdAt", "privateUserId")
                                    .ignoreNullValue());
                        }
                        map.put("itemId", entireItem.getItem().getItemId());
                        return JSONUtil.toJsonStr(map);
                    }).toList();
            Assert.notEmpty(itineraryItemsJson, Constant.TRIP_DAY_ITEMS_NO_ADDRESS + "，AI无法规划");
            long start = System.currentTimeMillis();
            Future<AiPlanTripDay> rePlanTask = THREAD_POOL.submit(() -> {
                // 创建prompt。PromptTemplate中有JSON，使用自定义render
                PromptTemplate promptTemplate = PromptTemplate.builder()
                        .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                        .resource(new ClassPathResource("prompts/RePlanTripDayUserPromptTemplate.md")).build();
                Prompt prompt = promptTemplate.create(Map.of("items", itineraryItemsJson, "date", tripDay.getDayDate()));
                ChatClient client = tripPlanClientProvider.getObject();
                return client.prompt(prompt).call().entity(new ParameterizedTypeReference<>() {
                });
            });
            // 将原先的items变为map，key为tripDayItem的id，value为tripDayItem
            Map<UUID, TripDayItems> tripDayItemsMap = entireItems.stream().collect(Collectors.toMap(
                    entireItem -> entireItem.getItem().getItemId(),
                    EntireTripDayItem::getItem));
            // 创建itemIdToEntity的map集合
            Map<UUID, ItineraryItem> itemIdToEntity = entireItems.stream().collect(Collectors.toMap(
                    entireItem -> entireItem.getItem().getItemId(),
                    EntireTripDayItem::getEntity));
            // 处理结果
            AiPlanTripDay output = null;
            try {
                output = rePlanTask.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Re plan trip day CLIENT TASK failed.", e);
                throw new RuntimeException(Constant.TRIP_DAY_RE_PLAN_FAILED);
            }
            if (output == null) {
                log.error("Re plan trip day failed, output null: {}", tripDay);
                throw new RuntimeException(Constant.TRIP_DAY_RE_PLAN_FAILED);
            }
            long end = System.currentTimeMillis();
            log.info("Re plan trip day success. Use {}s. Start processing: {}", (end - start) * 1.0 / 1000, tripDay);
            String summaryNotes = output.getSummaryNotes();
            String newTripDayNotes = tripDay.getNotes() + System.lineSeparator() + "AI:" + summaryNotes;
            tripDay.setNotes(newTripDayNotes);
            // 更新日程的顺序和相关信息，根据itemId获取到item更新即可
            List<AiPlanTripDayItem> orderedItems = output.getOrderedItems();
            AtomicReference<Double> order = new AtomicReference<>(10000.0);
            List<TripDayItems> modifiedItems = orderedItems.stream()
                    .map(orderedItem -> {
                        // 从map中获取对应的tripDayItem，并删除
                        TripDayItems tripDayItems = tripDayItemsMap.remove(orderedItem.getItemId());
                        if (tripDayItems == null) {
                            // map中找不到，大模型错误，忽略该item
                            log.error("Can't found trip day item in map: {}", orderedItem.getItemId());
                            return null;
                        }
                        // 更新item的相关信息，notes和estimatedCost追加
                        String aiNotes = orderedItem.getNotes() == null ? "" : orderedItem.getNotes();
                        String newItemNotes = tripDayItems.getNotes() + System.lineSeparator() + "AI:" + aiNotes;
                        BigDecimal aiEstimatedCost = orderedItem.getEstimatedCost() == null ? BigDecimal.ZERO : orderedItem.getEstimatedCost();
                        BigDecimal oldEstimatedCost = tripDayItems.getEstimatedCost() == null ? BigDecimal.ZERO : tripDayItems.getEstimatedCost();
                        BigDecimal newEstimatedCost = oldEstimatedCost.add(aiEstimatedCost);
                        tripDayItems.setNotes(newItemNotes);
                        tripDayItems.setEstimatedCost(newEstimatedCost);
                        tripDayItems.setStartTime(orderedItem.getStartTime());
                        tripDayItems.setEndTime(orderedItem.getEndTime());
                        // 更新item的顺序
                        tripDayItems.setItemOrder(order.getAndUpdate(value -> value + 10000.0));
                        return tripDayItems;
                    }).filter(Objects::nonNull).toList();
            modifiedItems = new ArrayList<>(modifiedItems);
            // 此时map中剩下的都是没有被修改的（没有地址或大模型出错的），放到最后
            for (TripDayItems tripDayItem : tripDayItemsMap.values()) {
                tripDayItem.setItemOrder(order.getAndUpdate(value -> value + 10000.0));
                modifiedItems.add(tripDayItem);
            }
            // 判断当前线程是否仍持有锁
            if (!rLock.isHeldByCurrentThread()){
                log.error("Re plan trip day: lock not held by current thread");
                throw new RuntimeException(Constant.TRIP_AI_EXECUTE_TIMEOUT);
            }
            // 保存至数据库，手动开启事务
            List<TripDayItems> finalModifiedItems = modifiedItems;
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    boolean a = updateById(tripDay);
                    boolean b = tripDayItemsService.updateBatchById(finalModifiedItems);
                    if (!a || !b) {
                        log.error("Re plan trip day: save to db failed {}, {}", tripDay, finalModifiedItems);
                        status.setRollbackOnly();
                    }else{
                        log.info("Re plan trip day: save to db success {}, {}", tripDay, finalModifiedItems);
                    }
                } catch (Exception e) {
                    log.error("Re plan trip day: save to db failed {}, {}", tripDay, finalModifiedItems, e);
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            });
            // 返回结果
            List<EntireTripDayItem> list = modifiedItems.stream().sorted(Comparator.comparing(TripDayItems::getItemOrder))
                    .map(item -> {
                        ItineraryItem itineraryItem = itemIdToEntity.get(item.getItemId());
                        return new EntireTripDayItem(item, itineraryItem);
                    }).toList();
            return new EntireTripDay(tripDay, list);
        } finally {
            tripLockUtils.unlock(rLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TripAccessValidate
    public boolean exchangeDayOrder(UUID userId, UUID tripId, UUID aTripDayId, UUID bTripDayId) {
        log.info("Exchange day order: {}, {}", aTripDayId, bTripDayId);
        Assert.notNull(aTripDayId, "aTripDayId cannot be null");
        Assert.notNull(bTripDayId, "bTripDayId cannot be null");
        // 需要同时获取两个tripDayLock
        RLock rLock = tripLockUtils.lockDays(tripId, List.of(aTripDayId, bTripDayId));
        try {
            List<TripDays> tripDays = lambdaQuery().in(TripDays::getTripDayId, aTripDayId, bTripDayId).list();
            if (tripDays == null || tripDays.size() != 2) {
                log.error("Trip days not exist: {}, {}", aTripDayId, bTripDayId);
                throw new BusinessException(Constant.TRIP_DAY_NOT_EXIST);
            }
            // 交换两个日程的date
            LocalDate aDate = baseMapper.getDateByTripDayId(aTripDayId);
            LocalDate bDate = baseMapper.getDateByTripDayId(bTripDayId);
            baseMapper.updateDate(aTripDayId, LocalDate.of(9999, 12, 31));
            baseMapper.updateDate(bTripDayId, aDate);
            baseMapper.updateDate(aTripDayId, bDate);
            log.info("Exchange day order success: {}, {}", aTripDayId, bTripDayId);
            return true;
        } finally {
            tripLockUtils.unlock(rLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TripAccessValidate
    public boolean deleteTripDays(UUID userId, UUID tripId, List<UUID> tripDayIds) {
        log.info("Delete trip days: {}", tripDayIds);
        Assert.notNull(tripDayIds, "tripDayIds cannot be null");
        Assert.notEmpty(tripDayIds, "tripDayIds cannot be empty");
        // 获取多个tripDay锁
        RLock rLock = tripLockUtils.lockDays(tripId, tripDayIds);
        try {
            // 级联删除这些日程tripDayIds下的所有item。
            // 注意如果表中没有匹配的数据，也会返回false。不进行返回值验证
            tripDayItemsService.lambdaUpdate().in(TripDayItems::getTripDayId, tripDayIds).remove();
            // 删除这些日程
            lambdaUpdate().in(TripDays::getTripDayId, tripDayIds).remove();
            log.info("Delete trip days success: {}", tripDayIds);
            return true;
        }finally {
            tripLockUtils.unlock(rLock);
        }
    }
}




