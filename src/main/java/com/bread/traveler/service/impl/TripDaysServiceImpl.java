package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.entity.TripDays;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.service.TripDaysService;
import com.bread.traveler.mapper.TripDaysMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_days】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
@Slf4j
public class TripDaysServiceImpl extends ServiceImpl<TripDaysMapper, TripDays> implements TripDaysService{

    @Autowired
    private TripDayItemsService tripDayItemsService;

    @Override
    public TripDays createTripDay(UUID tripId, LocalDate date, String note) {
        log.info("Create trip day: {}, date: {}, note: {}", tripId, date, note);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(date, "date cannot be null");
        // 查看数据库中是否有该日期的行程
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
    }

    @Override
    public boolean updateTripDayNote(UUID tripDayId, String note) {
        log.info("Update trip day note: {}, note: {}", tripDayId, note);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        boolean update = lambdaUpdate().eq(TripDays::getTripDayId, tripDayId)
                .set(TripDays::getNotes, note).update();
        if(update){
            log.info("Update trip day note success: {}", tripDayId);
            return true;
        }
        log.error("Update trip day note failed: {}", tripDayId);
        return false;
    }

    @Override
    public EntireTripDay getEntireTripDay(UUID tripDayId) {
        log.info("Get entire trip day: {}", tripDayId);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        TripDays tripDay = getById(tripDayId);
        if (tripDay == null){
            log.error("Trip day not exist: {}", tripDayId);
            throw new BusinessException(Constant.TRIP_DAY_NOT_EXIST);
        }
        List<EntireTripDayItem> entireItems = tripDayItemsService.getEntireItemsByTripDayId(tripDayId);
        return new EntireTripDay(tripDay, entireItems);
    }

    @Override
    public List<EntireTripDay> getEntireTripDaysByTripId(UUID tripId) {
        log.info("Get entire trip days by trip id: {}", tripId);
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取该行程的所有日程
        List<TripDays> tripDays = lambdaQuery().eq(TripDays::getTripId, tripId).list();
        if (tripDays == null || tripDays.isEmpty()){
            log.info("No trip days in trip: {}", tripId);
            return Collections.emptyList();
        }
        // 按照每个日程的日期进行升序排序，并获取它们的items（EntireTripDay）
        List<EntireTripDay> result = tripDays.stream()
                .sorted(Comparator.comparing(TripDays::getDayDate))
                .map(tripDay -> {
                    List<EntireTripDayItem> entireItems = tripDayItemsService.getEntireItemsByTripDayId(tripDay.getTripDayId());
                    return new EntireTripDay(tripDay, entireItems);
                }).toList();
        log.info("Get entire trip days success: {}", tripDays);
        return result;
    }

    @Override //todo ai智能重新规划日程
    public EntireTripDay aiRePlanTripDay(UUID tripDayId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchangeDayOrder(UUID aTripDayId, UUID bTripDayId) {
        log.info("Exchange day order: {}, {}", aTripDayId, bTripDayId);
        Assert.notNull(aTripDayId, "aTripDayId cannot be null");
        Assert.notNull(bTripDayId, "bTripDayId cannot be null");
        List<TripDays> tripDays = lambdaQuery().in(TripDays::getTripDayId, aTripDayId, bTripDayId).list();
        if (tripDays == null || tripDays.size() != 2){
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
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTripDays(List<UUID> tripDayIds) {
        log.info("Delete trip days: {}", tripDayIds);
        Assert.notNull(tripDayIds, "tripDayIds cannot be null");
        Assert.notEmpty(tripDayIds, "tripDayIds cannot be empty");
        // 级联删除这些日程tripDayIds下的所有item。
        // 注意如果表中没有匹配的数据，也会返回false。不进行返回值验证
        tripDayItemsService.lambdaUpdate().in(TripDayItems::getTripDayId, tripDayIds).remove();
        // 删除这些日程
        lambdaUpdate().in(TripDays::getTripDayId, tripDayIds).remove();
        log.info("Delete trip days success: {}", tripDayIds);
        return true;
    }
}




