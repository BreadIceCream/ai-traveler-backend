package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName expenses
 */
@TableName(value ="expenses")
@Data
public class Expenses implements Serializable {
    /**
     * 
     */
    @TableId(value = "expense_id")
    private UUID expenseId;

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
     * 
     */
    @TableField(value = "category")
    private String category;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "expense_date")
    private OffsetDateTime expenseDate;

}