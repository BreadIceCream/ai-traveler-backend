package com.bread.traveler.dto;

import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 行程地点、活动单元，两个子类Pois和NonPoiItem
 */
@Schema(description = "行程项目基类", subTypes = {Pois.class, NonPoiItem.class})
public class ItineraryItem {
}
