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
                        .title("Multi-Agent Coding Assistant API")
                        .version("1.0.0")
                        .description("AI-powered coding assistance through specialized agents using NVIDIA's Gemini API")
                        .contact(new Contact()
                                .name("Multi-Agent Assistant")
                                .url("https://github.com/your-repo")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development Server"),
                        new Server().url("http://localhost:8080").description("Default Server")
                ));
    }
}
