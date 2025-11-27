package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.EntireTrip;
import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.dto.TripDto;
import com.bread.traveler.entity.TripDays;
import com.bread.traveler.entity.Trips;
import com.bread.traveler.enums.TripStatus;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripDaysService;
import com.bread.traveler.service.TripsService;
import com.bread.traveler.mapper.TripsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trips】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
@Slf4j
public class TripsServiceImpl extends ServiceImpl<TripsMapper, Trips> implements TripsService{

    @Autowired
    private TripDaysService tripDaysService;

    @Override
    public Trips createTrip(UUID userId, TripDto dto) {
        log.info("Create trip: user {}, dto {}", userId, dto);
        Assert.notNull(userId, "userId cannot be null");
        Trips trip = BeanUtil.copyProperties(dto, Trips.class, "tripId");
        trip.setTripId(UUID.randomUUID());
        trip.setUserId(userId);
        trip.setStatus(TripStatus.PLANNING);
        trip.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(trip)) {
            log.info("Create trip success: {}", trip.getTripId());
            return trip;
        }
        log.error("Create trip failed: {}", trip);
        throw new RuntimeException(Constant.TRIP_CREATE_FAILED);
    }

    @Override
    public Trips updateTripInfo(UUID userId, TripDto dto) {
        log.info("Update trip info: user {}, dto {}", userId, dto);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(dto, "dto cannot be null");
        Assert.notNull(dto.getTripId(), "tripId cannot be null");
        Trips trip = lambdaQuery().eq(Trips::getTripId, dto.getTripId()).eq(Trips::getUserId, userId).one();
        if (trip == null) {
            log.error("Trip not found: {}", dto.getTripId());
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
    public List<Trips> getAllTripsOfUser(UUID userId) {
        log.info("Get all trips of user: {}", userId);
        Assert.notNull(userId, "userId cannot be null");
        List<Trips> result = lambdaQuery().eq(Trips::getUserId, userId).list();
        if (result == null || result.isEmpty()){
            log.info("No trips found for user: {}", userId);
            return Collections.emptyList();
        }
        // 按照创建时间倒序排序
        result.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return result;
    }

    @Override
    public EntireTrip getEntireTrip(UUID userId, UUID tripId) {
        log.info("Get entire trip: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        Trips trip = lambdaQuery().eq(Trips::getTripId, tripId).eq(Trips::getUserId, userId).one();
        if (trip == null) {
            log.error("Trip not found: {}", tripId);
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }
        List<EntireTripDay> tripDays = tripDaysService.getEntireTripDaysByTripId(tripId);
        return new EntireTrip(trip, tripDays);
    }

    @Override //todo ai智能生成行程
    public EntireTrip aiGenerateEntireTrip(UUID userId, UUID tripId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTrip(UUID userId, UUID tripId) {
        log.info("Delete trip: user {}, trip {}", userId, tripId);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(tripId, "tripId cannot be null");
        boolean exists = lambdaQuery().eq(Trips::getTripId, tripId).eq(Trips::getUserId, userId).exists();
        Assert.isTrue(exists, "Trip not found");
        // 级联删除该行程tripId下的所有日程和日程下的所有item
        List<UUID> tripDayIds = tripDaysService.lambdaQuery()
                .eq(TripDays::getTripId, tripId)
                .list().stream().map(TripDays::getTripDayId).toList();
        // 这个删除方法会自动删除所有日程下的所有item
        if (!tripDayIds.isEmpty()){
            // 日程不为空，删除所有日程
            tripDaysService.deleteTripDays(tripDayIds);
        }else{
            log.info("Trip days is empty: tripId {}", tripId);
        }
        lambdaUpdate().eq(Trips::getTripId, tripId).remove();
        log.info("Delete trip success: {}", tripId);
        return true;
    }

}




