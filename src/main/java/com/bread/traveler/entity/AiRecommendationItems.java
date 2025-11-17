package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.bread.traveler.enums.AiRecommendationEntityType;
import lombok.Data;

/**
 * 
 * @TableName ai_recommendation_items
 */
@TableName(value ="ai_recommendation_items")
@Data
public class AiRecommendationItems implements Serializable {
    /**
     * 推荐的entity_id
     */
    @TableId(value = "entity_id")
    private UUID entityId;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "conversation_id")
    private UUID conversationId;

    /**
     * 
     */
    @TableField(value = "entity_type")
    private AiRecommendationEntityType entityType;

}