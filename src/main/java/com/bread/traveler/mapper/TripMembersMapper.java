package com.bread.traveler.mapper;

import com.bread.traveler.entity.TripMembers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bread.traveler.enums.MemberRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_members】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.TripMembers
*/
@Mapper
public interface TripMembersMapper extends BaseMapper<TripMembers> {

    /**
     * 更新用户权限
     * @param tripId
     * @param handleUserId
     * @param newRole
     * @return
     */
    @Update("update trip_members set role = #{newRole}::member_role where trip_id = #{tripId} and user_id = #{handleUserId}")
    int updateMemberRole(UUID tripId, UUID handleUserId, MemberRole newRole);
}




