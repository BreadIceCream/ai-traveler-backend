package com.bread.traveler.service;

import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【ai_recommendation_conversation】的数据库操作Service
* @createDate 2025-11-17 22:29:15
*/
public interface AiRecommendationConversationService extends IService<AiRecommendationConversation> {

    /**
     * 创建会话
     * @param userId
     * @param queryText
     * @return
     */
    AiRecommendationConversation createConversation(UUID userId, String queryText);

    /**
     * 重命名会话
     * @param userId
     * @param conversationId
     * @param newTitle
     * @return
     */
    AiRecommendationConversation renameConversation(UUID userId, UUID conversationId, String newTitle);

    /**
     * 处理用户的对话
     * @param userId
     * @param conversationId
     * @param queryText
     * @return
     */
    AiRecommendResponse handleQuery(UUID userId, UUID conversationId, String queryText);

    /**
     * 根据id搜索会话
     * @param userId
     * @param conversationId
     * @return
     */
    AiRecommendationConversation searchConversationById(UUID userId, UUID conversationId);

    /**
     * 获取指定会话的历史内容
     * @param userId
     * @param conversationId
     * @return
     */
    List<Message> getConversationHistory(UUID userId, UUID conversationId);

    /**
     * 获取所有会话
     * @param userId
     * @return
     */
    List<AiRecommendationConversation> getAllConversations(UUID userId);

    /**
     * 删除会话, 会话内所有内容也会被删除，包括记忆、工具调用结果（关联的webPage和aiRecommendationItem）
     * @param userId
     * @param conversationId
     * @return
     */
    boolean deleteConversation(UUID userId, UUID conversationId);

}