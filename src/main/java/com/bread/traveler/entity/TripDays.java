package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName trip_days
 */
@TableName(value ="trip_days")
@Data
public class TripDays implements Serializable {
    /**
     * 
     */
    @TableId(value = "trip_day_id")
    private UUID tripDayId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "day_date")
    private LocalDate dayDate;

    /**
     * 
     */
    @TableField(value = "notes")
    private String notes;

}