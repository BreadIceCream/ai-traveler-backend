package com.bread.traveler.service;

import com.bread.traveler.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【users】的数据库操作Service
* @createDate 2025-11-14 12:09:43
*/
public interface UsersService extends IService<Users> {

    // 基础CRUD操作（继承自IService）
    // save(), saveBatch(), removeById(), updateById(), getById(), list(), page()

    /**
     * 根据用户ID查找用户
     * @param userId
     * @return
     */
    Users findUserById(UUID userId);

    /**
     * 创建用户
     * @param username
     * @param preferencesText
     * @return
     */
    Users createUser(String username, String preferencesText);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    Users getUserByUsername(String username);

    /**
     * 更新用户偏好设置
     * @param userId 用户ID
     * @param preferencesText 偏好文本
     * @return 是否更新成功
     */
    boolean updateUserPreferences(UUID userId, String preferencesText);

    /**
     * 查找兴趣相似用户
     * @param userId 用户ID
     * @param limit 查找数量限制
     * @return 相似用户列表
     */
    List<Users> findSimilarUsers(UUID userId, int limit);


}
