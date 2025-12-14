package com.bread.traveler.context;

import com.bread.traveler.enums.MemberRole;

import java.util.*;

public class TripPermissionContext {

    public static final ThreadLocal<Map<UUID, MemberRole>> VALIDATED_TRIPS_ROLES = ThreadLocal.withInitial(HashMap::new);
    public static final ThreadLocal<Set<UUID>> VISIBLE_TRIPS = ThreadLocal.withInitial(HashSet::new);

    /**
     * 记录已通过校验的旅程和角色
     * @param tripId
     * @param memberRole
     */
    public static void addValidatedTripRole(UUID tripId, MemberRole memberRole){
        VALIDATED_TRIPS_ROLES.get().put(tripId, memberRole);
    }

    /**
     * 记录可视的旅程
     * @param tripId
     */
    public static void addVisibleTrip(UUID tripId){
        VISIBLE_TRIPS.get().add(tripId);
    }

    /**
     * 获取已通过校验的旅程角色
     * @param tripId
     * @return
     */
    public static MemberRole getValidatedRole(UUID tripId){
        return VALIDATED_TRIPS_ROLES.get().get(tripId);
    }

    /**
     * 判断旅程是否可视
     * @param tripId
     * @return
     */
    public static boolean tripIsVisible(UUID tripId){
        return VISIBLE_TRIPS.get().contains(tripId);
    }

    /**
     * 清理上下文 (务必在请求结束时调用)
     */
    public static void clear() {
        VALIDATED_TRIPS_ROLES.remove();
        VISIBLE_TRIPS.remove();
    }

}
