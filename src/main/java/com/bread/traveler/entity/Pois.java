package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bread.traveler.enums.PoiType;
import com.bread.traveler.typehandler.JsonbTypeHandler;
import com.bread.traveler.typehandler.ListStringTypeHandler;
import com.bread.traveler.typehandler.VectorTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 
 * @TableName pois
 */
@TableName(value ="pois")
@Data
@Builder
public class Pois implements Serializable {
    /**
     * 
     */
    @TableId(value = "poi_id")
    private UUID poiId;

    /**
     * 
     */
    @TableField(value = "external_api_id")
    private String externalApiId;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型描述信息
     */
    @TableField(value = "type")
    private String type;

    @TableField(value = "city")
    private String city;

    /**
     * 
     */
    @TableField(value = "address")
    private String address;

    /**
     * 
     */
    @TableField(value = "latitude")
    private BigDecimal latitude;

    /**
     * 
     */
    @TableField(value = "longitude")
    private BigDecimal longitude;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "opening_hours")
    private String openingHours;

    /**
     * 
     */
    @TableField(value = "avg_visit_duration")
    private Integer avgVisitDuration;

    /**
     * 
     */
    @TableField(value = "avg_cost")
    private String avgCost;

    @TableField(value = "photos", typeHandler = ListStringTypeHandler.class)
    private List<String> photos;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "rating")
    private String rating;


    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

}