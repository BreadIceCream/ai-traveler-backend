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
 * @TableName packing_list_items
 */
@TableName(value ="packing_list_items")
@Data
public class PackingListItems implements Serializable {
    /**
     * 
     */
    @TableId(value = "packing_item_id")
    private UUID packingItemId;

    /**
     * 
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "item_name")
    private String itemName;

    /**
     * 
     */
    @TableField(value = "is_checked")
    private Boolean isChecked;

    /**
     * 
     */
    @TableField(value = "category")
    private String category;

}