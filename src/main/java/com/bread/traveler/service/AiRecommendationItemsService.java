package com.bread.traveler.service;

import com.bread.traveler.entity.AiRecommendationItems;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import jakarta.annotation.Nullable;

import javax.validation.constraints.Null;
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
     * @param isManual 是否为手动添加，如果为null返回所有
     * @return
     */
    List<Pois> getPoisItems(UUID userId, UUID conversationId, @Nullable Boolean isManual);

    /**
     * 获取会话推荐的nonPoi
     *
     * @param userId
     * @param conversationId
     * @param isManual 是否为手动添加，如果为null返回所有
     * @return
     */
    List<NonPoiItem> getNonPoiItems(UUID userId, UUID conversationId,@Nullable Boolean isManual);

    /**
     * 添加pois到会话推荐Item中
     * @param userId
     * @param conversationId
     * @param poiIds
     * @param isManual 是否手动添加。除了AI提取网页内容后添加为自动添加（即extractItemsFromWebPageAndSave方法），其他均为手动添加
     * @return
     */
    boolean addPois(UUID userId, UUID conversationId, List<UUID> poiIds, boolean isManual);

    /**
     * 添加NonPoiItem到会话推荐Item中
     *
     * @param userId
     * @param conversationId
     * @param nonPoiItemIds
     * @param isManual 是否手动添加
     * @return
     */
    boolean addNonPoiItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds, boolean isManual);

    /**
     * 删除会话推荐中的pois，不级联删除pois
     * @param userId
     * @param conversationId
     * @param poiIds 要删除的poisId，如果为空，则删除该会话推荐中的所有
     * @return
     */
    boolean removePoisFromItems(UUID userId, UUID conversationId, @Nullable List<UUID> poiIds);

    /**
     * 删除会话推荐中的NonPoiItem，级联删除NonPoiItem
     * @param userId
     * @param conversationId
     * @param nonPoiItemIds 要删除的NonPoiItemId，如果为空，则删除该会话推荐中的所有
     * @return
     */
    boolean removeNonPoiFromItems(UUID userId, UUID conversationId, @Nullable List<UUID> nonPoiItemIds);

    /**
     * 删除会话推荐中的所有Item
     * @param userId
     * @param conversationId
     * @return
     */
    boolean removeAllItems(UUID userId, UUID conversationId);
}