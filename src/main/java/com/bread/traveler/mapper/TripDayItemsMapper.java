package com.bread.traveler.mapper;

import com.bread.traveler.entity.TripDayItems;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_day_items】的数据库操作Mapper
* @createDate 2025-11-25 11:45:06
* @Entity com.bread.traveler.entity.TripDayItems
*/
@Mapper
public interface TripDayItemsMapper extends BaseMapper<TripDayItems> {

    /**
     * 获取最大order
     * @param tripDayId
     * @return
     */
    @Select("select max(item_order) from trip_day_items where trip_day_id = #{tripDayId}")
    Double getMaxOrder(UUID tripDayId);
}




