package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.bread.traveler.enums.EnumTypeHandler;
import com.bread.traveler.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName trip_members
 */
@TableName(value ="trip_members", autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripMembers implements Serializable {

    @TableId(value = "id")
    private UUID id;
    /**
     * 
     */
    @TableField(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "role", typeHandler = EnumTypeHandler.class)
    private MemberRole role;

    /**
     * 是否经过创建者同意
     */
    @TableField(value = "is_pass")
    private Boolean isPass;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;
}