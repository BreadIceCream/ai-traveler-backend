package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.bread.traveler.typehandler.VectorTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "username")
    private String username;

    /**
     * 
     */
    @TableField(value = "preferences_text")
    private String preferencesText;

    /**
     * 
     */
    @TableField(value = "preferences_embedding", typeHandler = VectorTypeHandler.class)
    @JsonIgnore
    private float[] preferencesEmbedding;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;
}