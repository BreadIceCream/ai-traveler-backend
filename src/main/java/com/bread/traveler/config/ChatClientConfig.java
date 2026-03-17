package com.bread.traveler.config;

import com.bread.traveler.tools.RecommendationTools;
import com.bread.traveler.tools.ToolNames;
import com.bread.traveler.tools.WebFetchTools;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.mcp.*;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import java.util.List;


@Configuration
public class ChatClientConfig {

    @Value("${zhipuai.default-model}")
    String defaultModel;
    @Value("${zhipuai.flash-model}")
    String flashModel;

    @Bean(name = "miniTaskClient")
    public ChatClient miniTaskClient(ZhiPuAiChatModel zhiPuAiChatModel) {
        return ChatClient.builder(zhiPuAiChatModel)
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        // 使用快速模型
                        .model(flashModel)
                        // 小任务关闭思考模式
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                        .temperature(0.5).build())
                .build();

    }

    @Bean(name = "extractItemsClient")
    public ChatClient extractItemsClient(ZhiPuAiChatModel zhiPuAiChatModel) {
        return ChatClient.builder(zhiPuAiChatModel)
                // 系统提示词
                .defaultSystem(new ClassPathResource("prompts/ExtractItemsSystemPrompt.md"))
                .defaultTools(new WebFetchTools())
                // 采用快速模型
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        // 使用快速模型
                        .model(flashModel)
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                        .temperature(0.5).build())
                .build();
    }


    @Bean(name = "recommendChatClient")
    public ChatClient recommendChatClient(ZhiPuAiChatModel zhiPuAiChatModel,
                                          RecommendationTools recommendationTools,
                                          List<McpSyncClient> mcpSyncClients,
                                          McpToolNamePrefixGenerator toolNamePrefixGenerator) {
        // 只负责aiRecommendation的对话处理，复杂的extract任务由extractItemsClient处理
        // 提供webSearch、poiSearch和部分高德Mcp工具
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClients)
                .toolFilter((mcpConnectionInfo, tool) -> ToolNames.GAODE_MCP_TOOLS_ALLOWED.contains(tool.name()))
                .toolNamePrefixGenerator(toolNamePrefixGenerator).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加系统提示词，默认工具，模型选择
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultTools(recommendationTools)
                .defaultSystem(new ClassPathResource("prompts/RecommendationSystemPrompt.md"))
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        .model(defaultModel)
                        .temperature(0.7)
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled()).build())
                .build();
    }

    @Bean(name = "routePlanClient")
    public ChatClient routePlanClient(ZhiPuAiChatModel zhiPuAiChatModel, List<McpSyncClient> mcpSyncClients, McpToolNamePrefixGenerator toolNamePrefixGenerator) {
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClients)
                .toolFilter((mcpConnectionInfo, tool) -> ToolNames.ROUTE_PLAN_CLIENT_GAODE_MCP_TOOLS.contains(tool.name()))
                .toolNamePrefixGenerator(toolNamePrefixGenerator).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加系统提示词，默认工具，模型选择
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem(new ClassPathResource("prompts/RoutePlanClientSystemPrompt.md"))
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        .model(flashModel)
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                        .temperature(0.5).build())
                .build();
    }

    @Bean(name = "tripPlanClient")
    public ChatClient tripPlanClient(ZhiPuAiChatModel zhiPuAiChatModel, List<McpSyncClient> mcpSyncClients, McpToolNamePrefixGenerator toolNamePrefixGenerator) {
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClients)
                .toolFilter((mcpConnectionInfo, tool) -> ToolNames.TRIP_PLAN_CLIENT_GAODE_MCP_TOOLS.contains(tool.name()))
                .toolNamePrefixGenerator(toolNamePrefixGenerator).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加系统提示词，默认工具，模型选择
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem(new ClassPathResource("prompts/TripPlanClientSystemPrompt.md"))
                .defaultOptions(ZhiPuAiChatOptions.builder()
                        .model(defaultModel)
                        .thinking(ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                        .temperature(0.6).build())
                .build();
    }


//    @Bean(name = "openAiChatClient")
//    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
//        return ChatClient.builder(openAiChatModel).build();
//    }


}
