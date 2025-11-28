package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class WishlistItems implements Serializable {
    /**
     * 
     */
    @TableId(value = "item_id")
    private UUID itemId;

    /**
     * 当前心愿单item所属的trip_id
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * poiId或者nonPoiId
     */
    @TableField(value = "entity_id")
    private UUID entityId;

    /**
     * 是否为poi类型
     */
    @TableField(value = "is_poi")
    private Boolean isPoi;

    @TableField(value = "created_at")
    private OffsetDateTime createdAt;
}