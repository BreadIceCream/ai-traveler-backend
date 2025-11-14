package com.bread.traveler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Travel Assistant")
                        .description("智能旅游计划AI助手")
                        .version("v2.0")
                        .contact(new Contact().name("Bread").email("bread@bread.com")));
    }

}
