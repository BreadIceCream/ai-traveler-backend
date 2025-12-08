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

    /**
     * 根据用户ID查找用户
     * @param userId
     * @return 完整用户信息，包括密码
     */
    Users findUserById(UUID userId);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 完整用户信息，包括密码
     */
    Users getUserByUsername(String username);

    /**
     * 创建用户
     *
     * @param username
     * @param password
     * @param preferencesText
     * @return
     */
    void createUser(String username, String password, String preferencesText);

    /**
     * 更新用户偏好设置
     * @param userId 用户ID
     * @param preferencesText 偏好文本
     * @return 更新后的Users对象
     */
    Users updateUserPreferences(UUID userId, String preferencesText);

    /**
     * 查找兴趣相似用户
     * @param userId 用户ID
     * @param limit 查找数量限制
     * @return 相似用户列表
     */
    List<Users> findSimilarUsers(UUID userId, int limit);


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);
}
