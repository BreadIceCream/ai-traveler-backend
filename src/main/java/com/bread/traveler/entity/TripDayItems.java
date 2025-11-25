package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName trip_day_items
 */
@TableName(value ="trip_day_items")
@Data
public class TripDayItems implements Serializable {
    /**
     * 
     */
    @TableId(value = "item_id")
    private UUID itemId;

    /**
     * 
     */
    @TableField(value = "trip_day_id")
    private UUID tripDayId;

    /**
     * 
     */
    @TableField(value = "entity_id")
    private UUID entityId;

    /**
     * 
     */
    @TableField(value = "start_time")
    private LocalTime startTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    private LocalTime endTime;

    /**
     * 
     */
    @TableField(value = "item_order")
    private Double itemOrder;

    /**
     * 交通建议，从上一个地点到当前地点
     */
    @TableField(value = "transport_notes")
    private String transportNotes;

    /**
     * 
     */
    @TableField(value = "estimated_cost")
    private BigDecimal estimatedCost;

    /**
     * 是否为poi类型
     */
    @TableField(value = "is_poi")
    private Boolean isPoi;
}