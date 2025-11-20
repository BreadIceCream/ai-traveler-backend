package com.bread.traveler.service;

import com.bread.traveler.entity.AiRecommendationItems;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.entity.NonPoiItem;
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
     * 获取会话推荐的nonPoi
     *
     * @param userId
     * @param conversationId
     * @param version
     * @return
     */
    List<NonPoiItem> getNonPoiItems(UUID userId, UUID conversationId, Integer version);

    /**
     * 添加pois到会话推荐Item中
     * @param userId
     * @param conversationId
     * @param poiIds
     * @param manual 是否手动添加。除了AI提取网页内容后添加为自动添加（即extractItemsFromWebPageAndSave方法），其他均为手动添加
     * @return
     */
    boolean addPois(UUID userId, UUID conversationId, List<UUID> poiIds, boolean manual);

    /**
     * 添加NonPoiItem到会话推荐Item中
     *
     * @param userId
     * @param conversationId
     * @param nonPoiItemIds
     * @param manual 是否手动添加
     * @return
     */
    boolean addNonPoiItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds, boolean manual);

    /**
     * 删除会话推荐中的pois，不级联删除pois
     * @param userId
     * @param conversationId
     * @param poiIds
     * @return
     */
    boolean removePoisFromItems(UUID userId, UUID conversationId, List<UUID> poiIds);

    /**
     * 删除会话推荐中的NonPoiItem，级联删除NonPoiItem
     * @param userId
     * @param conversationId
     * @param nonPoiItemIds
     * @return
     */
    boolean removeNonPoiFromItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds);
}