package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Schema(description = "日程项dto")
public class TripDayItemDto {

    @Schema(description = "日程项id")
    private UUID itemId;
    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalTime startTime;
    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalTime endTime;
    @Schema(description = "交通建议", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String transportNotes;
    @Schema(description = "预计花费", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal estimatedCost;
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String notes;


}
