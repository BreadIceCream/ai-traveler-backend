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
     * @param tripDayId 日程ID
     * @param entityId  POI/NonPoi的ID
     * @param isPoi     true:POI, false:NonPoi
     * @param dto       时间、花费、交通建议、备注
     * @return
     */
    TripDayItems addItems(UUID tripDayId, UUID entityId, boolean isPoi, TripDayItemDto dto);

    /**
     * 删除日程中的POI、NonPoi
     * @param itemIds
     * @return
     */
    boolean deleteItems(List<UUID> itemIds);

    /**
     * 更新日程item的信息，包括时间、花费、交通建议、备注
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
     * AI更新日程item的交通建议
     * 使用高德MCP或高德API。从上一个地点到当前地点
     * @param itemId
     * @return
     */
    TripDayItems updateTransportNote(UUID itemId, @Nullable String originAddress);

    /**
     * 获取某个日程中的所有item
     * @param tripDayId
     * @return items有序集合，若没有item则为空
     */
    List<TripDayItems> getItemsByTripDayId(UUID tripDayId);

    /**
     * 获取某个日程中的所有item，包括item的POI/NonPoi信息
     * @param tripDayId
     * @return EntireTripDayItem有序集合，若没有item则为空
     */
    List<EntireTripDayItem> getEntireItemsByTripDayId(UUID tripDayId);

}
