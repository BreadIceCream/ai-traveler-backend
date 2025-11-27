package com.bread.traveler.service;

import com.bread.traveler.dto.EntireWishlistItem;
import com.bread.traveler.entity.WishlistItems;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【wishlist_items】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface WishlistItemsService extends IService<WishlistItems> {

    /**
     * 添加到心愿单
     * @param tripId
     * @param entityId
     * @param isPoi
     */
    boolean addToWishList(UUID tripId, UUID entityId, boolean isPoi);

    /**
     * 删除心愿单中的item
     * @param itemIds
     * @return
     */
    boolean removeFromWishList(List<UUID> itemIds);

    /**
     * 获取某个行程下所有心愿单Item的详细信息，包含entity信息
     * @param tripId
     * @return List<EntireWishlistItem> 按照添加的顺序倒序排序
     */
    List<EntireWishlistItem> listEntireByTripId(UUID tripId);
}
