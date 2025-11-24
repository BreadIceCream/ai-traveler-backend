package com.bread.traveler.service;

import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【non_poi_item】的数据库操作Service
* @createDate 2025-11-19 14:56:55
*/
public interface NonPoiItemService extends IService<NonPoiItem> {

    /**
     * 批量删除
     * @param userId
     * @param nonPoiItemIds
     * @return
     */
    boolean deleteByIds(UUID userId, List<UUID> nonPoiItemIds);

    /**
     * 创建
     * @param userId
     * @param dto
     * @return
     */
    NonPoiItem createNonPoiItem(UUID userId, NonPoiItemDto dto);

    /**
     * 修改
     * @param userId
     * @param nonPoiItem
     * @return
     */
    boolean updateNonPoiItem(UUID userId, NonPoiItem nonPoiItem);
}
