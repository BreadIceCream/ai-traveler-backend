package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.dto.TripDayItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.mapper.TripDayItemsMapper;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_day_items】的数据库操作Service实现
* @createDate 2025-11-25 11:45:06
*/
@Service
@Slf4j
public class TripDayItemsServiceImpl extends ServiceImpl<TripDayItemsMapper, TripDayItems> implements TripDayItemsService{

    @Autowired
    @Qualifier("routePlanClient")
    private ObjectProvider<ChatClient> routePlanClientProvider;
    @Autowired
    private PoisService poisService;
    @Autowired
    private NonPoiItemService nonPoiItemService;

    @Override
    public TripDayItems addItems(UUID tripDayId, UUID entityId, boolean isPoi, TripDayItemDto dto) {
        log.info("Add item to trip day: tripDayId {}, entityId {}, isPoi {}, dto {}",
                tripDayId, entityId, isPoi, dto);
        Assert.notNull(tripDayId, "tripDayId cannot be null");
        Assert.notNull(entityId, "entityId cannot be null");
        // 检查entity是否存在
        boolean exists = isPoi ?
                poisService.lambdaQuery().eq(Pois::getPoiId, entityId).exists() :
                nonPoiItemService.lambdaQuery().eq(NonPoiItem::getId, entityId).exists();
        Assert.isTrue(exists, "entity not exists");
        // 检查item是否已经添加到当前日程，避免重复添加（不同日程允许重复添加）
        boolean itemExists = lambdaQuery().eq(TripDayItems::getTripDayId, tripDayId).eq(TripDayItems::getEntityId, entityId).eq(TripDayItems::getIsPoi, isPoi).exists();
        Assert.isTrue(!itemExists, Constant.TRIP_DAY_ITEM_EXISTS);
        // 创建item
        TripDayItems item = BeanUtil.copyProperties(dto, TripDayItems.class, "itemId");
        item.setItemId(UUID.randomUUID());
        item.setTripDayId(tripDayId);
        item.setEntityId(entityId);
        item.setIsPoi(isPoi);
        // 设置order，默认添加到最后一个
        // 获取最大的order
        Double maxOrder = baseMapper.getMaxOrder(tripDayId);
        double newOrder = getNewOrder(maxOrder, null);
        item.setItemOrder(newOrder);
        if (!save(item)) {
            log.error("Save trip day item failed: {}", item);
            return null;
        }
        log.info("Save trip day item success: {}", item);
        return item;
    }

    @Override
    public boolean deleteItems(List<UUID> itemIds) {
        log.info("Delete items of TRIP_DAY table: {}", itemIds);
        Assert.notNull(itemIds, "itemIds cannot be null");
        Assert.notEmpty(itemIds, "itemIds cannot be empty");
        lambdaUpdate().in(TripDayItems::getItemId, itemIds).remove();
        log.info("Delete items of TRIP_DAY table success: {}", itemIds);
        return true;
    }

    @Override
    public TripDayItems updateItemInfo(TripDayItemDto dto) {
        log.info("Update item info: {}", dto);
        Assert.notNull(dto, "dto cannot be null");
        Assert.notNull(dto.getItemId(), "itemId cannot be null");
        TripDayItems item = getById(dto.getItemId());
        if (item == null){
            log.error("Item not found: {}", dto.getItemId());
            throw new BusinessException("Item not found");
        }
        BeanUtil.copyProperties(dto, item, CopyOptions.create().setIgnoreProperties("itemId"));
        if (updateById(item)) {
            log.info("Update item info success: {}", dto);
            return item;
        }
        log.error("Update item info failed: {}", dto);
        throw new RuntimeException("Update item info failed");
    }

    @Override
    public boolean moveItemOrder(UUID currentId, UUID prevId, UUID nextId, UUID tripDayId) {
        log.info("Move item order: current {}, prev {}, next {}, NewTripDay {}", currentId, prevId, nextId, tripDayId);
        // 获取前一个item
        Double prevOrder = null;
        if (prevId != null){
            TripDayItems prev = lambdaQuery().eq(TripDayItems::getItemId, prevId).eq(TripDayItems::getTripDayId, tripDayId).one();
            prevOrder = prev != null ? prev.getItemOrder() : null;
        }
        // 获取后一个item
        Double nextOrder = null;
        if (nextId != null){
            TripDayItems next = lambdaQuery().eq(TripDayItems::getItemId, nextId).eq(TripDayItems::getTripDayId, tripDayId).one();
            nextOrder = next != null ? next.getItemOrder() : null;
        }
        // 计算新的order
        double newOrder = getNewOrder(prevOrder, nextOrder);
        // 更新item的order
        boolean update = lambdaUpdate()
                .eq(TripDayItems::getItemId, currentId)
                .set(TripDayItems::getItemOrder, newOrder)
                .set(TripDayItems::getTripDayId, tripDayId)
                .update();
        if(update){
            log.info("Move item order success: {}", currentId);
            return true;
        }
        log.info("Move item order failed: {}", currentId);
        return false;
    }

    @Override //todo 优化性能，提高查询效率
    public TripDayItems updateTransportNote(UUID itemId, @Nullable String originAddress) {
        log.info("Update transport note: {}", itemId);
        TripDayItems current = getById(itemId);
        Assert.notNull(current, Constant.DESTINATION_ITEM_NOT_FOUND);
        // 获取当前地点
        String destination = getAddressInfo(current);
        // 获取上一个地点
        if (originAddress == null || originAddress.trim().isEmpty()){
            // 从数据库中获取上一个地点
            // 这里不更改为getEntireItemsByTripDayId方法，因为getEntireItemsByTripDayId方法会查询所有entity信息
            // 而这里只筛选出1个然后查poi或nonPoi表
            List<TripDayItems> otherItems = getItemsByTripDayId(current.getTripDayId());
            TripDayItems prev = otherItems.stream()
                    // 筛选出顺序小于当前item的
                    .filter(item -> item.getItemOrder() < current.getItemOrder())
                    // 获取最大顺序的
                    .max(Comparator.comparing(TripDayItems::getItemOrder))
                    .orElse(null);
            if (prev == null){
                log.warn("Update transport note: Prev item not found: {}", itemId);
                // 设置transportNotes返回，但不更新到数据库
                current.setTransportNotes(Constant.PREV_ITEM_NOT_FOUND);
                return current;
            }
            // 获取prev的entityId和类型，到pois或nonPois中获取详细信息
            originAddress = getAddressInfo(prev);
        }
        log.info("Original address: {}", originAddress);
        // 调用client智能规划路径
        ChatClient client = routePlanClientProvider.getObject();
        String prompt = "Origin address:" + originAddress + System.lineSeparator() + "Destination:" + destination;
        String transportNotes = client.prompt(prompt).call().content();
        current.setTransportNotes(transportNotes);
        Thread.startVirtualThread(()->{
            log.info("Update item transport notes: {}", itemId);
            if (updateById(current)) {
                log.info("Update item transport notes success: {}", itemId);
            }else {
                log.error("Update item transport notes failed: {}", itemId);
            }
        });
        return current;
    }

    @Override
    public List<TripDayItems> getItemsByTripDayId(UUID tripDayId) {
        log.info("Get items by trip day id: {}", tripDayId);
        List<TripDayItems> result = lambdaQuery().eq(TripDayItems::getTripDayId, tripDayId).list();
        // 按照itemOrder升序排序
        result.sort(Comparator.comparing(TripDayItems::getItemOrder));
        return result;
    }

    @Override
    public List<EntireTripDayItem> getEntireItemsByTripDayId(UUID tripDayId) {
        log.info("Get entire items by trip day id: {}", tripDayId);
        List<TripDayItems> items = lambdaQuery().eq(TripDayItems::getTripDayId, tripDayId).list();
        // 按照itemOrder升序排序，到pois或nonPois中获取详细信息
        return items.stream()
                .sorted(Comparator.comparing(TripDayItems::getItemOrder))
                .map(item -> {
                    if (item.getIsPoi()) {
                        Pois pois = poisService.getById(item.getEntityId());
                        return new EntireTripDayItem(item, pois);
                    } else {
                        NonPoiItem nonPoiItem = nonPoiItemService.getById(item.getEntityId());
                        return new EntireTripDayItem(item, nonPoiItem);
                    }
                }).toList();
    }

    private String getAddressInfo(TripDayItems item){
        if (item.getIsPoi()){
            Pois pois = poisService.getPoiById(item.getEntityId());
            // 转为JSON格式。去掉 unnecessary字段，提高查询效率
            PoisTransportInfo poisTransportInfo = BeanUtil.copyProperties(pois, PoisTransportInfo.class);
            return JSONUtil.toJsonStr(poisTransportInfo, JSONConfig.create().setIgnoreNullValue(true));
        }else {
            NonPoiItem nonPoiItem = nonPoiItemService.getById(item.getEntityId());
            Assert.notNull(nonPoiItem, Constant.NON_POI_ITEM_NOT_FOUND);
            // 转为JSON格式，去掉 unnecessary字段，提高查询效率
            NonPoiTransportInfo nonPoiTransportInfo = BeanUtil.copyProperties(nonPoiItem, NonPoiTransportInfo.class);
            return JSONUtil.toJsonStr(nonPoiTransportInfo, JSONConfig.create().setIgnoreNullValue(true));
        }
    }

    private static double getNewOrder(Double prevOrder, Double nextOrder) {
        double newOrder;
        if (prevOrder == null && nextOrder == null){
            // 当前day没有其他item，该item是第一个。10000为基准值
            newOrder = 10000.0;
        }else if (prevOrder == null){
            // prev 为 null，当前item移动到最前面，减一个基准步长
            newOrder = nextOrder - 10000.0;
        }else if (nextOrder == null){
            // next 为 null，当前item移动到最后面，加上一个基准步长
            newOrder = prevOrder + 10000.0;
        }else {
            // 此时移动到了中间
            newOrder = (prevOrder + nextOrder) / 2;
        }
        return newOrder;
    }
}




