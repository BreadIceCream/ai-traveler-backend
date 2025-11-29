package com.bread.traveler.service;

import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.dto.TripDayItemDto;
import com.bread.traveler.entity.TripDayItems;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.NonPoiType;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_day_items】的数据库操作Service
* @createDate 2025-11-25 11:45:06
*/
public interface TripDayItemsService extends IService<TripDayItems> {



    @Data
    class PoisTransportInfo{
        private String name;
        private String type;
        private String city;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Data
    class NonPoiTransportInfo{
        private String title;
        private String description;
        private String city;
        private String estimatedAddress;
        private String extraInfo;
        private NonPoiType type;
    }

    /**
     * 添加POI、NonPoi到日程当中，默认为最后一个item
     *
     * @param userId    用户ID
     * @param tripId    旅程ID
     * @param tripDayId 日程ID
     * @param entityId  POI/NonPoi的ID
     * @param isPoi     true:POI, false:NonPoi
     * @param dto       时间、花费、交通建议、备注
     * @return
     */
    TripDayItems addItems(UUID userId, UUID tripId, UUID tripDayId, UUID entityId, boolean isPoi, TripDayItemDto dto);

    /**
     * 删除日程中的POI、NonPoi
     *
     * @param userId 用户ID
     * @param tripId 旅程ID
     * @param itemIds
     * @return
     */
    boolean deleteItems(UUID userId, UUID tripId, List<UUID> itemIds);

    /**
     * 更新日程item的信息，包括时间、花费、交通建议、备注
     *
     * @param userId 用户ID
     * @param tripId 旅程ID
     * @param dto
     * @return
     */
    TripDayItems updateItemInfo(UUID userId, UUID tripId, TripDayItemDto dto);

    /**
     * 移动日程item的位置
     *
     * @param userId    用户ID
     * @param tripId    旅程ID
     * @param currentId 当前item的ID
     * @param prevId    前一个item的ID(如果移动到第一位，则为 null)
     * @param nextId    后一个item的ID(如果移动到最后一位，则为 null)
     * @param tripDayId 移动后所属的天 (用于处理跨天移动的情况)
     * @return
     */
    boolean moveItemOrder(UUID userId, UUID tripId, UUID currentId, UUID prevId, UUID nextId, UUID tripDayId);

    /**
     * AI更新日程item的交通建议
     * 使用高德MCP或高德API。从上一个地点到当前地点
     *
     * @param userId 用户id
     * @param tripId 旅程id
     * @param itemId
     * @return
     */
    TripDayItems updateTransportNote(UUID userId, UUID tripId, UUID itemId, @Nullable String originAddress);

    /**
     * 获取某个日程中的所有item
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayId
     * @return items有序集合，若没有item则为空
     */
    List<TripDayItems> getItemsByTripDayId(UUID userId, UUID tripId, UUID tripDayId);

    /**
     * 获取某个日程中的所有item，包括item的POI/NonPoi信息
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param tripDayId
     * @return EntireTripDayItem有序集合，若没有item则为空
     */
    List<EntireTripDayItem> getEntireItemsByTripDayId(UUID userId, UUID tripId, UUID tripDayId);

}
