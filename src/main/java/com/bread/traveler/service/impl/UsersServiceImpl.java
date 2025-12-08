package com.bread.traveler.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author huang
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {


    @Autowired
    private VectorStore vectorStore;
    @Value("${jwt.expire}")
    private long expire;
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Users findUserById(UUID userId) {
        Users user = getById(userId);
        if (user == null) {
            throw new BusinessException(Constant.USERS_NOT_EXIST);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(String username, String password, String preferencesText) {
        log.info("Create user: {}, pw {}", username, password);
        Assert.notBlank(username, "Username cannot be empty");
        Assert.notBlank(password, "Password cannot be empty");
        Users user = new Users();
        String passwordHash = BCrypt.hashpw(password);
        user.setUsername(username);
        user.setPassword(passwordHash);
        user.setPreferencesText(preferencesText);
        user.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        try {
            save(user);
            // vector store中id即为userId, metadata中需要标识当前为users实体类
            Document userDocument = Document.builder()
                    .id(user.getUserId().toString())
                    .text(preferencesText)
                    .metadata(Map.of("entity", "Users"))
                    .build();
            vectorStore.add(List.of(userDocument));
            log.info("User created successfully.");
        } catch (Exception e) {
            log.error("Create user failed.", e);
            throw new BusinessException(Constant.USERS_CREATE_FAILED);
        }
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
    @Transactional(rollbackFor = Exception.class)
    public Users updateUserPreferences(UUID userId, String preferencesText) {
        Users user = findUserById(userId);
        user.setPreferencesText(preferencesText);
        try {
            // 更新vector store
            vectorStore.delete(List.of(userId.toString()));
            Document userDocument = Document.builder()
                    .id(user.getUserId().toString())
                    .text(preferencesText)
                    .metadata(Map.of("entity", "Users"))
                    .build();
            vectorStore.add(List.of(userDocument));
            if (updateById(user)) {
                log.info("User preferences updated successfully.");
                user.setPassword(null);
                return user;
            }else {
                throw new BusinessException(Constant.USERS_PREFERENCES_UPDATE_FAILED);
            }
        } catch (Exception e) {
            log.error("Update user preferences failed.", e);
            throw new BusinessException(Constant.USERS_PREFERENCES_UPDATE_FAILED);
        }
    }

    @Override
    public List<Users> findSimilarUsers(UUID userId, int limit) {
        log.info("Find similar users for user: {}", userId);
        Users user = findUserById(userId);
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .filterExpression("entity == 'Users'")
                .query(user.getPreferencesText())
                .topK(limit).build());
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyList();
        }
        // 解析相似用户,去掉用户本身
        List<UUID> similarUserIds = documents.stream()
                .filter(document -> !document.getId().equals(userId.toString()))
                .map(document -> {
                    log.info("Document: {}, score: {}", document.getId(), document.getScore());
                    return UUID.fromString(document.getId());
                }).toList();
        List<Users> users = listByIds(similarUserIds);
        for (Users u : users) {
            u.setPassword(null);
        }
        return users;
    }

    @Override
    public String login(String username, String password) {
        log.info("Login user: {}, pw {}", username, password);
        Assert.notBlank(username, "Username cannot be empty");
        Assert.notBlank(password, "Password cannot be empty");
        // 获取用户
        Users user = getUserByUsername(username);
        // 验证密码
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(Constant.USERS_PASSWORD_ERROR);
        }
        // 生成JWT token
        // 3. 生成 JWT Token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("username", user.getUsername());
        // Hutool 会自动添加 exp (过期时间) 等标准 Claims，这里设置 Key
        String token = JWTUtil.createToken(payload, secret.getBytes());

        // 4. 将 Token 存入 Redis (实现服务端管理，比如强制下线)
        // Key 格式: login:token:{userId}
        String redisKey = Constant.USERS_LOGIN_REDIS_KEY + user.getUserId();
        redisTemplate.opsForValue().set(redisKey, token, 24, TimeUnit.HOURS);

        return token;
    }

}




