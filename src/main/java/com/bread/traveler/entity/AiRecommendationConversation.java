package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName ai_recommendation_conversation
 */
@TableName(value ="ai_recommendation_conversation")
@Data
public class AiRecommendationConversation implements Serializable {
    /**
     * 
     */
    @TableId(value = "conversation_id")
    private UUID conversationId;

    /**
     * 
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
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private OffsetDateTime updatedAt;
}