package com.bread.traveler.service;

import com.bread.traveler.dto.TripDayItemDto;
import com.bread.traveler.entity.TripDayItems;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_day_items】的数据库操作Service
* @createDate 2025-11-25 11:45:06
*/
public interface TripDayItemsService extends IService<TripDayItems> {

    /**
     * 添加POI、NonPoi到日程当中，默认为最后一个item
     * @param tripDayId     日程ID
     * @param entityId      POI/NonPoi的ID
     * @param isPoi         true:POI, false:NonPoi
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param estimatedCost 预计花费
     * @return
     */
    TripDayItems addItems(UUID tripDayId, UUID entityId, boolean isPoi, LocalTime startTime, LocalTime endTime, BigDecimal estimatedCost);

    /**
     * 删除日程中的POI、NonPoi
     * @param itemIds
     * @return
     */
    boolean deleteItems(List<UUID> itemIds);

    /**
     * 更新日程item的信息，包括时间、花费、交通建议
     * @param dto
     * @return
     */
    TripDayItems updateItemInfo(TripDayItemDto dto);

    /**
     * 移动日程item的位置
     * @param currentId 当前item的ID
     * @param prevId 前一个item的ID(如果移动到第一位，则为 null)
     * @param nextId 后一个item的ID(如果移动到最后一位，则为 null)
     * @param tripDayId 移动后所属的天 (用于处理跨天移动的情况)
     * @return
     */
    boolean moveItemOrder(UUID currentId, UUID prevId, UUID nextId, UUID tripDayId);

    /**
     * 更新日程item的交通建议，调用高德api接口更新
     * @param itemId
     * @return
     */
    TripDayItems updateTransportNote(UUID itemId);

}
