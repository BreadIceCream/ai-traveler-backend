package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName poi_ratings
 */
@TableName(value ="poi_ratings")
@Data
public class PoiRatings implements Serializable {
    /**
     * 
     */
    @TableId(value = "rating_id")
    private UUID ratingId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "poi_id")
    private UUID poiId;

    /**
     * 
     */
    @TableField(value = "rating")
    private Integer rating;

    /**
     * 
     */
    @TableField(value = "review_text")
    private String reviewText;

    /**
     * 
     */
    @TableField(value = "visited_at")
    private OffsetDateTime visitedAt;

}