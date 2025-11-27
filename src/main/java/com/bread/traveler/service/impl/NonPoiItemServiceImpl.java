package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.mapper.NonPoiItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【non_poi_item】的数据库操作Service实现
* @createDate 2025-11-19 14:56:55
*/
@Service
@Slf4j
public class NonPoiItemServiceImpl extends ServiceImpl<NonPoiItemMapper, NonPoiItem> implements NonPoiItemService{

    @Override
    public boolean deleteByIds(UUID userId, List<UUID> nonPoiItemIds) {
        LambdaUpdateChainWrapper<NonPoiItem> wrapper = lambdaUpdate()
                .eq(NonPoiItem::getPrivateUserId, userId)
                .in(NonPoiItem::getId, nonPoiItemIds);
        if (!wrapper.remove()) {
            log.error("Delete non-poi items failed: user {}, items {}", userId, nonPoiItemIds);
            return false;
        }
        log.info("Delete non-poi items success: user {}, items {}", userId, nonPoiItemIds);
        return true;
    }

    @Override
    public NonPoiItem createNonPoiItem(UUID userId, NonPoiItemDto dto) {
        NonPoiItem nonPoiItem = BeanUtil.copyProperties(dto, NonPoiItem.class, "id");
        nonPoiItem.setId(UUID.randomUUID());
        nonPoiItem.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        nonPoiItem.setPrivateUserId(userId);
        if (!save(nonPoiItem)) {
            log.info("Create non-poi item failed: {}", nonPoiItem);
            return null;
        }
        log.info("Create non-poi item success: {}", nonPoiItem);
        return nonPoiItem;
    }

    @Override
    public NonPoiItem updateNonPoiItem(UUID userId, NonPoiItemDto dto) {
        log.info("Update non-poi item: {}", dto);
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(dto.getId(), "id cannot be null");
        NonPoiItem item = lambdaQuery().eq(NonPoiItem::getId, dto.getId()).eq(NonPoiItem::getPrivateUserId, userId).one();
        Assert.notNull(item, "Non-poi item not found: {}", dto.getId());
        // 更新item，忽略id，createdAt，privateUserId这三个属性
        BeanUtil.copyProperties(dto, item, "id", "createdAt", "privateUserId");
        if (updateById(item)) {
            log.info("Update non-poi item success: {}", dto);
            return item;
        }
        log.error("Update non-poi item failed: {}", dto);
        return null;
    }

    @Override
    public List<NonPoiItem> getByUserId(UUID userId) {
        log.info("Get non-poi items by userId: {}", userId);
        List<NonPoiItem> list = lambdaQuery().eq(NonPoiItem::getPrivateUserId, userId).list();
        return list != null ? list : Collections.emptyList();
    }
}




