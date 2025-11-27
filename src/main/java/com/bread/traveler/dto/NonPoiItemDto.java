package com.bread.traveler.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.enums.EnumTypeHandler;
import lombok.Data;

import java.util.UUID;

@Data
public class NonPoiItemDto {

    private UUID id;
    private NonPoiType type;
    private String title;
    private String description;
    private String city;
    private String activityTime;
    private String estimatedAddress;
    private String extraInfo;
    private String sourceUrl;
}
