package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName wishlist_items
 */
@TableName(value ="wishlist_items")
@Data
@Schema(description = "心愿单项目")
public class WishlistItems implements Serializable {
    /**
     * 
     */
    @TableId(value = "item_id")
    @Schema(description = "项目ID")
    private UUID itemId;

    /**
     * 当前心愿单item所属的trip_id
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * poiId或者nonPoiId
     */
    @TableField(value = "entity_id")
    @Schema(description = "实体ID")
    private UUID entityId;

    /**
     * 是否为poi类型
     */
    @TableField(value = "is_poi")
    @Schema(description = "是否为POI", example = "true")
    private Boolean isPoi;

    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}