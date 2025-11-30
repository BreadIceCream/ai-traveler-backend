package com.bread.traveler.controller;

import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.bread.traveler.service.AiRecommendationConversationService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation/conversation")
@Tag(name = "AI推荐会话管理")
public class AiRecommendationConversationController {

    @Autowired
    private AiRecommendationConversationService aiRecommendConversationService;

    @PostMapping("/create")
    @Operation(summary = "创建AI推荐会话", description = "创建一个AI推荐会话\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"conversationId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"title\": \"北京旅游推荐\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"updatedAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result createConversation(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "查询文本", example = "我想去北京旅游") @RequestParam String queryText) {
        AiRecommendationConversation conversation = aiRecommendConversationService.createConversation(userId,
                queryText);
        return Result.success(conversation);
    }

    @PutMapping("/rename")
    @Operation(summary = "重命名AI推荐会话", description = "重命名一个AI推荐会话\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"conversationId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"title\": \"北京之旅\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"updatedAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result renameConversation(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId,
            @Schema(description = "新标题", example = "北京之旅") @RequestParam String newTitle) {
        AiRecommendationConversation conversation = aiRecommendConversationService.renameConversation(userId,
                conversationId, newTitle);
        return Result.success(conversation);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有AI推荐会话", description = "获取当前用户下的所有AI推荐会话\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"conversationId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"title\": \"北京旅游推荐\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"updatedAt\": \"2023-10-01T12:00:00+08:00\"\n    }\n  ]\n}\n```")
    public Result getAllConversations(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId) {
        List<AiRecommendationConversation> conversations = aiRecommendConversationService.getAllConversations(userId);
        return Result.success(conversations);
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "获取AI推荐会话", description = "获取一个AI推荐会话\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"conversationId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"title\": \"北京旅游推荐\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"updatedAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result getConversation(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @PathVariable UUID conversationId) {
        AiRecommendationConversation conversation = aiRecommendConversationService.searchConversationById(userId,
                conversationId);
        return Result.success(conversation);
    }

    @GetMapping("/history/{conversationId}")
    @Operation(summary = "获取AI推荐会话历史", description = "获取一个AI推荐会话的历史内容\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"role\": \"user\",\n      \"content\": \"我想去北京\"\n    },\n    {\n      \"role\": \"assistant\",\n      \"content\": \"好的，推荐...\"\n    }\n  ]\n}\n```")
    public Result getConversationHistory(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @PathVariable UUID conversationId) {
        List<Message> history = aiRecommendConversationService.getConversationHistory(userId, conversationId);
        return Result.success(history);
    }

    @PostMapping("/handle")
    @Operation(summary = "处理用户对话", description = "处理用户的当前问题\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"aiMessages\": [\"您好，推荐您去故宫博物院参观\", \"故宫是中国古代宫殿建筑群...\"],\n    \"toolUse\": [\"POI_SEARCH\", \"WEB_SEARCH\"],\n    \"toolCallResults\": {\n      \"POI_SEARCH\": [\"故宫博物院\", \"天安门广场\"],\n      \"WEB_SEARCH\": [\"故宫开放时间\"]\n    }\n  }\n}\n```")
    public Result handleQuery(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId,
            @Schema(description = "查询文本", example = "推荐一些北京的景点") @RequestParam String queryText) {
        AiRecommendResponse response = aiRecommendConversationService.handleQuery(userId, conversationId, queryText);
        return Result.success(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除AI推荐会话", description = "删除一个AI推荐会话\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": true\n}\n```")
    public Result deleteConversation(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId) {
        boolean result = aiRecommendConversationService.deleteConversation(userId, conversationId);
        return Result.success(result);
    }

}
