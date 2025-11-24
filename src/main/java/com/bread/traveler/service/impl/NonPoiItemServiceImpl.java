package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.WebPage;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.mapper.NonPoiItemMapper;
import com.bread.traveler.service.WebSearchService;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        NonPoiItem nonPoiItem = BeanUtil.copyProperties(dto, NonPoiItem.class);
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
    public boolean updateNonPoiItem(UUID userId, NonPoiItem nonPoiItem) {
        // 忽略id，createdAt，privateUserId这三个属性
        NonPoiItem updateEntity = BeanUtil.copyProperties(nonPoiItem, NonPoiItem.class, "id", "createdAt", "privateUserId");
        boolean update = lambdaUpdate()
                .eq(NonPoiItem::getId, nonPoiItem.getId())
                .eq(NonPoiItem::getPrivateUserId, userId)
                .update(updateEntity);
        if (!update) {
            log.info("Update non-poi item failed: {}", nonPoiItem);
            return false;
        }
        log.info("Update non-poi item success: {}", nonPoiItem);
        return true;
    }
}




