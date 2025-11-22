package com.bread.traveler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.AiRecommendationConversation;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.AiRecommendationConversationService;
import com.bread.traveler.mapper.AiRecommendationConversationMapper;
import com.bread.traveler.service.AiRecommendationItemsService;
import com.bread.traveler.service.WebSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Content;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.bread.traveler.tools.RecommendationTools.TOOL_CONTEXT_CONVERSATION_ID;

/**
 * @author huang
 * @description 针对表【ai_recommendation_conversation】的数据库操作Service实现
 * @createDate 2025-11-17 22:29:15
 */
@Service
public class AiRecommendationConversationServiceImpl extends ServiceImpl<AiRecommendationConversationMapper, AiRecommendationConversation> implements AiRecommendationConversationService {

    @Autowired
    @Qualifier("recommendChatClient")
    private ObjectProvider<ChatClient> recommendChatClientProvider;
    @Autowired
    @Qualifier("miniTaskClient")
    private ObjectProvider<ChatClient> miniTaskClientProvider;
    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;
    @Autowired
    private WebSearchService webSearchService;
    @Autowired
    private AiRecommendationItemsService aiRecommendationItemsService;

    @Override
    public AiRecommendationConversation createConversation(UUID userId, String queryText) {
        AiRecommendationConversation conversation = new AiRecommendationConversation();
        ChatClient client = miniTaskClientProvider.getObject();
        String title = client.prompt().system(new ClassPathResource("prompts/TitleGenerateSystemPrompt.md"))
                .user(queryText).call().content();
        conversation.setUserId(userId);
        conversation.setTitle(title);
        conversation.setConversationId(UUID.randomUUID());
        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        conversation.setCreatedAt(now);
        conversation.setUpdatedAt(now);
        save(conversation);
        return conversation;
    }

    @Override
    public AiRecommendationConversation searchConversationById(UUID userId, UUID conversationId) {
        AiRecommendationConversation conversation = getById(conversationId);
        if (conversation == null){
            throw new BusinessException(Constant.CONVERSATION_NOT_EXIST);
        }
        if (!conversation.getUserId().equals(userId)){
            throw new BusinessException(Constant.CONVERSATION_PERMISSION_DENIED);
        }
        return conversation;
    }

    @Override
    public AiRecommendationConversation renameConversation(UUID userId, UUID conversationId, String newTitle) {
        AiRecommendationConversation conversation = searchConversationById(userId, conversationId);
        conversation.setTitle(newTitle);
        conversation.setUpdatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        updateById(conversation);
        return conversation;
    }

    @Override
    public AiRecommendResponse handleQuery(UUID userId, UUID conversationId, String queryText) {
        // 对当前对话加锁，避免多个线程同时处理同一个对话
        // 对于对话，同步串行处理，避免在同一对话中并行处理用户的多个输入请求
        synchronized (conversationId.toString()){
            // 更新对话的updatedAt
            AiRecommendationConversation conversation = searchConversationById(userId, conversationId);
            conversation.setUpdatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
            updateById(conversation);
            // 保存使用的工具及其结果
            Set<String> toolUse = new HashSet<>();
            Map<String, List<String>> toolCallResults = new HashMap<>();
            // 创建client和memory，手动管理记忆（只有本轮对话正常结束返回时，才会将此轮对话保存到数据库memory当中）
            // 数据库memory只保存UserMessage和AssistantMessage，不保存ToolMessage，SystemMessage已经配置
            ChatClient client = recommendChatClientProvider.getObject();
            ChatMemory chatMemory = MessageWindowChatMemory.builder()
                    .chatMemoryRepository(chatMemoryRepository)
                    .maxMessages(Constant.CHAT_MEMORY_MAX_MESSAGES)
                    .build();
            // 采用user-controlled Tool Execution，添加ToolContext
            ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();
            ChatOptions chatOptions = ToolCallingChatOptions.builder()
                    .model("GLM-4.5-AirX")
                    .internalToolExecutionEnabled(false)
                    .toolContext(Map.of(TOOL_CONTEXT_CONVERSATION_ID, conversationId))
                    .build();
            // 获取全部历史对话信息history，currentDialogueMessages保存本轮对话
            List<Message> currentDialogueMessages = new ArrayList<>();
            List<Message> history = chatMemory.get(conversationId.toString());
            UserMessage userMessage = new UserMessage(queryText);
            currentDialogueMessages.add(userMessage);
            history.add(userMessage);
            Prompt prompt = new Prompt(history, chatOptions);
            // 调用client，将输出添加到currentDialogueMessages和history中
            ChatResponse chatResponse = client.prompt(prompt)
                    .call().chatResponse();
            addToHistoryAndDialogue(chatResponse, history, currentDialogueMessages);
            while(chatResponse.hasToolCalls()){
                // 处理工具调用
                ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
                Message lastMessage = toolExecutionResult.conversationHistory().getLast();
                // 解析工具调用结果
                if (lastMessage instanceof ToolResponseMessage toolResponseMessage){
                    // lastMessage是工具调用的结果，强转
                    List<ToolResponseMessage.ToolResponse> toolResponses = toolResponseMessage.getResponses();
                    // 将每个工具调用的结果添加到toolUse和toolCallResults中
                    toolResponses.forEach(toolResponse -> {
                        toolUse.add(toolResponse.name());
                        List<String> prevResults = toolCallResults.getOrDefault(toolResponse.name(), new ArrayList<>());
                        prevResults.add(toolResponse.responseData());
                        toolCallResults.put(toolResponse.name(), prevResults);
                    });
                }
                // 创建新的prompt，继续处理。这里的conversationHistory是全部对话信息
                prompt = new Prompt(toolExecutionResult.conversationHistory(), chatOptions);
                chatResponse = client.prompt(prompt)
                        .call().chatResponse();
                addToHistoryAndDialogue(chatResponse, history, currentDialogueMessages);
                // 循环处理，直到不需要工具调用
            }
            // 将本轮对话保存到记忆，并返回本轮对话中的AI输出
            chatMemory.add(conversationId.toString(), currentDialogueMessages);
            return new AiRecommendResponse(currentDialogueMessages.stream()
                    .filter(message -> message.getMessageType().equals(MessageType.ASSISTANT))
                    .map(Content::getText).toList(), toolUse, toolCallResults);
        }
    }

    private void addToHistoryAndDialogue(ChatResponse chatResponse, List<Message> history, List<Message> currentDialogueAiMessages) {
        List<AssistantMessage> aiMessages = chatResponse.getResults().stream()
                .map(Generation::getOutput)
                // 筛选出非空的文本
                .filter(message -> message.getText() != null && !message.getText().isBlank()).toList();
        history.addAll(aiMessages);
        currentDialogueAiMessages.addAll(aiMessages);
    }

    @Override
    public List<Message> getConversationHistory(UUID userId, UUID conversationId) {
        AiRecommendationConversation conversation = searchConversationById(userId, conversationId);
        // 获取对话历史，对话已经按照时间排序
        List<Message> conversationHistory = chatMemoryRepository.findByConversationId(conversation.getConversationId().toString());
        // 只保留用户消息和AI消息
        return conversationHistory.stream().filter(message -> {
            // 只保留用户消息和AI消息
            MessageType messageType = message.getMessageType();
            return messageType.equals(MessageType.USER) || messageType.equals(MessageType.ASSISTANT);
        }).toList();
    }

    @Override
    public List<AiRecommendationConversation> getAllConversations(UUID userId) {
        List<AiRecommendationConversation> allConversations = lambdaQuery().eq(AiRecommendationConversation::getUserId, userId).list();
        // 按照更新时间倒序排序
        allConversations.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return allConversations;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConversation(UUID userId, UUID conversationId) {
        AiRecommendationConversation conversation = searchConversationById(userId, conversationId);
        // 删除会话关联的webPage
        boolean aResult = webSearchService.deleteByConversationId(conversationId);
        // 删除会话中的Items
        boolean bResult = aiRecommendationItemsService.removeAllItems(userId, conversationId);
        // 删除会话
        boolean cResult = removeById(conversation);
        return aResult && bResult && cResult;
    }
}




