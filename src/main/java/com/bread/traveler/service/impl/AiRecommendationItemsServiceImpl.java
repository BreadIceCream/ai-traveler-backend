package com.bread.traveler.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.bread.traveler.entity.AiRecommendationItems;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.AiRecommendationConversationService;
import com.bread.traveler.service.AiRecommendationItemsService;
import com.bread.traveler.mapper.AiRecommendationItemsMapper;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.service.PoisService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【ai_recommendation_items】的数据库操作Service实现
* @createDate 2025-11-17 22:29:15
*/
@Service
@Slf4j
public class AiRecommendationItemsServiceImpl extends ServiceImpl<AiRecommendationItemsMapper, AiRecommendationItems> implements AiRecommendationItemsService{

    @Autowired
    @Lazy
    private AiRecommendationConversationService conversationService;
    @Autowired
    private PoisService poisService;
    @Autowired
    private NonPoiItemService nonPoiItemService;
    @Autowired
    @Lazy
    private AiRecommendationItemsService selfProxy;

    @Override
    public List<Pois> getPoisItems(UUID userId, UUID conversationId, @Nullable Boolean isManual) {
        log.info("Get pois items from ITEM table: conversation {}, isManual {}", conversationId, isManual);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 获取会话推荐的pois
        // 先从item表中获取pois记录
        LambdaQueryChainWrapper<AiRecommendationItems> wrapper = lambdaQuery()
                .eq(AiRecommendationItems::getConversationId, conversationId)
                .eq(AiRecommendationItems::getIsPoi, true);
        if (isManual != null) {
            wrapper.eq(AiRecommendationItems::getIsManual, isManual).list();
        }
        List<AiRecommendationItems> poisItems = wrapper.list();
        List<UUID> poiIds = poisItems.stream()
                .sorted((o1, o2) -> {
                    // 按照添加的时间顺序降序排序，最新的添加的排在最前面
                    return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                }).map(AiRecommendationItems::getEntityId).toList();
        if (poiIds.isEmpty()){
            log.warn("Get pois items NO RESULTS in ITEM table: conversation {}", conversationId);
            return Collections.emptyList();
        }
        // 从pois表中获取pois实体，按照poiIds中id的顺序返回
        List<String> poiIdsStr = poiIds.stream().map(poiId -> "'" + poiId + "'").toList();
        List<Pois> result = poisService.lambdaQuery().in(Pois::getPoiId, poiIds)
                .last("order by (poi_id," + StrUtil.join(",", poiIdsStr) + ")").list();
        if (result == null || result.isEmpty()){
            log.warn("Get pois items NO RESULTS in POI table: conversation {}", conversationId);
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public List<NonPoiItem> getNonPoiItems(UUID userId, UUID conversationId, @Nullable Boolean isManual) {
        log.info("Get non poi items from ITEM table: conversation {}, isManual {}", conversationId, isManual);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 获取会话推荐的nonPoiItem
        LambdaQueryChainWrapper<AiRecommendationItems> wrapper = lambdaQuery()
                .eq(AiRecommendationItems::getConversationId, conversationId)
                .eq(AiRecommendationItems::getIsPoi, false);
        if (isManual != null) {
            wrapper.eq(AiRecommendationItems::getIsManual, isManual);
        }
        List<AiRecommendationItems> nonPoiItems = wrapper.list();
        List<UUID> nonPoiItemIds = nonPoiItems.stream()
                .sorted((o1, o2) -> {
                    // 按照添加的时间顺序降序排序，最新的添加的排在最前面
                    return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                }).map(AiRecommendationItems::getEntityId).toList();
        if (nonPoiItemIds.isEmpty()){
            log.warn("Get non poi items NO RESULTS in ITEM table: conversation {}", conversationId);
            return Collections.emptyList();
        }
        // 从nonPoiItem表中获取nonPoiItem实体，按照nonPoiItemIds中id的顺序返回
        List<String> nonPoiItemIdsStr = nonPoiItemIds.stream().map(nonPoiItemId -> "'" + nonPoiItemId + "'").toList();
        List<NonPoiItem> result = nonPoiItemService.lambdaQuery().in(NonPoiItem::getId, nonPoiItemIds)
                .last("order by (id," + StrUtil.join(",", nonPoiItemIdsStr) + ")").list();
        if (result == null || result.isEmpty()){
            log.warn("Get non poi items NO RESULTS in NON_POI_ITEM table: conversation {}", conversationId);
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public boolean addPois(UUID userId, UUID conversationId, List<UUID> poiIds, boolean isManual) {
        log.info("Add pois to ITEM table: conversation {}, isManual {}", conversationId, isManual);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 添加pois到item表中
        // 先从pois表中获取pois记录
        List<Pois> pois = poisService.listByIds(poiIds);
        if (pois.size() != poiIds.size()){
            log.warn("Some poi don't exist: {}", poiIds);
        }
        List<AiRecommendationItems> items = pois.stream().map(poi -> {
            // 创建item实体
            return new AiRecommendationItems(
                    poi.getPoiId(),
                    OffsetDateTime.now(ZoneId.systemDefault()),
                    conversationId,
                    isManual,
                    true
            );
        }).toList();
        if (!selfProxy.saveBatch(items)) {
            log.error("Add pois to ITEM table FAILED: conversation {}, pois {}", conversationId, poiIds);
            return false;
        }
        return true;
    }

    @Override
    public boolean addPoisDirect(UUID userId, UUID conversationId, List<UUID> poiIds) {
        log.info("Add pois to ITEM table: conversation {}, isManual false", conversationId);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 添加pois到item表中
        List<AiRecommendationItems> items = poiIds.stream().map(poiId -> {
            // 创建item实体
            return new AiRecommendationItems(
                    poiId,
                    OffsetDateTime.now(ZoneId.systemDefault()),
                    conversationId,
                    false,
                    true
            );
        }).toList();
        if (!selfProxy.saveBatch(items)) {
            log.error("Add pois to ITEM table FAILED: conversation {}, pois {}", conversationId, poiIds);
            return false;
        }
        return true;
    }

    @Override
    public boolean addNonPoiItems(UUID userId, UUID conversationId, List<UUID> nonPoiItemIds, boolean isManual) {
        log.info("Add non poi items to ITEM table: conversation {}, isManual {}", conversationId, isManual);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 添加nonPoiItem到item表中
        List<NonPoiItem> nonPoiItems = nonPoiItemService.listByIds(nonPoiItemIds);
        if (nonPoiItems.size() != nonPoiItemIds.size()){
            log.warn("Some non poi item don't exist: {}", nonPoiItemIds);
        }
        List<AiRecommendationItems> items = nonPoiItems.stream().map(nonPoiItem -> {
            // 创建item实体
            return new AiRecommendationItems(
                    nonPoiItem.getId(),
                    OffsetDateTime.now(ZoneId.systemDefault()),
                    conversationId,
                    isManual,
                    false
            );
        }).toList();
        if (!selfProxy.saveBatch(items)) {
            log.error("Add non poi items to ITEM table FAILED: conversation {}, nonPoiItems {}", conversationId, nonPoiItemIds);
            return false;
        }
        return true;
    }

    @Override
    public boolean removePoisFromItems(UUID userId, UUID conversationId, @Nullable List<UUID> poiIds) {
        log.info("Remove pois from ITEM table: conversation {}, pois {}", conversationId, poiIds);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 删除item表中的pois关联记录，不级联删除pois表
        LambdaUpdateChainWrapper<AiRecommendationItems> wrapper = lambdaUpdate()
                .eq(AiRecommendationItems::getConversationId, conversationId)
                .eq(AiRecommendationItems::getIsPoi, true);
        if (poiIds != null && !poiIds.isEmpty()) {
            wrapper.in(AiRecommendationItems::getEntityId, poiIds);
        }
        if (!wrapper.remove()) {
            log.error("Remove pois from ITEM table FAILED: conversation {}, pois {}", conversationId, poiIds);
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeNonPoiFromItems(UUID userId, UUID conversationId, @Nullable List<UUID> nonPoiItemIds) {
        log.info("Remove non poi items from ITEM table: conversation {}, nonPoiItems {}", conversationId, nonPoiItemIds);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 删除item表中的nonPoiItem关联记录
        LambdaUpdateChainWrapper<AiRecommendationItems> wrapper = lambdaUpdate()
                .eq(AiRecommendationItems::getConversationId, conversationId)
                .eq(AiRecommendationItems::getIsPoi, false);
        if (nonPoiItemIds != null && !nonPoiItemIds.isEmpty()) {
            // 删除该会话推荐的指定nonPoiItem
            wrapper.in(AiRecommendationItems::getEntityId, nonPoiItemIds);
        }
        if (!wrapper.remove()) {
            log.error("Remove non poi items from ITEM table FAILED: conversation {}, nonPoiItems {}", conversationId, nonPoiItemIds);
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAllItems(UUID userId, UUID conversationId) {
        log.info("Remove all items from ITEM table: conversation {}", conversationId);
        AiRecommendationConversation conversation = conversationService.searchConversationById(userId, conversationId);
        // 删除该会话推荐的所有pois，ids填null
        boolean a = removePoisFromItems(userId, conversationId, null);
        // 删除该会话推荐的所有nonPoiItem，ids填null
        boolean b = removeNonPoiFromItems(userId, conversationId, null);
        if (!a || !b){
            log.error("Remove all items from ITEM table FAILED: conversation {}", conversationId);
            return false;
        }
        return true;
    }
}




