package com.bread.traveler.config;

import com.bread.traveler.tools.RecommendationTools;
import com.bread.traveler.tools.ToolNames;
import com.bread.traveler.tools.WebFetchTools;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.mcp.*;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import java.util.List;


@Configuration
public class ChatClientConfig {

    @Bean(name = "miniTaskClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient miniTaskClient(ZhiPuAiChatModel zhiPuAiChatModel){
        return ChatClient.builder(zhiPuAiChatModel)
                .defaultOptions(ChatOptions.builder()
                        .model("GLM-4-FlashX-250414")
                        .temperature(0.5).build())
                .build();
    }

    @Bean(name = "extractItemsClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient extractItemsClient(ZhiPuAiChatModel zhiPuAiChatModel){
        return ChatClient.builder(zhiPuAiChatModel)
                // 系统提示词
                .defaultSystem(new ClassPathResource("prompts/ExtractItemsSystemPrompt.md"))
                .defaultTools(new WebFetchTools())
                // 采用GLM-4.5-AirX模型
                .defaultOptions(ChatOptions.builder()
                        .model("GLM-4.5-AirX")
                        .temperature(0.5).build())
                .build();
    }


    @Bean(name = "recommendChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient recommendChatClient(ZhiPuAiChatModel zhiPuAiChatModel,
                                          RecommendationTools recommendationTools,
                                          List<McpSyncClient> mcpSyncClients,
                                          McpToolNamePrefixGenerator toolNamePrefixGenerator){
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
                .defaultOptions(ChatOptions.builder().model("GLM-4.5-AirX").build())
                .build();
    }

    @Bean(name = "routePlanClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient routePlanClient(ZhiPuAiChatModel zhiPuAiChatModel, List<McpSyncClient> mcpSyncClients, McpToolNamePrefixGenerator toolNamePrefixGenerator){
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClients)
                .toolFilter((mcpConnectionInfo, tool) -> ToolNames.ROUTE_PLAN_CLIENT_GAODE_MCP_TOOLS.contains(tool.name()))
                .toolNamePrefixGenerator(toolNamePrefixGenerator).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加系统提示词，默认工具，模型选择
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem(new ClassPathResource("prompts/RoutePlanClientSystemPrompt.md"))
                .defaultOptions(ChatOptions.builder().model("GLM-4.5-AirX").temperature(0.4).build())
                .build();
    }

    @Bean(name = "tripPlanClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient tripPlanClient(ZhiPuAiChatModel zhiPuAiChatModel, List<McpSyncClient> mcpSyncClients, McpToolNamePrefixGenerator toolNamePrefixGenerator){
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClients)
                .toolFilter((mcpConnectionInfo, tool) -> ToolNames.TRIP_PLAN_CLIENT_GAODE_MCP_TOOLS.contains(tool.name()))
                .toolNamePrefixGenerator(toolNamePrefixGenerator).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加系统提示词，默认工具，模型选择
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultSystem(new ClassPathResource("prompts/TripPlanClientSystemPrompt.md"))
                .defaultOptions(ChatOptions.builder().model("GLM-4.5-AirX").temperature(0.7).build())
                .build();
    }


//    @Bean(name = "openAiChatClient")
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
//        return ChatClient.builder(openAiChatModel).build();
//    }


}
