package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @TableName trip_logs
 */
@TableName(value ="trip_logs")
@Data
@Schema(description = "旅程日志")
public class TripLogs implements Serializable {
    /**
     * 
     */
    @TableId(value = "log_id")
    @Schema(description = "日志ID")
    private UUID logId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "content")
    @Schema(description = "日志文本内容")
    private String content;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private UUID userId;

    /**
     * 是否公开
     */
    @TableField(value = "is_public")
    @Schema(description = "是否公开")
    private Boolean isPublic;

    /**
     * 图片url集合，存储List结合的json字符串
     */
    @TableField(value = "imgs")
    @Schema(description = "图片url集合，存储List结合的json字符串")
    private String imgs;
}