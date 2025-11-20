package com.bread.traveler.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
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
                .defaultOptions(ChatOptions.builder().model("GLM-4-FlashX-250414").build())
                .build();
    }

    @Bean(name = "extractItemsClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient extractItemsClient(ZhiPuAiChatModel zhiPuAiChatModel, List<McpSyncClient> mcpSyncClients){
        return ChatClient.builder(zhiPuAiChatModel)
                // 系统提示词
                .defaultSystem(new ClassPathResource("prompts/ExtractItemsSystemPrompt.md"))
                // 配置Mcp工具fetch
                .defaultTools(new SyncMcpToolCallbackProvider(mcpSyncClients))
                // 采用GLM-4.5-AirX模型
                .defaultOptions(ChatOptions.builder()
                        .model("GLM-4.5-AirX")
                        .temperature(0.5).build())
                .build();
    }


    @Bean(name = "recommendChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient recommendChatClient(ZhiPuAiChatModel zhiPuAiChatModel){
        return ChatClient.builder(zhiPuAiChatModel)
                .defaultOptions(ChatOptions.builder().model("GLM-4-FlashX-250414").build())
                .build();
    }


    @Bean(name = "openAiChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }


}
