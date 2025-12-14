package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.annotation.TripAccessValidate;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.TripMemberDto;
import com.bread.traveler.dto.TripMemberPendingRequestDto;
import com.bread.traveler.entity.TripMembers;
import com.bread.traveler.entity.Trips;
import com.bread.traveler.entity.Users;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.enums.TripStatus;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripMembersService;
import com.bread.traveler.mapper.TripMembersMapper;
import com.bread.traveler.service.TripsService;
import com.bread.traveler.service.UsersService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【trip_members】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class TripMembersServiceImpl extends ServiceImpl<TripMembersMapper, TripMembers> implements TripMembersService {

    @Autowired
    @Lazy
    private TripsService tripsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    @Lazy
    private TripMembersService selfProxy;

    @Override
    public boolean addMemberRequest(UUID tripId, UUID userId) {
        log.info("Adding member request for trip: {}, user: {}", tripId, userId);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        // 查看trip是否存在
        Trips trip = tripsService.getById(tripId);
        Assert.notNull(trip, Constant.TRIP_NOT_EXIST);
        // 查看trip是否public且状态为PLANNING
        Assert.isTrue(!trip.getIsPrivate() && TripStatus.PLANNING.equals(trip.getStatus()),
                Constant.TRIP_MEMBER_REQUEST_DENIED);
        // 查看用户是否存在
        Users user = usersService.getById(userId);
        Assert.notNull(user, Constant.USERS_NOT_EXIST);
        // 查看该成员是否已经发送过请求
        TripMembers member = lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, userId).one();
        if (member != null){
            throw member.getIsPass() ? new BusinessException(Constant.TRIP_MEMBER_EXIST) : new BusinessException(Constant.TRIP_MEMBER_REQUEST_EXIST);
        }
        // 添加成员请求。默认为VIEWER，未通过待处理
        TripMembers tripMember = new TripMembers(UUID.randomUUID(), tripId, userId, MemberRole.VIEWER, false, OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(tripMember)) {
            log.info("Add member request success: {}", tripMember);
            return true;
        }
        log.error("Add member request failed: {}", tripMember);
        return false;
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER)
    public boolean inviteMembers(UUID tripId, UUID userId, List<UUID> inviteUserIds) {
        log.info("Inviting member: trip {}, user {}, inviteUserId {}", tripId, userId, inviteUserIds);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        Assert.isTrue(inviteUserIds != null && !inviteUserIds.isEmpty(),
                Constant.TRIP_MEMBER_INVITE_USER_NOT_EMPTY);
        // 获取邀请的users（表中不存在的user会被忽略，以表为准）
        List<Users> inviteUsers = usersService.lambdaQuery().in(Users::getUserId, inviteUserIds).list();
        if (inviteUsers == null || inviteUsers.isEmpty()) {
            log.error("Invite user not exist: {}", inviteUserIds);
            throw new BusinessException(Constant.USERS_NOT_EXIST);
        }
        // 获取当前旅程已添加的用户
        List<TripMembers> joinedMembers = lambdaQuery().eq(TripMembers::getTripId, tripId).list();
        Set<UUID> joinedMemberIds = joinedMembers.stream().map(TripMembers::getUserId).collect(Collectors.toSet());
        // 过滤已经添加的用户。创建TripMember
        List<TripMembers> inviteMembers = inviteUsers.stream()
                // 筛选未添加的用户
                .filter(inviteUser -> !joinedMemberIds.contains(inviteUser.getUserId()))
                .map(inviteUser -> new TripMembers(
                        UUID.randomUUID(),
                        tripId,
                        inviteUser.getUserId(),
                        MemberRole.VIEWER,
                        true, // 邀请的成员直接通过
                        OffsetDateTime.now(ZoneId.systemDefault())
                )).toList();
        if (inviteMembers.isEmpty()) {
            log.info("All invite user already joined: {}", inviteUserIds);
            return true;
        }
        if (selfProxy.saveBatch(inviteMembers)) {
            log.info("Invite member success: {}", inviteMembers);
            return true;
        }
        log.error("Invite member failed: {}", inviteMembers);
        throw new RuntimeException(Constant.TRIP_MEMBER_INVITE_USER_FAILED);
    }

    @Override
    public boolean createOwner(UUID tripId, UUID userId) {
        log.info("Create owner: trip {}, owner {}", tripId, userId);
        TripMembers owner = new TripMembers(UUID.randomUUID(), tripId, userId, MemberRole.OWNER, true, OffsetDateTime.now(ZoneId.systemDefault()));
        return save(owner);
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER) // 只有OWNER才能处理成员请求
    public boolean handleMemberRequest(UUID tripId, UUID userId, UUID handleUserId, Boolean accept) {
        log.info("Handling member request: {}, handleUserId {}, currentUserId {}, accept {}", tripId, handleUserId, userId, accept);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(handleUserId, "handleUserId cannot be null");
        Assert.notNull(userId, "currentUserId cannot be null");
        Assert.notNull(accept, "accept cannot be null");
        // 查看member表中是否存在该请求
        TripMembers handleMember = lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, handleUserId).one();
        if (handleMember == null || handleMember.getIsPass()) {
            log.error("Member request not exist: {}", handleMember);
            throw new BusinessException(Constant.TRIP_MEMBER_REQUEST_NOT_EXIST);
        }
        // 处理请求，accept为true则通过，为false则删除
        boolean result;
        if (accept) {
            handleMember.setIsPass(true);
            result = updateById(handleMember);
        } else {
            // 拒绝请求，删除该成员请求
            result = removeById(handleMember);
        }
        if (result) {
            log.info("Handle member request success: accept?{}", accept);
        } else {
            log.error("Handle member request failed: accept?{}", accept);
        }
        return result;
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER) // 只有OWNER可以删除成员
    public boolean deleteMember(UUID tripId, UUID userId, UUID handleUserId) {
        log.info("Deleting member: tripId {}, handleUserId {}, userId {}", tripId, handleUserId, userId);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(handleUserId, "handleUserId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        // 查看该成员是否存在
        TripMembers member = lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, handleUserId).one();
        Assert.notNull(member, Constant.TRIP_MEMBER_NOT_EXIST);
        return removeById(member);
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER) // 仅允许OWNER删除
    public boolean deleteMembersByTripId(UUID tripId, UUID userId) {
        log.info("Delete all members of trip {} by user {}", tripId, userId);
        lambdaUpdate().eq(TripMembers::getTripId, tripId).remove();
        return true;
    }

    @Override
    @TripAccessValidate(lowestRole = MemberRole.OWNER)
    public boolean updateMemberRole(UUID tripId, UUID userId, UUID handleUserId, MemberRole newRole) {
        log.info("Updating member role: {}, handleUserId {}, currentUserId {}, newRole {}", tripId, handleUserId, userId, newRole);
        Assert.notNull(tripId, "tripId cannot be null");
        Assert.notNull(handleUserId, "handleUserId cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(newRole, "newRole cannot be null");
        // 查看该成员是否存在
        TripMembers member = lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, handleUserId).one();
        Assert.notNull(member, Constant.TRIP_MEMBER_NOT_EXIST);
        // 更新成员权限
        member.setRole(newRole);
        return updateById(member);
    }

    @Override
    @TripVisibilityValidate
    public List<TripMemberDto> getTripMembers(UUID userId, UUID tripId, @Nullable Boolean isPass) {
        log.info("Get members: {}, isPass {}", tripId, isPass);
        Assert.notNull(tripId, "tripId cannot be null");
        // 获取成员列表
        List<TripMembers> members = lambdaQuery().eq(TripMembers::getTripId, tripId).list();
        if (members == null || members.isEmpty()) {
            log.info("No members found: tripId {}, isPass {}", tripId, isPass);
            return Collections.emptyList();
        }
        // 根据isPass过滤用户，获取用户信息
        // 如果isPass为null则不过滤，否则根据isPass过滤
        return members.stream()
                // 如果isPass为null则不过滤，否则根据isPass过滤
                .filter(member -> isPass == null || isPass.equals(member.getIsPass()))
                .map(member -> {
                    TripMemberDto dto = BeanUtil.copyProperties(member, TripMemberDto.class);
                    Users user = usersService.getById(member.getUserId());
                    dto.setUserName(user.getUsername());
                    dto.setPreferencesText(user.getPreferencesText());
                    return dto;
                }).toList();
    }

    @Override
    public List<TripMembers> getJoinedTrips(UUID userId) {
        log.info("Get joined trips of user: {}", userId);
        Assert.notNull(userId, "userId cannot be null");
        // 获取该用户userId的所有旅程
        List<TripMembers> allTrips = lambdaQuery().eq(TripMembers::getUserId, userId).list();
        if (allTrips == null || allTrips.isEmpty()){
            log.info("No joined trips of user: {}", userId);
            return Collections.emptyList();
        }
        // 按照加入的时间倒叙排序
        return allTrips.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();
    }

    @Override
    public List<TripMemberPendingRequestDto> getPendingRequests(UUID userId) {
        log.info("Get pending requests of user: {}", userId);
        Assert.notNull(userId, "userId cannot be null");
        // 获取该用户创建的所有行程
        List<Trips> trips = tripsService.lambdaQuery().eq(Trips::getUserId, userId).list();
        if (trips == null || trips.isEmpty()){
            log.info("No trips of user: {}", userId);
            return Collections.emptyList();
        }
        // 获取所有行程的成员请求
        Map<UUID, Trips> tripIdToTrip = trips.stream().collect(Collectors.toMap(Trips::getTripId, trip -> trip));
        List<TripMemberPendingRequestDto> pendingRequestDtos = baseMapper.getPendingCountByTripIds(tripIdToTrip.keySet());
        pendingRequestDtos.forEach(dto -> {
            dto.setTitle(tripIdToTrip.get(dto.getTripId()).getTitle());
        });
        return pendingRequestDtos;
    }

}




