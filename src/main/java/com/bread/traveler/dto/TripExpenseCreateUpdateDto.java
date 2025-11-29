package com.bread.traveler.dto;

import com.bread.traveler.enums.ExpenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Schema(description = "支出创建参数")
public class TripExpenseCreateUpdateDto {

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
    @Schema(description = "分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private ExpenseType category;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "支出时间")
    private OffsetDateTime expenseTime;

}
