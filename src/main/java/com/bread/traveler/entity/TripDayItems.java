package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName trip_day_items
 */
@TableName(value ="trip_day_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "日程项目")
public class TripDayItems implements Serializable {
    /**
     * 
     */
    @TableId(value = "item_id")
    @Schema(description = "项目ID")
    private UUID itemId;

    /**
     * 
     */
    @TableField(value = "trip_day_id")
    @Schema(description = "日程ID")
    private UUID tripDayId;

    /**
     * 
     */
    @TableField(value = "entity_id")
    @Schema(description = "实体ID")
    private UUID entityId;

    /**
     * 
     */
    @TableField(value = "start_time")
    @Schema(description = "开始时间", example = "09:00:00")
    private LocalTime startTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    @Schema(description = "结束时间", example = "10:30:00")
    private LocalTime endTime;

    /**
     * 
     */
    @TableField(value = "item_order")
    @Schema(description = "项目顺序", example = "1.0")
    private Double itemOrder;

    /**
     * 交通建议，从上一个地点到当前地点
     */
    @TableField(value = "transport_notes")
    @Schema(description = "交通建议", example = "步行10分钟")
    private String transportNotes;

    /**
     * 
     */
    @TableField(value = "estimated_cost")
    @Schema(description = "预计花费", example = "50.00")
    private BigDecimal estimatedCost;

    /**
     * 是否为poi类型
     */
    @TableField(value = "is_poi")
    @Schema(description = "是否为POI", example = "true")
    private Boolean isPoi;

    @TableField(value = "notes")
    @Schema(description = "备注", example = "需要提前预约")
    private String notes;
}