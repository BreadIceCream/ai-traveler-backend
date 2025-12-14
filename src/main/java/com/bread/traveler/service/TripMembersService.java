package com.bread.traveler.service;

import com.bread.traveler.dto.TripMemberDto;
import com.bread.traveler.dto.TripMemberPendingRequestDto;
import com.bread.traveler.entity.TripMembers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.MemberRole;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_members】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripMembersService extends IService<TripMembers> {

    /**
     * 用户申请加入旅程
     * 只能申请加入PUBLIC且PLANNING状态的旅程
     * @param tripId 旅程id
     * @param userId 当前用户id（请求用户id）
     * @return
     */
    boolean addMemberRequest(UUID tripId, UUID userId);

    /**
     * 邀请成员加入旅程，创建者可以邀请其他用户加入旅程
     * 只有OWNER可以邀请成员加入
     *
     * @param tripId 旅程id
     * @param userId 当前用户id
     * @param inviteUserIds 邀请的用户id列表
     * @return
     */
    boolean inviteMembers(UUID tripId, UUID userId, List<UUID> inviteUserIds);

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
     * 获取旅程的所有用户
     *
     * @param userId 当前用户id
     * @param tripId 旅程id
     * @param isPass 是否通过批准。若为null返回所有用户
     * @return 成员列表，含用户信息。如果没有匹配的成员，则返回空列表
     */
    List<TripMemberDto> getTripMembers(UUID userId, UUID tripId, @Nullable Boolean isPass);

    /**
     * 获取用户加入的旅程（包括申请但未通过的isPass=false)
     * 供TripsService调用
     * @param userId
     * @return TripMembers列表，按照加入的顺序倒叙排序。如果没有，则返回空列表
     */
    List<TripMembers> getJoinedTrips(UUID userId);


    /**
     * 获取用户创建的所有行程的待处理申请数量
     * @param userId
     * @return
     */
    List<TripMemberPendingRequestDto> getPendingRequests(UUID userId);
}
