package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.bread.traveler.enums.EnumTypeHandler;
import com.bread.traveler.enums.LogType;
import lombok.Data;

/**
 * 
 * @TableName trip_logs
 * 旅程日志表，用于记录用户在旅程中的日志
 */
@TableName(value ="trip_logs", autoResultMap = true)
@Data
public class TripLogs implements Serializable {

    @TableId(value = "log_id")
    private UUID logId;

    /**
     * 旅程id
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 类型
     */
    @TableField(value = "log_type", typeHandler = EnumTypeHandler.class)
    private LogType logType;

    @TableField(value = "content")
    private String content;

    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

    /**
     * 创建用户id
     */
    @TableField(value = "user_id")
    private UUID userId;

    @TableField(value = "is_public")
    private Boolean isPublic;
}