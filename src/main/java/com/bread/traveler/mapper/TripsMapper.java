package com.bread.traveler.mapper;

import com.bread.traveler.entity.Trips;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trips】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.Trips
*/
@Mapper
public interface TripsMapper extends BaseMapper<Trips> {

    /**
     * 修改旅程状态
     * @param userId
     * @param tripId
     * @param newStatus
     * @return
     */
    @Update("update trips set status = #{newStatus}::trip_status where user_id = #{userId} and trip_id = #{tripId}")
    int changeStatus(UUID userId, UUID tripId, String newStatus);

    /**
     * 统计符合条件的旅程数量
     * @param isPrivate
     * @param status
     * @param city
     * @param startDate
     * @param endDate
     * @return
     */
    long countTrips(Boolean isPrivate, String status, String city, LocalDate startDate, LocalDate endDate);

    /**
     * 分页查询符合条件的旅程
     * 注意：自定义xml SQL不会生效 MyBatis-Plus 的注解配置 (@TableField)，指定的TypeHandler会失效。需要自行创建<resultMap></resultMap>
     *
     * @param isPrivate
     * @param status
     * @param city
     * @param startDate
     * @param endDate
     * @param offset
     * @param limit
     * @return
     */
    List<Trips> selectTripsPage(Boolean isPrivate, String status, String city, LocalDate startDate, LocalDate endDate, long offset, long limit);
}




