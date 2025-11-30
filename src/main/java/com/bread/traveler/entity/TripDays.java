package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "旅程日程")
public class TripDays implements Serializable {
    /**
     * 
     */
    @TableId(value = "trip_day_id")
    @Schema(description = "日程ID")
    private UUID tripDayId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "day_date")
    @Schema(description = "日期", example = "2024-12-01")
    private LocalDate dayDate;

    /**
     * 
     */
    @TableField(value = "notes")
    @Schema(description = "备注", example = "第一天的行程安排")
    private String notes;

}