package com.bread.traveler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bread.traveler.dto.EntireTrip;
import com.bread.traveler.dto.TripDto;
import com.bread.traveler.dto.TripWithMemberInfoDto;
import com.bread.traveler.entity.Trips;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.TripStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trips】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface TripsService extends IService<Trips> {

    @Data
    class AiPlanTrip {
        private List<TripDaysService.AiPlanTripDay> tripDays;
    }

    /**
     * 创建旅程
     * 可见性默认设置为private，状态默认设置为PLANNING
     * @param dto
     * @return
     */
    Trips createTrip(UUID userId, TripDto dto);

    /**
     * 更新旅程信息
     * @param userId
     * @param tripId
     * @param dto
     * @return
     */
    Trips updateTripInfo(UUID userId, UUID tripId, TripDto dto);

    /**
     * 更新旅程可见性。仅允许OWNER修改
     * @param userId 用户id
     * @param tripId 旅程id
     * @param isPrivate 是否私密
     * @return
     */
    boolean changeVisibility(UUID userId, UUID tripId, Boolean isPrivate);

    /**
     * 更新旅程状态。仅允许OWNER修改
     * @param userId 用户id
     * @param tripId 旅程id
     * @param newStatus 新状态
     * @return
     */
    boolean changeStatus(UUID userId, UUID tripId, TripStatus newStatus);

    /**
     * 获取用户的所有旅程，包括申请但未通过的isPass=false
     * @param userId
     * @return 旅程列表，包含该用户的成员信息。如果没有，则返回空集合
     */
    List<TripWithMemberInfoDto> getAllTripsOfUser(UUID userId);

    /**
     * 获取旅程的全部信息，包括每个日程的详情
     * @param userId 当前用户ID
     * @param tripId 旅程ID
     * @return
     */
    EntireTrip getEntireTrip(UUID userId, UUID tripId);

    /**
     * todo 添加语义搜索功能
     * 分页获取公开的旅程。支持通过目的地、时间范围筛选
     * 只能获取公开的、PLANNING的旅程
     * @param destinationCity
     * @param startDate
     * @param endDate
     * @return 分页结果，按照旅程创建时间倒序排序
     */
    Page<Trips> getPublicTrips(String destinationCity, LocalDate startDate, LocalDate endDate, Integer pageNum, Integer pageSize);

    /**
     * AI智能规划，根据wishlist心愿单生成整个旅程。新生成的旅程会覆盖原旅程
     * 只考虑有地址的item，没有地址的item会被忽略不会被添加到旅程中，用户需要自行添加（保留在心愿单中）
     * @param userId
     * @param tripId
     * @return
     */
    EntireTrip aiGenerateEntireTripPlan(UUID userId, UUID tripId);

    /**
     * 删除用户指定旅程，级联删除该旅程下的所有日程和日程下的所有item，以及旅程成员信息
     * 只有OWNER可以删除
     * @param userId
     * @param tripId
     * @return
     */
    boolean deleteTrip(UUID userId, UUID tripId);

}
