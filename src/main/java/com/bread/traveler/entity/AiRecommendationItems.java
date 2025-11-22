package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName ai_recommendation_items
 */
@TableName(value ="ai_recommendation_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @TableField(value = "is_manual")
    private Boolean isManual;

    /**
     * 
     */
    @TableField(value = "is_poi")
    private Boolean isPoi;

}