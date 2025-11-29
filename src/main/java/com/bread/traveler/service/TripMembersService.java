package com.bread.traveler.service;

import com.bread.traveler.dto.TripMemberDto;
import com.bread.traveler.entity.TripMembers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.MemberRole;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_members】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripMembersService extends IService<TripMembers> {

    /**
     * 添加成员请求
     * @param tripId
     * @param requestUserId
     * @return
     */
    boolean addMemberRequest(UUID tripId, UUID requestUserId);

    /**
     * 创建旅程拥有者
     * 供TripsService调用
     * @param tripId 旅程id
     * @param userId 创建用户id
     * @return
     */
    boolean createOwner(UUID tripId, UUID userId);

    /**
     * 处理成员请求
     * 只有OWNER可以处理成员请求
     * @param tripId       旅程id
     * @param userId       当前用户id
     * @param handleUserId 待处理成员id
     * @param accept       是否接受
     * @return
     */
    boolean handleMemberRequest(UUID tripId, UUID userId, UUID handleUserId , Boolean accept);

    /**
     * 删除成员
     * 只有OWNER可以删除成员
     * @param tripId        旅程id
     * @param userId        当前用户id
     * @param handleUserId  待处理成员id
     * @return
     */
    boolean deleteMember(UUID tripId, UUID userId, UUID handleUserId);

    /**
     * 删除Trip的全部成员
     * 供TripsService使用
     * @param tripId
     * @param userId
     * @return
     */
    boolean deleteMembersByTripId(UUID tripId, UUID userId);

    /**
     * 更新成员角色。
     * 只有OWNER可以更新成员角色
     * 暂时不使用该方法，成员除了OWNER外都是VIEWER，保证只有OWNER可以修改TRIP旅程信息
     * @param tripId       旅程id
     * @param userId       当前用户id
     * @param handleUserId 待处理成员id
     * @param newRole      新的角色
     * @return
     */
    boolean updateMemberRole(UUID tripId, UUID userId, UUID handleUserId, MemberRole newRole);

    /**
     * 获取旅程的所有成员
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param isPass 是否通过批准。若为null返回所有成员
     * @return 成员列表，含用户信息。如果没有匹配的成员，则返回空列表
     */
    List<TripMemberDto> getMembers(UUID userId, UUID tripId, @Nullable Boolean isPass);

    /**
     * 获取用户加入的旅程（包括申请但未通过的isPass=false)
     * 供TripsService调用
     * @param userId
     * @return TripMembers列表，按照加入的顺序倒叙排序。如果没有，则返回空列表
     */
    List<TripMembers> getJoinedTrips(UUID userId);


}
