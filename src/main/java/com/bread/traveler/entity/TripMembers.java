package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "旅程成员")
public class TripMembers implements Serializable {

    @TableId(value = "id")
    @Schema(description = "成员ID")
    private UUID id;
    /**
     * 
     */
    @TableField(value = "trip_id")
    @Schema(description = "旅程ID")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "role", typeHandler = EnumTypeHandler.class)
    @Schema(description = "成员角色", example = "VIEWER")
    private MemberRole role;

    /**
     * 是否经过创建者同意
     */
    @TableField(value = "is_pass")
    @Schema(description = "申请是否已通过", example = "false")
    private Boolean isPass;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}