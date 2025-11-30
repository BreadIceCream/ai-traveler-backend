package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.bread.traveler.dto.ItineraryItem;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.enums.EnumTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName non_poi_item
 */
@EqualsAndHashCode(callSuper = false)
@TableName(value ="non_poi_item", autoResultMap = true)
@Data
@Schema(description = "非POI项目")
public class NonPoiItem extends ItineraryItem implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    @Schema(description = "非POI项ID")
    private UUID id;

    @TableField(value = "type", typeHandler = EnumTypeHandler.class)
    @Schema(description = "类型", example = "ACTIVITY")
    private NonPoiType type;

    /**
     * 
     */
    @TableField(value = "title")
    @Schema(description = "标题", example = "全聚德烤鸭店")
    private String title;

    /**
     * 
     */
    @TableField(value = "description")
    @Schema(description = "描述", example = "正宗北京烤鸭")
    private String description;

    /**
     * 
     */
    @TableField(value = "city")
    @Schema(description = "城市", example = "北京")
    private String city;

    /**
     * 
     */
    @TableField(value = "activity_time")
    @Schema(description = "活动时间", example = "10:00-22:00")
    private String activityTime;

    /**
     * 
     */
    @TableField(value = "estimated_address")
    @Schema(description = "预计地址", example = "北京市东城区前门大街32号")
    private String estimatedAddress;

    /**
     * 
     */
    @TableField(value = "extra_info")
    @Schema(description = "额外信息", example = "人均消费200-300元")
    private String extraInfo;

    /**
     * 
     */
    @TableField(value = "source_url")
    @Schema(description = "来源URL", example = "https://example.com")
    private String sourceUrl;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    /**
     * userId
     */
    @TableField(value = "private_user_id")
    @Schema(description = "私有用户ID")
    private UUID privateUserId;
}