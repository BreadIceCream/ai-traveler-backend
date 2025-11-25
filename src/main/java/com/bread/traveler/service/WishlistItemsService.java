package com.bread.traveler.service;

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
     * 获取某个行程下所有心愿单Item
     * @param tripId
     * @return
     */
    List<WishlistItems> listByTripId(UUID tripId);

    /**
     * 删除心愿单中的item
     * @param itemIds
     * @return
     */
    boolean removeFromWishList(List<UUID> itemIds);

}
