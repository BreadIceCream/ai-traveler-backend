package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
    @TableId(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "poi_id")
    private UUID poiId;

}