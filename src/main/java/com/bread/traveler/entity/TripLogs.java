package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "旅程日志")
public class TripLogs implements Serializable {

    @TableId(value = "log_id")
    @Schema(description = "日志ID")
    private UUID logId;

    /**
     * 旅程id
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 类型
     */
    @TableField(value = "log_type", typeHandler = EnumTypeHandler.class)
    @Schema(description = "日志类型", example = "NOTE")
    private LogType logType;

    @TableField(value = "content")
    @Schema(description = "日志内容", example = "今天游览了故宫博物院")
    private String content;

    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    /**
     * 创建用户id
     */
    @TableField(value = "user_id")
    @Schema(description = "创建者ID")
    private UUID userId;

    @TableField(value = "is_public")
    @Schema(description = "是否公开", example = "true")
    private Boolean isPublic;
}