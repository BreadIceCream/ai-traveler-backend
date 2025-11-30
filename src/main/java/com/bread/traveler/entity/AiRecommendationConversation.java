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

import lombok.Data;

/**
 * 
 * @TableName ai_recommendation_conversation
 */
@TableName(value ="ai_recommendation_conversation")
@Data
@Schema(description = "AI推荐会话")
public class AiRecommendationConversation implements Serializable {
    /**
     * 
     */
    @TableId(value = "conversation_id")
    @Schema(description = "会话ID")
    private UUID conversationId;

    /**
     * 
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "title")
    @Schema(description = "标题", example = "北京旅游推荐")
    private String title;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;
}