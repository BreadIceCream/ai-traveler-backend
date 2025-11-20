package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.AiRecommendationItems;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.AiRecommendationItemsService;
import com.bread.traveler.mapper.AiRecommendationItemsMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【ai_recommendation_items】的数据库操作Service实现
* @createDate 2025-11-17 22:29:15
*/
@Service
public class AiRecommendationItemsServiceImpl extends ServiceImpl<AiRecommendationItemsMapper, AiRecommendationItems> implements AiRecommendationItemsService{

    @Override
    public List<Pois> getPoisItems(UUID userId, UUID conversationId, Integer version) {
        return List.of();
    }

    @Override
    public List<NonPoiItem> getNonPoiItems(UUID userId, UUID conversationId, Integer version) {
        return List.of();
    }

    @Override
    public boolean addPois(UUID userId, UUID conversationId, List<UUID> poiIds, boolean manual) {
        return false;
    }

    @Override
    public boolean addNonPoiItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds, boolean manual) {
        return false;
    }

    @Override
    public boolean removePoisFromItems(UUID userId, UUID conversationId, List<UUID> poiIds) {
        return false;
    }

    @Override
    public boolean removeNonPoiFromItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds) {
        return false;
    }
}




