package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        LambdaQueryChainWrapper<NonPoiItem> wrapper = lambdaQuery()
                .eq(NonPoiItem::getPrivateUserId, userId)
                .in(NonPoiItem::getId, nonPoiItemIds);
        if (!remove(wrapper)) {
            log.error("Delete non-poi items failed: user {}, items {}", userId, nonPoiItemIds);
            return false;
        }
        log.info("Delete non-poi items success: user {}, items {}", userId, nonPoiItemIds);
        return true;
    }
}




