package com.bread.traveler.dto;

import com.bread.traveler.entity.TripDayItems;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "完整日程项信息")
public class EntireTripDayItem {

    @Schema(description = "日程项基本信息", implementation = TripDayItems.class)
    private TripDayItems item;
    
    @Schema(description = "实体信息", implementation = ItineraryItem.class)
    private ItineraryItem entity;

}
