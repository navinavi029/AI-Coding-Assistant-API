package com.assistant.multiagent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Coding Assistant API")
                        .version("1.0.0")
                        .description("Multi-agent coding assistance through specialized agents using NVIDIA AI")
                        .contact(new Contact()
                                .name("AI Coding Assistant")
                                .url("https://github.com/navinavi029/general-coding-assistant-api")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ));
    }
}
