package com.bread.traveler.mapper;

import com.bread.traveler.entity.NonPoiItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bread.traveler.enums.NonPoiType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【non_poi_item】的数据库操作Mapper
* @createDate 2025-11-19 14:56:55
* @Entity com.bread.traveler.entity.NonPoiItem
*/
@Mapper
public interface NonPoiItemMapper extends BaseMapper<NonPoiItem> {

    /**
     * 统计用户指定类型的非POI数量
     * @param userId
     * @param type 类型的name
     * @return
     */
    long countNonPois(UUID userId, String type);

    /**
     * 分页查询用户指定类型的非POI
     * @param userId
     * @param type 类型的name
     * @param offset
     * @param pageSize
     * @return
     */
    List<NonPoiItem> getPage(UUID userId, String type, long offset, Integer pageSize);
}




