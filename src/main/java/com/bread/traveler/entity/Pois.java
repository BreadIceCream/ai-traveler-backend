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
import java.util.Map;
import java.util.UUID;

import com.bread.traveler.enums.PoiType;
import com.bread.traveler.typehandler.JsonbTypeHandler;
import com.bread.traveler.typehandler.VectorTypeHandler;
import lombok.Data;

/**
 * 
 * @TableName pois
 */
@TableName(value ="pois")
@Data
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
     * 
     */
    @TableField(value = "type")
    private PoiType type;

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
    @TableField(value = "description_embedding", typeHandler = VectorTypeHandler.class)
    private float[] descriptionEmbedding;

    /**
     * 
     */
    @TableField(value = "opening_hours", typeHandler = JsonbTypeHandler.class)
    private Map<String, Object> openingHours;

    /**
     * 
     */
    @TableField(value = "avg_visit_duration")
    private Integer avgVisitDuration;

    /**
     * 
     */
    @TableField(value = "price_level")
    private Integer priceLevel;

    /**
     * 
     */
    @TableField(value = "created_by_user_id")
    private UUID createdByUserId;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;

}