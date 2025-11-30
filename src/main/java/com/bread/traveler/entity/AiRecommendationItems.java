package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "AI推荐项")
public class AiRecommendationItems implements Serializable {
    /**
     * 推荐的entity_id
     */
    @TableId(value = "entity_id")
    @Schema(description = "实体ID")
    private UUID entityId;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "conversation_id")
    @Schema(description = "会话ID")
    private UUID conversationId;

    @TableField(value = "is_manual")
    @Schema(description = "是否手动添加", example = "false")
    private Boolean isManual;

    /**
     * 
     */
    @TableField(value = "is_poi")
    @Schema(description = "是否为POI", example = "true")
    private Boolean isPoi;

}