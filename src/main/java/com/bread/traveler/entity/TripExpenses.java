package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.bread.traveler.enums.EnumTypeHandler;
import com.bread.traveler.enums.ExpenseType;
import lombok.Data;

/**
 * 
 * @TableName expenses
 */
@TableName(value ="trip_expenses", autoResultMap = true)
@Data
@Schema(description = "旅程支出")
public class TripExpenses implements Serializable {
    /**
     * 
     */
    @TableId(value = "expense_id")
    @Schema(description = "支出ID")
    private UUID expenseId;

    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "amount")
    @Schema(description = "金额", example = "150.00")
    private BigDecimal amount;

    /**
     * 支出分类
     */
    @TableField(value = "category", typeHandler = EnumTypeHandler.class)
    @Schema(description = "分类", example = "ACCOMMODATION")
    private ExpenseType category;

    /**
     * 
     */
    @TableField(value = "description")
    @Schema(description = "描述", example = "酒店住宿费用")
    private String description;

    /**
     * 
     */
    @TableField(value = "expense_time")
    @Schema(description = "支出时间")
    private OffsetDateTime expenseTime;
}