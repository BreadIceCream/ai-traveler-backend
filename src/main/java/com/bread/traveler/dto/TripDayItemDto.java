package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Schema(description = "日程项dto")
public class TripDayItemDto {

    @Schema(description = "日程项id")
    private UUID itemId;
    @Schema(description = "开始时间")
    private LocalTime startTime;
    @Schema(description = "结束时间")
    private LocalTime endTime;
    @Schema(description = "交通建议")
    private String transportNotes;
    @Schema(description = "预计花费")
    private BigDecimal estimatedCost;


}
