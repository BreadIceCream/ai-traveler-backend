package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户")
public class Users implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id")
    @Schema(description = "用户ID")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "username")
    @Schema(description = "用户名", example = "testuser")
    private String username;

    @TableField(value = "password")
    @Schema(description = "密码", example = "123456")
    private String password;

    /**
     * 
     */
    @TableField(value = "preferences_text")
    @Schema(description = "偏好文本", example = "喜欢历史文化和美食")
    private String preferencesText;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}