package com.bread.traveler.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.enums.EnumTypeHandler;
import lombok.Data;

@Data
public class NonPoiItemDto {

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
}
