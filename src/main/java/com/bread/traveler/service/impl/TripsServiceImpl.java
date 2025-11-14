package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.Trips;
import com.bread.traveler.service.TripsService;
import com.bread.traveler.mapper.TripsMapper;
import org.springframework.stereotype.Service;

/**
* @author huang
* @description 针对表【trips】的数据库操作Service实现
* @createDate 2025-11-14 12:09:43
*/
@Service
public class TripsServiceImpl extends ServiceImpl<TripsMapper, Trips>
    implements TripsService{

}




