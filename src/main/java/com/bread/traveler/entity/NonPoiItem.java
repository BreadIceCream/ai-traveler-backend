package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.typehandler.EnumTypeHandler;
import lombok.Data;

/**
 * 
 * @TableName non_poi_item
 */
@TableName(value ="non_poi_item")
@Data
public class NonPoiItem implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private UUID id;

    @TableField(value = "type", typeHandler = EnumTypeHandler.class)
    private NonPoiType type;

    /**
     * 
     */
    @TableField(value = "title")
    private String title;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "city")
    private String city;

    /**
     * 
     */
    @TableField(value = "activity_time")
    private String activityTime;

    /**
     * 
     */
    @TableField(value = "estimated_address")
    private String estimatedAddress;

    /**
     * 
     */
    @TableField(value = "extra_info")
    private String extraInfo;

    /**
     * 
     */
    @TableField(value = "source_url")
    private String sourceUrl;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

    /**
     * userId
     */
    @TableField(value = "private_user_id")
    private UUID privateUserId;
}