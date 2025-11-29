package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Trips implements Serializable {
    /**
     * 
     */
    @TableId(value = "trip_id")
    private UUID tripId;

    /**
     * 创建的用户id，OWNER
     */
    @TableField(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "title")
    private String title;

    /**
     * 
     */
    @TableField(value = "destination_city")
    private String destinationCity;

    /**
     * 
     */
    @TableField(value = "start_date")
    private LocalDate startDate;

    /**
     * 
     */
    @TableField(value = "end_date")
    private LocalDate endDate;

    /**
     * 
     */
    @TableField(value = "total_budget")
    private BigDecimal totalBudget;

    /**
     * 
     */
    @TableField(value = "status", typeHandler = EnumTypeHandler.class)
    private TripStatus status;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

    @TableField(value = "is_private")
    private Boolean isPrivate;

}