package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.mapper.TripDayItemsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_day_items】的数据库操作Service实现
* @createDate 2025-11-25 11:45:06
*/
@Service
@Slf4j
public class TripDayItemsServiceImpl extends ServiceImpl<TripDayItemsMapper, TripDayItems> implements TripDayItemsService{

    @Override
    public boolean moveItemOrder(UUID currentId, UUID prevId, UUID nextId, UUID tripDayId) {
        log.info("Move item order: current {}, prev {}, next {}, NewTripDay {}", currentId, prevId, nextId, tripDayId);
        // 获取前一个item
        Double prevOrder = null;
        if (prevId != null){
            TripDayItems prev = lambdaQuery().eq(TripDayItems::getItemId, prevId).eq(TripDayItems::getTripDayId, tripDayId).one();
            prevOrder = prev.getItemOrder();
        }
        // 获取后一个item
        Double nextOrder = null;
        if (nextId != null){
            TripDayItems next = lambdaQuery().eq(TripDayItems::getItemId, nextId).eq(TripDayItems::getTripDayId, tripDayId).one();
            nextOrder = next.getItemOrder();
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




