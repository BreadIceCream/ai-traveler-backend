package com.bread.traveler.service;

import com.bread.traveler.dto.EntireTrip;
import com.bread.traveler.dto.TripDto;
import com.bread.traveler.entity.Trips;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trips】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripsService extends IService<Trips> {

    /**
     * 创建行程
     * @param dto
     * @return
     */
    Trips createTrip(UUID userId, TripDto dto);

    /**
     * 更新行程信息
     * @param userId
     * @param dto
     * @return
     */
    Trips updateTripInfo(UUID userId, TripDto dto);

    /**
     * 获取用户所有行程
     * @param userId
     * @return
     */
    List<Trips> getAllTripsOfUser(UUID userId);

    /**
     * 获取用户指定行程的全部信息，包括每个日程的详情
     * @param userId
     * @param tripId
     * @return
     */
    EntireTrip getEntireTrip(UUID userId, UUID tripId);

    /**
     * AI智能规划，生成整个行程
     * @param userId
     * @param tripId
     * @return
     */
    EntireTrip aiGenerateEntireTrip(UUID userId, UUID tripId);

    /**
     * 删除用户指定行程，级联删除该行程下的所有日程和日程下的所有item
     * @param userId
     * @param tripId
     * @return
     */
    boolean deleteTrip(UUID userId, UUID tripId);

}
