package com.bread.traveler.service;

import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.entity.TripDays;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_days】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripDaysService extends IService<TripDays> {

    /**
     * 创建行程中的日程，必须指定date
     * @param tripId
     * @param date
     * @param note
     * @return
     */
    TripDays createTripDay(UUID tripId, LocalDate date, String note);

    /**
     * 更新日程的note
     * @param tripDayId
     * @param note
     * @return
     */
    boolean updateTripDayNote(UUID tripDayId, String note);

    /**
     * 获取行程中某天的详情，包括每个item
     * @param tripDayId
     * @return
     */
    EntireTripDay getEntireTripDay(UUID tripDayId);

    /**
     * 获取行程中所有的日程详情，包括每个日程的item
     * @param tripId
     * @return 日程有序集合，按照日期进行排序。如果行程中没有日程，则返回空集合
     */
    List<EntireTripDay> getEntireTripDaysByTripId(UUID tripId);

    /**
     * AI智能规划，重新规划某天的行程
     * 需要获取该天的所有item并重新规划
     * @param tripDayId
     * @return
     */
    EntireTripDay aiRePlanTripDay(UUID tripDayId);

    /**
     * 交换两个日程的顺序
     * @param aTripDayId
     * @param bTripDayId
     * @return
     */
    boolean exchangeDayOrder(UUID aTripDayId, UUID bTripDayId);

    /**
     * 删除行程中的日程，级联删除该日程下的所有item
     * @param tripDayIds
     * @return
     */
    boolean deleteTripDays(List<UUID> tripDayIds);

}
