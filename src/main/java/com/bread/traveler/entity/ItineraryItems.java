package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName itinerary_items
 */
@TableName(value ="itinerary_items")
@Data
public class ItineraryItems implements Serializable {
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
    @TableField(value = "poi_id")
    private UUID poiId;

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
    private Integer itemOrder;

    /**
     * 
     */
    @TableField(value = "transport_notes")
    private String transportNotes;

    /**
     * 
     */
    @TableField(value = "estimated_cost")
    private BigDecimal estimatedCost;

}