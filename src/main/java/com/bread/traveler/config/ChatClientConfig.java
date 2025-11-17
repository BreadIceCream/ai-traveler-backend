package com.bread.traveler.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ChatClientConfig {

    @Bean("zhiPuChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient zhiPuChatClient(ZhiPuAiChatModel zhiPuAiChatModel){
        return ChatClient.builder(zhiPuAiChatModel).build();
    }


    @Bean("openAiChatClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }


}
