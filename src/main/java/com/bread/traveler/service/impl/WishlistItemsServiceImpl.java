package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.EntireWishlistItem;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.entity.WishlistItems;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.service.WishlistItemsService;
import com.bread.traveler.mapper.WishlistItemsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【wishlist_items】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistItemsServiceImpl extends ServiceImpl<WishlistItemsMapper, WishlistItems> implements WishlistItemsService{

    private final PoisService poisService;
    private final NonPoiItemService nonPoiItemService;

    @Override
    public boolean addToWishList(UUID tripId, UUID entityId, boolean isPoi) {
        log.info("Add item to wishlist: tripId {}, entityId {}, isPoi {}", tripId, entityId, isPoi);
        Assert.notNull(tripId, "TripId cannot be null");
        Assert.notNull(entityId, "EntityId cannot be null");
        // 查看entity是否存在
        boolean entityExists = isPoi ?
                poisService.lambdaQuery().eq(Pois::getPoiId, entityId).exists() :
                nonPoiItemService.lambdaQuery().eq(NonPoiItem::getId, entityId).exists();
        Assert.isTrue(entityExists, "entity not exists");
        // 查看wishlist中是否已经添加
        boolean itemExists = lambdaQuery().eq(WishlistItems::getTripId, tripId).eq(WishlistItems::getEntityId, entityId).exists();
        Assert.isTrue(!itemExists, Constant.WISHLIST_ITEM_EXISTS);
        WishlistItems item = new WishlistItems();
        item.setId(UUID.randomUUID());
        item.setEntityId(entityId);
        item.setTripId(tripId);
        item.setIsPoi(isPoi);
        item.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        if (save(item)) {
            log.info("Add item to wishlist success: {}", item.getId());
            return true;
        }
        log.info("Add item to wishlist failed: {}", item.getId());
        return false;
    }

    @Override
    public List<EntireWishlistItem> listEntireByTripId(UUID tripId) {
        log.info("List entire wishlist items by tripId: {}", tripId);
        List<WishlistItems> wishlistItems = lambdaQuery().eq(WishlistItems::getTripId, tripId).list();
        // 按照添加时间倒序排序，同时获取对应的entity信息
        return wishlistItems.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(wishlistItem -> {
                    if (wishlistItem.getIsPoi()) {
                        Pois pois = poisService.getById(wishlistItem.getEntityId());
                        return new EntireWishlistItem(wishlistItem, pois);
                    } else {
                        NonPoiItem nonPoiItem = nonPoiItemService.getById(wishlistItem.getEntityId());
                        return new EntireWishlistItem(wishlistItem, nonPoiItem);
                    }
                }).toList();
    }

    @Override
    public boolean removeFromWishList(List<UUID> itemIds) {
        log.info("Remove items from wishlist: {}", itemIds);
        Assert.notNull(itemIds, "itemIds cannot be null");
        Assert.notEmpty(itemIds, "itemIds cannot be empty");
        boolean remove = lambdaUpdate().in(WishlistItems::getId, itemIds).remove();
        Assert.isTrue(remove, Constant.WISHLIST_ITEM_NOT_EXISTS);
        log.info("Remove items from wishlist success: {}", itemIds);
        return true;
    }
}




