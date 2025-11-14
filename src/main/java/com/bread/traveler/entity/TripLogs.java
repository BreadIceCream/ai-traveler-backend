package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.bread.traveler.enums.LogType;
import lombok.Data;

/**
 * 
 * @TableName trip_logs
 */
@TableName(value ="trip_logs")
@Data
public class TripLogs implements Serializable {
    /**
     * 
     */
    @TableId(value = "log_id")
    private UUID logId;

    /**
     * 
     */
    @TableField(value = "trip_day_id")
    private UUID tripDayId;

    /**
     * 
     */
    @TableField(value = "log_type")
    private LogType logType;

    /**
     * 
     */
    @TableField(value = "content")
    private String content;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

}