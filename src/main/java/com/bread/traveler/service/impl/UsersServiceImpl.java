package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * @author huang
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {


    @Autowired
    private EmbeddingModel embeddingModel;

    @Override
    public Users findUserById(UUID userId) {
        Users user = getById(userId);
        if (user == null) {
            throw new BusinessException(Constant.USERS_NOT_EXIST);
        }
        return user;
    }

    @Override
    public Users createUser(String username, String preferencesText) {
        Users user = new Users();
        float[] embedding = embeddingModel.embed(preferencesText);
        user.setUsername(username);
        user.setPreferencesText(preferencesText);
        user.setPreferencesEmbedding(embedding);
        user.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        try {
            save(user);
        } catch (Exception e) {
            throw new BusinessException(Constant.USERS_CREATE_FAILED);
        }
        return user;
    }

    @Override
    public Users getUserByUsername(String username) {
        Users user = lambdaQuery().eq(Users::getUsername, username).one();
        if (user == null) {
            throw new BusinessException(Constant.USERS_NOT_EXIST);
        }
        return user;
    }

    @Override
    public boolean updateUserPreferences(UUID userId, String preferencesText) {
        Users user = findUserById(userId);
        // 计算偏好向量
        float[] vectors = embeddingModel.embed(preferencesText);
        // 更新用户的偏好设置
        user.setPreferencesText(preferencesText);
        user.setPreferencesEmbedding(vectors);
        return updateById(user);
    }

    @Override
    public List<Users> findSimilarUsers(UUID userId, int limit) {
        return List.of();
    }

}




