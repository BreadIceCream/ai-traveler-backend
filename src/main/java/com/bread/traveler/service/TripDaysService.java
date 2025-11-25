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
     * 创建行程中的日程，默认为最后一天
     * @param tripId
     * @param date
     * @param note
     * @return
     */
    TripDays createTripDay(UUID tripId, LocalDate date, String note);

    /**
     * 更新行程中的日程，更新时间、note
     * @param tripDayId
     * @param date
     * @param note
     * @return
     */
    boolean updateTripDay(UUID tripDayId, LocalDate date, String note);

    /**
     * 获取行程中某天的详情，包括每个item
     * @param tripDayId
     * @return
     */
    EntireTripDay getEntireTripDay(UUID tripDayId);

    /**
     * AI智能规划，重新规划某天的行程
     * 需要获取该天的所有item并重新规划
     * @param tripDayId
     * @return
     */
    EntireTripDay aiRePlanTripDay(UUID tripDayId);

    /**
     * 移动行程中某天的位置
     * @param currentId 当前tripDay的ID
     * @param prevId    前一个tripDay的ID，为null则代表最前面
     * @param nextId    后一个tripDay的ID，为null则代表最后面
     * @return
     */
    boolean moveDayOrder(UUID currentId, UUID prevId, UUID nextId);

    /**
     * 删除行程中的日程，级联删除该日程下的所有item
     * @param tripDayIds
     * @return
     */
    boolean deleteTripDays(List<UUID> tripDayIds);

}
