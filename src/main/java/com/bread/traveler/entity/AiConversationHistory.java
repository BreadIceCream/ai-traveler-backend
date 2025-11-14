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

import com.bread.traveler.enums.RoleType;
import lombok.Data;

/**
 * 
 * @TableName ai_conversation_history
 */
@TableName(value ="ai_conversation_history")
@Data
public class AiConversationHistory implements Serializable {
    /**
     * 
     */
    @TableId(value = "message_id")
    private UUID messageId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "role")
    private RoleType role;

    /**
     * 
     */
    @TableField(value = "message_content")
    private String messageContent;

    /**
     * 
     */
    @TableField(value = "timestamp")
    private OffsetDateTime timestamp;

}