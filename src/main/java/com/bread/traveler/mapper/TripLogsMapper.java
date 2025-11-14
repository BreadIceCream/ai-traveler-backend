package com.bread.traveler.mapper;

import com.bread.traveler.entity.TripLogs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author huang
* @description 针对表【trip_logs】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.TripLogs
*/
@Mapper
public interface TripLogsMapper extends BaseMapper<TripLogs> {

}




