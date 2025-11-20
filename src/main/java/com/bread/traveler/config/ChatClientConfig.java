package com.bread.traveler.config;

import com.bread.traveler.tools.RecommendationTools;
import com.bread.traveler.tools.WebFetchTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;


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
    public ChatClient recommendChatClient(ZhiPuAiChatModel zhiPuAiChatModel, RecommendationTools recommendationTools){
        // 只负责aiRecommendation的对话处理，复杂的extract任务由extractItemsClient处理
        // 提供webSearch、poiSearch工具，可以搜索网页和POI，但不负责具体的解析
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();
        return ChatClient.builder(zhiPuAiChatModel)
                // 添加MemoryAdvisor
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(new ClassPathResource("prompts/RecommendationSystemPrompt.md"))
                .defaultTools(recommendationTools)
                .defaultOptions(ChatOptions.builder().model("GLM-4-FlashX-250414").build())
                .build();
    }


    @Bean(name = "openAiChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }


}
