package com.bread.traveler.dto;

import com.bread.traveler.entity.Trips;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "完整旅程信息")
public class EntireTrip {

    @Schema(description = "旅程基本信息", implementation = Trips.class)
    private Trips trip;
    
    @Schema(description = "旅程日程列表", implementation = EntireTripDay.class)
    private List<EntireTripDay> tripDays;

}
