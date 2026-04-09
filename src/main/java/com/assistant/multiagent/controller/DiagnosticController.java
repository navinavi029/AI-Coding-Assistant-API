package com.assistant.multiagent.controller;

import com.assistant.multiagent.config.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/diagnostic")
public class DiagnosticController {

    private final WebClient webClient;
    private final AppConfig appConfig;

    public DiagnosticController(WebClient webClient, AppConfig appConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
    }

    @GetMapping("/test-nvidia-connection")
    public ResponseEntity<Map<String, Object>> testNvidiaConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            String response = webClient.post()
                    .uri(appConfig.getNvidiaApiUrl())
                    .header("Authorization", "Bearer " + appConfig.getNvidiaApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of(
                            "model", "google/gemma-4-31b-it",
                            "messages", java.util.List.of(
                                    Map.of("role", "user", "content", "Hi")
                            ),
                            "max_tokens", 10
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
            
            long endTime = System.currentTimeMillis();
            
            result.put("status", "SUCCESS");
            result.put("responseTime", (endTime - startTime) + "ms");
            result.put("response", response);
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
            if (e.getCause() != null) {
                result.put("cause", e.getCause().getMessage());
            }
        }
        
        return ResponseEntity.ok(result);
    }
}
