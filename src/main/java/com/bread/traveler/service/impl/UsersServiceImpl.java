package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private VectorStore vectorStore;

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
    public Users createUser(String username, String preferencesText) {
        Users user = new Users();
        user.setUsername(username);
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
        } catch (Exception e) {
            log.error("Create user failed.", e);
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
        return listByIds(similarUserIds);
    }

}




