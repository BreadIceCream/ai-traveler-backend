package com.bread.traveler.aspect;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bread.traveler.annotation.TripRoleValidate;
import com.bread.traveler.annotation.TripVisibilityValidate;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.TripMembers;
import com.bread.traveler.entity.Trips;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.TripMembersService;
import com.bread.traveler.service.TripsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
@Slf4j
public class TripPermissionAspect {

    @Autowired
    private TripMembersService tripMembersService;
    @Autowired
    private TripsService tripsService;

    @Before("@annotation(tripRoleValidate)")
    public void checkModifyPermission(JoinPoint joinPoint, TripRoleValidate tripRoleValidate){
        // 获取方法所需的最低权限
        MemberRole requiredRole = tripRoleValidate.lowestRole();

        // 1.获取方法参数
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        // 2.尝试从参数中获取tripId和userId（当前用户id）
        // 约定被注解的方法，必须有名为tripId和userId的参数，且为UUID类型
        UUID tripId = null;
        UUID userId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("tripId".equals(parameterNames[i]) && args[i] instanceof UUID){
                tripId = (UUID) args[i];
            }else if ("userId".equals(parameterNames[i]) && args[i] instanceof UUID){
                userId = (UUID) args[i];
            }
            if (tripId != null && userId != null){
                break;
            }
        }
        if (tripId == null || userId == null){
            log.error("Permission Check Failed: tripId {}, userId {}", tripId, userId);
            throw new BusinessException("缺少参数");
        }
        // 3. 校验权限，获取用户角色
        TripMembers member = tripMembersService.lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, userId).one();
        if (member == null){
            // 用户不是成员
            throw new BusinessException(Constant.NOT_TRIP_MEMBER);
        }
        if (!member.getRole().hasPermission(requiredRole)){
            log.warn("Permission Denied: User {} has role {} but needs {} for trip {}", userId, member.getRole(), requiredRole, tripId);
            throw new BusinessException(Constant.TRIP_MEMBER_NO_PERMISSION);
        }
    }

    @Before("@annotation(tripVisibilityValidate)")
    public void checkTripVisibility(JoinPoint joinPoint, TripVisibilityValidate tripVisibilityValidate){
        // 1.获取方法参数
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        // 2.尝试从方法参数中获取tripId和userId（当前用户id）
        UUID tripId = null;
        UUID userId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("tripId".equals(parameterNames[i]) && args[i] instanceof UUID){
                tripId = (UUID) args[i];
            }else if ("userId".equals(parameterNames[i]) && args[i] instanceof UUID){
                userId = (UUID) args[i];
            }
            if (tripId != null && userId != null){
                break;
            }
        }
        if (tripId == null){
            log.error("Visibility Check Failed: tripId is null");
            throw new BusinessException("缺少tripId参数");
        }

        // 3.判断trip是否存在
        Trips trip = tripsService.getById(tripId);
        if (trip == null){
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }
        // 4.判断是否公开
        if (trip.getIsPrivate()){
            // 私密trip。判断用户是否为成员。是成员的话最低权限为VIEWER，可以查看
            if (userId == null){
                log.error("Visibility Check Failed: userId is null");
                throw new BusinessException("缺少userId参数");
            }
            TripMembers member = tripMembersService.lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, userId).one();
            if (member == null){
                // 用户不是成员
                log.warn("Visit denied: tripId {}, userId {}, is private {}", tripId, userId, trip.getIsPrivate());
                throw new BusinessException(Constant.NOT_TRIP_MEMBER);
            }
        }
        log.info("Visit permit: tripId {}, userId {}, is private {}", tripId, userId, trip.getIsPrivate());
    }

}
