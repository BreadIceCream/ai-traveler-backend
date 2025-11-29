package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class TripExpenses implements Serializable {
    /**
     * 
     */
    @TableId(value = "expense_id")
    private UUID expenseId;

    @TableField(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 支出分类
     */
    @TableField(value = "category", typeHandler = EnumTypeHandler.class)
    private ExpenseType category;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "expense_time")
    private OffsetDateTime expenseTime;
}