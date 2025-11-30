package com.bread.traveler.aspect;

import com.bread.traveler.annotation.TripAccessValidate;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class TripPermissionAspect {

    @Autowired
    private TripMembersService tripMembersService;
    @Autowired
    private TripsService tripsService;

    //todo 将检验的状态保存至redis

    /**
     * 访问权限校验，主要针对增删改方法。校验在当前方法下，user是否能访问trip：
     * 1.首先trip得存在。2.用户是成员。3.用户权限 >= 当前要求的最低权限
     * @param joinPoint
     * @param tripAccessValidate
     */
    @Before("@annotation(tripAccessValidate)")
    public void checkAccessPermission(JoinPoint joinPoint, TripAccessValidate tripAccessValidate){
        // 获取方法所需的最低权限
        MemberRole requiredRole = tripAccessValidate.lowestRole();

        Map<String, UUID> userIdAndTripId = getUserIdAndTripId(joinPoint);
        UUID tripId = userIdAndTripId.get("tripId");
        UUID userId = userIdAndTripId.get("userId");

        if (tripId == null || userId == null){
            log.error("Permission Check Failed: tripId {}, userId {}", tripId, userId);
            throw new BusinessException("缺少参数");
        }

        // 判断trip是否存在
        Trips trip = tripsService.getById(tripId);
        if (trip == null){
            log.error("Permission Check Failed: tripId {} not exist", tripId);
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }

        // 判断用户是否为成员
        TripMembers member = validateAndGetMember(tripId, userId);
        // 判断是否满足最低权限
        if (!member.getRole().hasPermission(requiredRole)){
            log.warn("Permission Denied: User {} has role {}, but needs {} for trip {}", userId, member.getRole(), requiredRole, tripId);
            throw new BusinessException(Constant.TRIP_MEMBER_NO_PERMISSION);
        }
        log.info("Permission Check Succeed: User {} has role {}, needs {} for trip {}", userId, member.getRole(), requiredRole, tripId);
    }

    /**
     * Trip可见性校验，主要针对“查”方法。校验trip对user是否可见：
     * 1.首先trip得存在。2.public trip对全部user可见. private trip只对通过的member可见
     * @param joinPoint
     * @param tripVisibilityValidate
     */
    @Before("@annotation(tripVisibilityValidate)")
    public void checkTripVisibility(JoinPoint joinPoint, TripVisibilityValidate tripVisibilityValidate){
        Map<String, UUID> userIdAndTripId = getUserIdAndTripId(joinPoint);
        UUID tripId = userIdAndTripId.get("tripId");
        UUID userId = userIdAndTripId.get("userId");

        if (tripId == null){
            log.error("Visibility Check Failed: tripId is null");
            throw new BusinessException("缺少tripId参数");
        }

        // 判断trip是否存在
        Trips trip = tripsService.getById(tripId);
        if (trip == null){
            throw new BusinessException(Constant.TRIP_NOT_EXIST);
        }
        // 判断是否公开
        if (trip.getIsPrivate()){
            // private trip只对成员可见
            if (userId == null){
                log.error("Visibility Check Failed: userId is null");
                throw new BusinessException("缺少userId参数");
            }
            // 验证是否为成员。不是成员，抛出异常
            TripMembers member = validateAndGetMember(tripId, userId);
        }
        log.info("Visit permit: tripId {}, userId {}, is private {}", tripId, userId, trip.getIsPrivate());
    }

    /**
     * 验证用户是否为成员（isPass=true，已通过的）
     * @param tripId
     * @param userId
     * @return TripMembers 成员信息
     * @throws BusinessException 不是成员，抛出异常
     */
    private TripMembers validateAndGetMember(UUID tripId, UUID userId){
        TripMembers member = tripMembersService.lambdaQuery().eq(TripMembers::getTripId, tripId).eq(TripMembers::getUserId, userId).one();
        if (member == null || !member.getIsPass()){
            log.warn("Member Validation Failed: tripId {}, userId {}", tripId, userId);
            throw new BusinessException(Constant.NOT_TRIP_MEMBER);
        }
        log.info("Member Validation Succeed: tripId {}, userId {}", tripId, userId);
        return member;
    }

    /**
     * 获取userid和tripId
     * @param joinPoint
     * @return Map<String, UUID>
     */
    private Map<String, UUID> getUserIdAndTripId(JoinPoint joinPoint){
        // 1.获取方法参数
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        // 2.尝试从参数中获取tripId和userId（当前用户id）
        // 约定被注解的方法，必须有名为tripId和userId的参数，且为UUID类型
        UUID tripId = null;
        UUID userId = null;
        HashMap<String, UUID> map = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            if ("tripId".equals(parameterNames[i]) && args[i] instanceof UUID){
                tripId = (UUID) args[i];
                map.put("tripId", tripId);
            }else if ("userId".equals(parameterNames[i]) && args[i] instanceof UUID){
                userId = (UUID) args[i];
                map.put("userId", userId);
            }
            if (tripId != null && userId != null){
                break;
            }
        }
        return map;
    }

}
