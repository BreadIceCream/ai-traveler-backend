package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.bread.traveler.service.AiRecommendationConversationService;
import com.bread.traveler.mapper.AiRecommendationConversationMapper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【ai_recommendation_conversation】的数据库操作Service实现
* @createDate 2025-11-17 22:29:15
*/
@Service
public class AiRecommendationConversationServiceImpl extends ServiceImpl<AiRecommendationConversationMapper, AiRecommendationConversation>
    implements AiRecommendationConversationService{

    @Override
    public AiRecommendationConversation createConversation(UUID userId, String queryText) {
        return null;
    }

    @Override
    public AiRecommendationConversation renameConversation(UUID userId, UUID conversationId, String newTitle) {
        return null;
    }

    @Override
    public AiRecommendResponse handleQuery(UUID userId, UUID conversationId, String queryText) {
        return null;
    }

    @Override
    public List<Message> getConversationHistory(UUID userId, UUID conversationId) {
        return List.of();
    }
}




