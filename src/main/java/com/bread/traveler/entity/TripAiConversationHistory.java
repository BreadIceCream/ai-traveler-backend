package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.bread.traveler.enums.RoleType;
import lombok.Data;

/**
 * 
 * @TableName ai_conversation_history
 */
@TableName(value ="trip_ai_conversation_history")
@Data
@Schema(description = "AI会话历史")
public class TripAiConversationHistory implements Serializable {
    /**
     * 
     */
    @TableId(value = "message_id")
    @Schema(description = "消息ID")
    private UUID messageId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "role")
    @Schema(description = "角色", example = "user")
    private RoleType role;

    /**
     * 
     */
    @TableField(value = "message_content")
    @Schema(description = "消息内容", example = "我想去北京旅游")
    private String messageContent;

    /**
     * 
     */
    @TableField(value = "timestamp")
    @Schema(description = "时间戳")
    private OffsetDateTime timestamp;

}