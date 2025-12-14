package com.bread.traveler.service;

import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.entity.TripDays;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * @author huang
 * @description 针对表【trip_days】的数据库操作Service
 * @createDate 2025-11-14 12:09:43
 */
public interface TripDaysService extends IService<TripDays> {

    @Data
    class AiPlanTripDay {
        private String summaryNotes;
        private List<AiPlanTripDayItem> orderedItems;
    }

    @Data
    class AiPlanTripDayItem {
        private UUID itemId;
        private LocalTime startTime;
        private LocalTime endTime;
        private BigDecimal estimatedCost;
        private String notes;
    }

    /**
     * 创建旅程中的日程，必须指定date
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param date
     * @param note
     * @return
     */
    TripDays createTripDay(UUID userId, UUID tripId, LocalDate date, String note);

    /**
     * 更新日程的note
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayId
     * @param note
     * @return
     */
    boolean updateTripDayNote(UUID userId, UUID tripId, UUID tripDayId, String note);

    /**
     * 获取旅程中某天的详情，包括每个item
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayId
     * @return
     */
    EntireTripDay getEntireTripDay(UUID userId, UUID tripId, UUID tripDayId);

    /**
     * 获取旅程中所有的日程详情，包括每个日程的item
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @return 日程有序集合，按照日期进行排序。如果旅程中没有日程，则返回空集合
     */
    List<EntireTripDay> getEntireTripDaysByTripId(UUID userId, UUID tripId);

    /**
     * AI智能规划，重新规划某天的日程，只考虑有地址的item，没有地址的item会被忽略放到最后
     * 需要获取该天的item并重新规划。
     * 会更新该tripDay的note、tripDayItems的note和estimatedCost（追加的形式，字符串拼接，cost值累加）
     * 4个item总耗时约40s左右
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayId
     * @return
     */
    EntireTripDay aiRePlanTripDay(UUID userId, UUID tripId, UUID tripDayId);

    /**
     * 交换两个日程的顺序
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param aTripDayId
     * @param bTripDayId
     * @return
     */
    boolean exchangeDayOrder(UUID userId, UUID tripId, UUID aTripDayId, UUID bTripDayId);

    /**
     * 删除旅程中的日程，级联删除该日程下的所有item
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayIds
     * @return
     */
    boolean deleteTripDays(UUID userId, UUID tripId, List<UUID> tripDayIds);

}
