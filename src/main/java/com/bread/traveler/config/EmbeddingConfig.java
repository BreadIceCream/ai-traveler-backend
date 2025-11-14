package com.bread.traveler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EmbeddingConfig {

    @Value("${embedding.api-key}")
    private String apiKey;
    @Value("${embedding.base-url}")
    private String baseUrl;
    @Value("${embedding.model}")
    private String model;

    @Bean(name = "embeddingModel")
    @ConditionalOnProperty(name = "embedding.manufacturer", havingValue = "zhipuai")
    public EmbeddingModel ZhipuEmbeddingModel(){
        log.info("Using ZhiPuAiEmbeddingModel...");
        return new ZhiPuAiEmbeddingModel(
                new ZhiPuAiApi(apiKey),
                MetadataMode.EMBED,
                ZhiPuAiEmbeddingOptions.builder()
                        .model(model)
                        .dimensions(1536)
                        .build()
        );
    }

    @Bean(name = "embeddingModel")
    @ConditionalOnProperty(name = "embedding.manufacturer", havingValue = "openai", matchIfMissing = true)
    public EmbeddingModel OpenAIEmbeddingModel(){
        log.info("Using OpenAiEmbeddingModel...");
        OpenAiApi.Builder apiBuilder = new OpenAiApi.Builder().apiKey(apiKey);
        if (baseUrl != null && !baseUrl.isBlank()){
            apiBuilder.baseUrl(baseUrl);
        }
        return new OpenAiEmbeddingModel(
                apiBuilder.build(),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(model)
                        .dimensions(1536)
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );
    }

}
