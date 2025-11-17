package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.entity.Activity;
import com.bread.traveler.service.ActivityService;
import com.bread.traveler.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author huang
* @description 针对表【activity】的数据库操作Service实现
* @createDate 2025-11-17 22:29:15
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}




