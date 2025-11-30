package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.bread.traveler.enums.EnumTypeHandler;
import com.bread.traveler.enums.TripStatus;
import com.bread.traveler.typehandler.JsonbTypeHandler;
import lombok.Data;

/**
 * 
 * @TableName trips
 */
@TableName(value ="trips", autoResultMap = true)
@Data
@Schema(description = "旅程")
public class Trips implements Serializable {
    /**
     * 
     */
    @TableId(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 创建的用户id，OWNER
     */
    @TableField(value = "user_id")
    @Schema(description = "创建者ID")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "title")
    @Schema(description = "标题", example = "北京三日游")
    private String title;

    /**
     * 
     */
    @TableField(value = "destination_city")
    @Schema(description = "目的地城市", example = "北京")
    private String destinationCity;

    /**
     * 
     */
    @TableField(value = "start_date")
    @Schema(description = "开始日期", example = "2024-12-01")
    private LocalDate startDate;

    /**
     * 
     */
    @TableField(value = "end_date")
    @Schema(description = "结束日期", example = "2024-12-03")
    private LocalDate endDate;

    /**
     * 
     */
    @TableField(value = "total_budget")
    @Schema(description = "总预算", example = "3000.00")
    private BigDecimal totalBudget;

    /**
     * 
     */
    @TableField(value = "status", typeHandler = EnumTypeHandler.class)
    @Schema(description = "状态", example = "PLANNED")
    private TripStatus status;

    /**
     * 
     */
    @TableField(value = "description")
    @Schema(description = "描述", example = "北京三日游计划")
    private String description;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    @TableField(value = "is_private")
    @Schema(description = "是否私有", example = "false")
    private Boolean isPrivate;

}