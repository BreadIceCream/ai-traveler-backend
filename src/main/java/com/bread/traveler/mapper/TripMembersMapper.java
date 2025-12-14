package com.bread.traveler.mapper;

import com.bread.traveler.dto.TripMemberPendingRequestDto;
import com.bread.traveler.entity.TripMembers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
* @author huang
* @description 针对表【trip_members】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.TripMembers
*/
@Mapper
public interface TripMembersMapper extends BaseMapper<TripMembers> {

    /**
     * 获取待处理的申请数量
     * @param tripIds
     * @return
     */
    List<TripMemberPendingRequestDto> getPendingCountByTripIds(@Param("tripIds") Set<UUID> tripIds);
}




