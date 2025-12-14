package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripAccessValidate;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.*;
import com.bread.traveler.entity.*;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.enums.TripStatus;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.*;
import com.bread.traveler.mapper.TripsMapper;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【trips】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class TripsServiceImpl extends ServiceImpl<TripsMapper, Trips> implements TripsService {

    @Autowired
    private TripDaysService tripDaysService;
    @Autowired
    private WishlistItemsService wishlistItemsService;
    @Autowired
    @Qualifier("tripPlanClient")
    private ObjectProvider<ChatClient> tripPlanClientProvider;
    @Autowired
    private TripDayItemsService tripDayItemsService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TripMembersService tripMembersService;
    @Autowired
    private TripLockUtils tripLockUtils;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Trips createTrip(UUID userId, TripDto dto) {
        log.info("Create trip: user {}, dto {}", userId, dto);
        Assert.notNull(userId, "userId cannot be null");
        Trips trip = BeanUtil.copyProperties(dto, Trips.class, "tripId");
        trip.setTripId(UUID.randomUUID());
        trip.setUserId(userId);
        trip.setStatus(TripStatus.PLANNING);
        trip.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        trip.setIsPrivate(true);
        // 添加到trip_member表
        if (save(trip) && tripMembersService.createOwner(trip.getTripId(), userId)) {
            log.info("Create trip success: {}", trip.getTripId());
            return trip;
        }
        log.error("Create trip failed: {}", trip);
        throw new RuntimeException(Constant.TRIP_CREATE_FAILED);
    }

    @Override
    @TripAccessValidate
    public Trips updateTripInfo(UUID userId, UUID tripId, TripDto dto) {
        log.info("Update trip info: user {}, dto {}", userId, dto);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(dto, "dto cannot be null");
        Trips trip = lambdaQuery().eq(Trips::getTripId, tripId).one();
        if (trip == null) {
            log.error("Trip not found: {}", tripId);
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }
        // 更新信息
        BeanUtil.copyProperties(dto, trip, "tripId");
        if (updateById(trip)) {
            log.info("Update trip info success: {}", trip.getTripId());
            return trip;
        }
        log.error("Update trip info failed: {}", trip.getTripId());
        throw new RuntimeException(Constant.TRIP_UPDATE_FAILED);
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER) // 仅允许OWNER修改
    public boolean changeVisibility(UUID userId, UUID tripId, Boolean isPrivate) {
        log.info("Change visibility of trip {}: is private {}", tripId, isPrivate);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(isPrivate, "isPrivate cannot be null");
        // 更新visibility
        boolean update = lambdaUpdate().eq(Trips::getTripId, tripId).eq(Trips::getUserId, userId).set(Trips::getIsPrivate, isPrivate).update();
        if (!update) {
            log.error("Change visibility failed: {}", tripId);
            throw new RuntimeException(Constant.TRIP_VISIBILITY_UPDATE_FAILED);
        }
        log.info("Change visibility success: {}", tripId);
        return true;
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER)
    public boolean changeStatus(UUID userId, UUID tripId, TripStatus newStatus) {
        log.info("Change status of trip {}: {}", tripId, newStatus);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(newStatus, "newStatus cannot be null");
        // 更新status，自定义预编译sql，采用::进行类型推断，避免了mp中setSql方法的注入问题
        int row = baseMapper.changeStatus(userId, tripId, newStatus.name());
        if (row != 1) {
            log.error("Change status failed: {}", tripId);
            throw new RuntimeException(Constant.TRIP_STATUS_UPDATE_FAILED);
        }
        log.info("Change status success: {}", tripId);
        return true;
    }

    @Override
    public List<TripWithMemberInfoDto> getAllTripsOfUser(UUID userId) {
        log.info("Get all trips of user: {}", userId);
        Assert.notNull(userId, "userId cannot be null");
        // 获取用户加入的旅程,包括申请但未通过的isPass=false
        // 已经按照加入的顺序倒叙排序
        List<TripMembers> tripMemberInfos = tripMembersService.getJoinedTrips(userId);
        if (tripMemberInfos.isEmpty()) {
            return Collections.emptyList();
        }
        Map<UUID, TripMembers> tripIdToTripMember = tripMemberInfos.stream().collect(Collectors.toMap(TripMembers::getTripId, tripMember -> tripMember));
        // 获取所有旅程信息
        List<UUID> tripIds = tripMemberInfos.stream().map(TripMembers::getTripId).toList();
        List<Trips> trips = listByIds(tripIds);
        // 创建TripWithMemberInfoDto列表
        // 按照用户加入旅程的时间倒序排序
        return trips.stream().map(trip -> {
                    TripMembers memberInfo = tripIdToTripMember.get(trip.getTripId());
                    TripWithMemberInfoDto dto = BeanUtil.copyProperties(trip, TripWithMemberInfoDto.class, "userId");
                    dto.setOwnerId(trip.getUserId());
                    dto.setMemberRole(memberInfo.getRole());
                    dto.setIsPass(memberInfo.getIsPass());
                    dto.setJoinedAt(memberInfo.getCreatedAt());
                    return dto;
                })
                // 按照用户加入旅程的时间倒序排序
                .sorted((o1, o2) -> o2.getJoinedAt().compareTo(o1.getJoinedAt()))
                .toList();
    }

    @Override
    @TripVisibilityValidate
    public EntireTrip getEntireTrip(UUID userId, UUID tripId) {
        log.info("Get entire trip: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Trips trip = lambdaQuery().eq(Trips::getTripId, tripId).one();
        if (trip == null) {
            log.error("Trip not found: {}", tripId);
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }
        List<EntireTripDay> tripDays = tripDaysService.getEntireTripDaysByTripId(userId, tripId);
        return new EntireTrip(trip, tripDays);
    }

    @Override
    public Page<Trips> getPublicTrips(String destinationCity, LocalDate startDate, LocalDate endDate, Integer pageNum, Integer pageSize) {
        log.info("Get public trips: destinationCity {}, startDate {}, endDate {}, pageNum {}, pageSize {}", destinationCity, startDate, endDate, pageNum, pageSize);
        Assert.notNull(pageNum, "pageNum cannot be null");
        Assert.notNull(pageSize, "pageSize cannot be null");
//        LambdaQueryChainWrapper<Trips> wrapper = lambdaQuery()
//                .eq(Trips::getIsPrivate, false)
//                // 如果直接使用eq会报类型转换错误
//                .eqSql(Trips::getStatus, "'%s'".formatted(TripStatus.PLANNING.name()))
//                .like(destinationCity != null && !destinationCity.trim().isEmpty(), Trips::getDestinationCity, destinationCity)
//                .ge(startDate != null, Trips::getStartDate, startDate)
//                .le(endDate != null, Trips::getEndDate, endDate);
//        return wrapper.page(Page.of(pageNum, pageSize));
        // 自定义动态sql语句，使用::进行TripStatus类型推断，避免了mp中eqSql方法的注入问题
        long offset = (long) (pageNum - 1) * pageSize;
        long total = baseMapper.countTrips(false, TripStatus.PLANNING.name(), destinationCity, startDate, endDate);
        if (total == 0){
            // 总数为0，返回空页
           log.info("Total is 0, return empty page");
           return Page.of(pageNum, pageSize, total);
        }
        // 获取分页数据列表，组建Page对象并返回
        List<Trips> records = baseMapper.selectTripsPage(
                false, TripStatus.PLANNING.name(), destinationCity, startDate, endDate, offset, pageSize);
        Page<Trips> page = Page.of(pageNum, pageSize, total);
        page.setRecords(records);
        page.setPages((long) Math.ceil(total * 1.0 / pageSize));
        return page;
    }

    @Override
    @TripAccessValidate //todo 引入分布式锁，避免并发修改
    public EntireTrip aiGenerateEntireTripPlan(UUID userId, UUID tripId) {
        log.info("Ai generate entire trip: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        // 1. 获取锁 (开启看门狗机制/Watchdog，确保AI执行期间锁自动续期)
        RLock rLock = tripLockUtils.lockTrip(tripId);
        try{
            // 判断旅程是否存在
            Trips trip = lambdaQuery().eq(Trips::getTripId, tripId).one();
            if (trip == null) {
                log.error("Trip not found: {}", tripId);
                throw new BusinessException(Constant.TRIP_NOT_EXIST);
            }
            // 根据wishList智能生成旅程，获取该旅程的所有wishlistItem
            List<EntireWishlistItem> entireWishlistItems = wishlistItemsService.listEntireByTripId(tripId);
            Assert.notEmpty(entireWishlistItems, Constant.WISHLIST_EMPTY + "，AI无法规划");
            // 获取entity，过滤不必要的字段。nonPoiItem只保留有estimatedAddress的
            List<String> itineraryItemsJson = entireWishlistItems.stream()
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
            long start = System.currentTimeMillis();
            Future<AiPlanTrip> aiPlanTripTask = THREAD_POOL.submit(() -> {
                // 创建prompt。PromptTemplate中有JSON，使用自定义render
                PromptTemplate promptTemplate = PromptTemplate.builder()
                        .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                        .resource(new ClassPathResource("prompts/AiGenerateTripUserPromptTemplate.md")).build();
                String date = trip.getStartDate() + "至" + trip.getEndDate();
                Prompt prompt = promptTemplate.create(Map.of("items", itineraryItemsJson, "date", date));
                ChatClient client = tripPlanClientProvider.getObject();
                return client.prompt(prompt).call().entity(new ParameterizedTypeReference<>() {
                });
            });
            // 将原先的entireWishlistItems转为map，key为itemId，value为entireWishlistItem
            Map<UUID, EntireWishlistItem> itemIdToEntireWishlistItem = entireWishlistItems.stream().collect(Collectors.toMap(
                    entireWishlistItem -> entireWishlistItem.getItem().getItemId(),
                    entireWishlistItem -> entireWishlistItem
            ));
            // 处理结果
            AiPlanTrip output = null;
            try {
                output = aiPlanTripTask.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("AI generate entire trip CLIENT TASK failed", e);
                throw new RuntimeException(Constant.TRIP_AI_GENERATE_FAILED);
            }
            if (output == null) {
                log.error("AI generate entire trip failed, output null: {}", trip);
                throw new RuntimeException(Constant.TRIP_AI_GENERATE_FAILED);
            }
            long end = System.currentTimeMillis();
            log.info("AI generate entire trip success. Use {}s. Start processing: {}", (end - start) * 1.0 / 1000, trip);
            List<TripDaysService.AiPlanTripDay> aiPlanTripDays = output.getTripDays();
            AtomicReference<LocalDate> date = new AtomicReference<>(trip.getStartDate());
            List<EntireTripDay> entireTripDays = aiPlanTripDays.stream().map(aiPlanTripDay -> {
                // 根据ai创建的当日规划，创建tripDay和EntireTripDayItem
                // 创建tripDay
                TripDays tripDay = new TripDays();
                tripDay.setTripDayId(UUID.randomUUID());
                tripDay.setTripId(tripId);
                tripDay.setDayDate(date.getAndUpdate(d -> d.plusDays(1)));
                tripDay.setNotes(aiPlanTripDay.getSummaryNotes());
                // 创建List<EntireTripDayItem>
                List<TripDaysService.AiPlanTripDayItem> aiPlanTripDayItems = aiPlanTripDay.getOrderedItems();
                AtomicReference<Double> order = new AtomicReference<>(10000.0);
                List<EntireTripDayItem> entireTripDayItems = aiPlanTripDayItems.stream().map(aiPlanTripDayItem -> {
                    // 返回的aiPlanTripDayItem中的itemId是wishlistItem的id，获取对应的entireWishlistItem
                    EntireWishlistItem entireWishlistItem = itemIdToEntireWishlistItem.get(aiPlanTripDayItem.getItemId());
                    if (entireWishlistItem == null) {
                        // 无法从map中获取entireWishlistItem，大模型错误，忽略这个item
                        log.error("AI generate entire trip: WishlistItem not found in map. LLM error: {}", aiPlanTripDayItem.getItemId());
                        return null;
                    }
                    // 创建tripDayItem
                    WishlistItems wishlistItem = entireWishlistItem.getItem();
                    TripDayItems tripDayItem = TripDayItems.builder()
                            .itemId(UUID.randomUUID())
                            .tripDayId(tripDay.getTripDayId())
                            .entityId(wishlistItem.getEntityId())
                            .isPoi(wishlistItem.getIsPoi())
                            .startTime(aiPlanTripDayItem.getStartTime())
                            .endTime(aiPlanTripDayItem.getEndTime())
                            .estimatedCost(aiPlanTripDayItem.getEstimatedCost())
                            .notes(aiPlanTripDayItem.getNotes())
                            .itemOrder(order.getAndUpdate(value -> value + 10000.0)).build();
                    // 创建EntireTripDayItem，关联entity
                    ItineraryItem itineraryItem = entireWishlistItem.getEntity();
                    return new EntireTripDayItem(tripDayItem, itineraryItem);
                }).filter(Objects::nonNull).toList();
                // 创建EntireTripDay并返回
                return new EntireTripDay(tripDay, entireTripDayItems);
            }).toList();
            // 查看锁是否被当前线程持有
            if (!rLock.isHeldByCurrentThread()){
                log.error("Trip lock not held by current thread: {}", trip);
                throw new RuntimeException(Constant.TRIP_AI_EXECUTE_TIMEOUT);
            }
            // 保存至数据库，创建事务
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    // 需要更新tripDay表和tripDayItem表
                    List<TripDays> allTripDays = new ArrayList<>();
                    List<TripDayItems> allTripDayItems = new ArrayList<>();
                    for (EntireTripDay entireTripDay : entireTripDays) {
                        allTripDays.add(entireTripDay.getTripDay());
                        List<TripDayItems> tripDayItems = entireTripDay.getTripDayItems().stream().map(EntireTripDayItem::getItem).toList();
                        allTripDayItems.addAll(tripDayItems);
                    }
                    // 删除该trip原先的规划
                    List<TripDays> oriTripDays = tripDaysService.lambdaQuery().eq(TripDays::getTripId, tripId).list();
                    if (oriTripDays != null && !oriTripDays.isEmpty()) {
                        List<UUID> oriTripDayIds = oriTripDays.stream().map(TripDays::getTripDayId).toList();
                        log.info("AI generate trip: Delete original trip plans: {}", oriTripDayIds);
                        // 这个方法会删除旅程中的日程，级联删除该日程下的所有item
                        tripDaysService.deleteTripDays(userId, tripId, oriTripDayIds);
                    }
                    // 保存新的规划
                    boolean a = tripDaysService.saveBatch(allTripDays);
                    boolean b = tripDayItemsService.saveBatch(allTripDayItems);
                    if (!a || !b) {
                        log.error("AI generate entire trip failed: Save to db failed: {}, {}", allTripDays, allTripDayItems);
                        status.setRollbackOnly();
                    } else {
                        log.info("AI generate entire trip success. Save to db success: {}, {}", allTripDays, allTripDayItems);
                    }
                } catch (Exception e) {
                    log.error("AI generate entire trip failed: Save to db failed", e);
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            });
            // 返回结果
            return new EntireTrip(trip, entireTripDays);
        }catch (Exception e){
            log.error("AI generate entire trip failed", e);
            throw new RuntimeException(e);
        }finally {
            tripLockUtils.unlock(rLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TripAccessValidate(lowestRole = MemberRole.OWNER) // 仅允许OWNER删除
    public boolean deleteTrip(UUID userId, UUID tripId) {
        log.info("Delete trip: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        RLock rLock = tripLockUtils.lockTrip(tripId);
        try {
            boolean exists = lambdaQuery().eq(Trips::getTripId, tripId).eq(Trips::getUserId, userId).exists();
            Assert.isTrue(exists, Constant.TRIP_NOT_EXIST);
            // 级联删除该旅程tripId下的所有日程和日程下的所有item
            List<UUID> tripDayIds = tripDaysService.lambdaQuery()
                    .eq(TripDays::getTripId, tripId)
                    .list().stream().map(TripDays::getTripDayId).toList();
            // 这个删除方法会自动删除所有日程下的所有item
            if (!tripDayIds.isEmpty()) {
                // 日程不为空，删除所有日程
                tripDaysService.deleteTripDays(userId, tripId, tripDayIds);
            } else {
                log.info("Trip days is empty: tripId {}", tripId);
            }
            // 删除旅程和成员
            tripMembersService.deleteMembersByTripId(tripId, userId);
            lambdaUpdate().eq(Trips::getTripId, tripId).remove();
            log.info("Delete trip success: {}", tripId);
            return true;
        } finally {
            tripLockUtils.unlock(rLock);
        }
    }

}




