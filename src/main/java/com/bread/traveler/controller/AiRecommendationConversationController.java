package com.bread.traveler.controller;

import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.bread.traveler.service.AiRecommendationConversationService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "创建AI推荐会话", description = "创建一个AI推荐会话")
    public Result createConversation(@RequestParam UUID userId, @RequestParam String queryText) {
        AiRecommendationConversation conversation = aiRecommendConversationService.createConversation(userId, queryText);
        return Result.success(conversation);
    }

    @PutMapping("/rename")
    @Operation(summary = "重命名AI推荐会话", description = "重命名一个AI推荐会话")
    public Result renameConversation(@RequestParam UUID userId, @RequestParam UUID conversationId, @RequestParam String newTitle) {
        AiRecommendationConversation conversation = aiRecommendConversationService.renameConversation(userId, conversationId, newTitle);
        return Result.success(conversation);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有AI推荐会话", description = "获取当前用户下的所有AI推荐会话")
    public Result getAllConversations(@RequestParam UUID userId) {
        List<AiRecommendationConversation> conversations = aiRecommendConversationService.getAllConversations(userId);
        return Result.success(conversations);
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "获取AI推荐会话", description = "获取一个AI推荐会话")
    public Result getConversation(@RequestParam UUID userId, @PathVariable UUID conversationId) {
        AiRecommendationConversation conversation = aiRecommendConversationService.searchConversationById(userId, conversationId);
        return Result.success(conversation);
    }

    @GetMapping("/history/{conversationId}")
    @Operation(summary = "获取AI推荐会话历史", description = "获取一个AI推荐会话的历史内容")
    public Result getConversationHistory(@RequestParam UUID userId, @PathVariable UUID conversationId) {
        List<Message> history = aiRecommendConversationService.getConversationHistory(userId, conversationId);
        return Result.success(history);
    }

    @PostMapping("/handle")
    @Operation(summary = "处理用户对话", description = "处理用户的当前问题")
    public Result handleQuery(@RequestParam UUID userId, @RequestParam UUID conversationId, @RequestParam String queryText) {
        AiRecommendResponse response = aiRecommendConversationService.handleQuery(userId, conversationId, queryText);
        return Result.success(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除AI推荐会话", description = "删除一个AI推荐会话")
    public Result deleteConversation(@RequestParam UUID userId, @RequestParam UUID conversationId) {
        boolean result = aiRecommendConversationService.deleteConversation(userId, conversationId);
        return Result.success(result);
    }

}
