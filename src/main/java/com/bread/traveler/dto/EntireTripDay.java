package com.bread.traveler.dto;

import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.entity.TripDays;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "完整日程信息")
public class EntireTripDay{
    
    @Schema(description = "日程基本信息", implementation = TripDays.class)
    private TripDays tripDay;
    
    @Schema(description = "日程项列表", implementation = EntireTripDayItem.class)
    private List<EntireTripDayItem> tripDayItems;
}
