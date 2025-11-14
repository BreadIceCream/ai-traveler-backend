package com.bread.traveler.mapper;

import com.bread.traveler.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author huang
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-11-14 12:09:43
* @Entity com.bread.traveler.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




