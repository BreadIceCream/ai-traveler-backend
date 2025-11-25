package com.bread.traveler.dto;

import com.bread.traveler.enums.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Schema(description = "行程dto")
public class TripDto {

    @Schema(description = "行程id")
    private UUID tripId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "目的地城市")
    private String destinationCity;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "预算")
    private BigDecimal totalBudget;

    @Schema(description = "描述")
    private String description;

}
