package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.UUID;

import com.bread.traveler.enums.MemberRole;
import lombok.Data;

/**
 * 
 * @TableName trip_members
 */
@TableName(value ="trip_members")
@Data
public class TripMembers implements Serializable {
    /**
     * 
     */
    @TableId(value = "trip_id")
    private UUID tripId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private UUID userId;

    /**
     * 
     */
    @TableField(value = "role")
    private MemberRole role;

}