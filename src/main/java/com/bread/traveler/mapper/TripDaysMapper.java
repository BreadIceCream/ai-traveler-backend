package com.bread.traveler.mapper;

import com.bread.traveler.entity.TripDays;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_days】的数据库操作Mapper
* @createDate 2025-11-26 15:34:21
* @Entity com.bread.traveler.entity.TripDays
*/
@Mapper
public interface TripDaysMapper extends BaseMapper<TripDays> {

    /**
     * 更新日程的date
     * @param tripDayId
     * @param date
     * @return
     */
    @Update("update trip_days set day_date = #{date} where trip_day_id = #{tripDayId}")
    int updateDate(UUID tripDayId, LocalDate date);

    /**
     * 获取日程的date
     * @param tripDayId
     * @return
     */
    @Select("select day_date from trip_days where trip_day_id = #{tripDayId}")
    LocalDate getDateByTripDayId(UUID tripDayId);

}




