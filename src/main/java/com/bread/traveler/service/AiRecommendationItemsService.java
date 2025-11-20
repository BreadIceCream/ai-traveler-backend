package com.bread.traveler.service;

import com.bread.traveler.entity.AiRecommendationItems;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.entity.Pois;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【ai_recommendation_items】的数据库操作Service
* @createDate 2025-11-17 22:29:15
*/
public interface AiRecommendationItemsService extends IService<AiRecommendationItems> {

    /**
     * 获取会话推荐的pois
     * @param userId
     * @param conversationId
     * @param version
     * @return
     */
    List<Pois> getPoisItems(UUID userId, UUID conversationId, Integer version);

    /**
     * 获取会话推荐的activities
     * @param userId
     * @param conversationId
     * @param version
     * @return
     */
    List<Activity> getActivitiesItems(UUID userId, UUID conversationId, Integer version);

    /**
     * 手动添加pois到会话推荐中，version为0
     * @param userId
     * @param conversationId
     * @param poiIds
     * @return
     */
    boolean addPoisToItems(UUID userId, UUID conversationId, List<UUID> poiIds);

    /**
     * 手动添加activities到会话推荐中，version为0
     * @param userId
     * @param conversationId
     * @param activityIds
     * @return
     */
    boolean addActivitiesToItems(UUID userId, UUID conversationId, List<UUID> activityIds);

    /**
     * 删除会话推荐中的pois，不级联删除pois
     * @param userId
     * @param conversationId
     * @param poiIds
     * @return
     */
    boolean removePoisFromItems(UUID userId, UUID conversationId, List<UUID> poiIds);

    /**
     * 删除会话推荐中的activities，级联删除activities
     *
     * @param userId
     * @param conversationId
     * @param activityIds
     * @return
     */
    boolean removeActivitiesFromItems(UUID userId, UUID conversationId, List<UUID> activityIds);
}