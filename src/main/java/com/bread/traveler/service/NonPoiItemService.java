package com.bread.traveler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.NonPoiType;

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
     * @param dto
     * @return
     */
    NonPoiItem updateNonPoiItem(UUID userId, NonPoiItemDto dto);

    /**
     * 根据用户ID分页获取
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    Page<NonPoiItem> getPageByUserId(UUID userId, Integer pageNum, Integer pageSize, NonPoiType type);
}
