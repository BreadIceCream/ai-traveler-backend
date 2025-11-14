package com.bread.traveler.mapper;

import com.bread.traveler.entity.Trips;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author huang
* @description 针对表【trips】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.Trips
*/
@Mapper
public interface TripsMapper extends BaseMapper<Trips> {

}




