package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.bread.traveler.dto.ItineraryItem;
import com.bread.traveler.typehandler.ListStringTypeHandler;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName pois
 */
@EqualsAndHashCode(callSuper = false)
@TableName(value ="pois")
@Data
@Builder
@Schema(description = "POI兴趣点")
public class Pois extends ItineraryItem implements Serializable {
    /**
     * 
     */
    @TableId(value = "poi_id")
    @Schema(description = "POI ID")
    private UUID poiId;

    /**
     * 
     */
    @TableField(value = "external_api_id")
    @Schema(description = "外部API ID", example = "B0FFH4XYZ")
    private String externalApiId;

    /**
     * 
     */
    @TableField(value = "name")
    @Schema(description = "名称", example = "故宫博物院")
    private String name;

    /**
     * 类型描述信息
     */
    @TableField(value = "type")
    @Schema(description = "类型", example = "museum")
    private String type;

    @TableField(value = "city")
    @Schema(description = "城市", example = "北京")
    private String city;

    /**
     * 
     */
    @TableField(value = "address")
    @Schema(description = "地址", example = "北京市东城区景山前街4号")
    private String address;

    /**
     * 
     */
    @TableField(value = "latitude")
    @Schema(description = "纬度", example = "39.9163")
    private BigDecimal latitude;

    /**
     * 
     */
    @TableField(value = "longitude")
    @Schema(description = "经度", example = "116.3972")
    private BigDecimal longitude;

    /**
     * 
     */
    @TableField(value = "description")
    @Schema(description = "描述", example = "明清两朝的皇宫，现为故宫博物院")
    private String description;

    /**
     * 
     */
    @TableField(value = "opening_hours")
    @Schema(description = "开放时间", example = "08:30-17:00")
    private String openingHours;

    /**
     * 
     */
    @TableField(value = "avg_visit_duration")
    @Schema(description = "平均游览时长（分钟）", example = "180")
    private Integer avgVisitDuration;

    /**
     * 
     */
    @TableField(value = "avg_cost")
    @Schema(description = "平均花费", example = "60元")
    private String avgCost;

    @TableField(value = "photos", typeHandler = ListStringTypeHandler.class)
    @Schema(description = "照片URL列表")
    private List<String> photos;

    @TableField(value = "phone")
    @Schema(description = "电话", example = "010-85007421")
    private String phone;

    @TableField(value = "rating")
    @Schema(description = "评分", example = "4.8")
    private String rating;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

}