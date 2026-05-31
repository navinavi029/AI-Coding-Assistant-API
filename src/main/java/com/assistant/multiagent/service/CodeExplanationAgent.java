package com.assistant.multiagent.service;

import com.assistant.multiagent.client.NvidiaApiClient;
import com.assistant.multiagent.config.AppConfig;
import com.assistant.multiagent.model.AgentType;
import com.assistant.multiagent.model.AssistResponse;
import com.assistant.multiagent.model.NvidiaRequest;
import com.assistant.multiagent.model.NvidiaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;

/**
 * Agent implementation specialized in code explanation.
 * Explains code clearly and concisely, breaking down complex logic into understandable concepts.
 * 
 * Validates: Requirements 8.3, 8.4, 1.5
 */
@Service
public class CodeExplanationAgent implements Agent {

    private static final Logger logger = LoggerFactory.getLogger(CodeExplanationAgent.class);

    private static final String SYSTEM_PROMPT = 
        "You are an expert code explanation assistant. Explain the provided code clearly and concisely, " +
        "breaking down complex logic into understandable concepts. Assume the reader has basic programming knowledge.";

    private final NvidiaApiClient nvidiaApiClient;
    private final AppConfig appConfig;

    /**
     * Constructs a new CodeExplanationAgent with injected dependencies.
     *
     * @param nvidiaApiClient the NVIDIA API client
     * @param appConfig the application configuration
     */
    public CodeExplanationAgent(NvidiaApiClient nvidiaApiClient, AppConfig appConfig) {
        this.nvidiaApiClient = nvidiaApiClient;
        this.appConfig = appConfig;
    }

    /**
     * Returns the system prompt for code explanation.
     *
     * @return the system prompt string
     */
    @Override
    public String getSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    @Override
    public AssistResponse process(String prompt) {
        logger.info("Processing code explanation request");

        try {
            // Build request with system prompt prepended
            NvidiaRequest request = NvidiaRequest.builder()
                    .model(appConfig.getNvidiaModel())
                    .addSystemMessage(SYSTEM_PROMPT)
                    .addUserMessage(prompt)
                    .stream(false)
                    .build();

            // Call NVIDIA API
            NvidiaResponse nvidiaResponse = nvidiaApiClient.sendRequest(request);

            // Extract content from response
            String content = extractContent(nvidiaResponse);

            // Build and return AssistResponse
            return AssistResponse.builder()
                    .content(content)
                    .agentType(AgentType.CODE_EXPLANATION.name())
                    .timestamp(Instant.now().toString())
                    .addMetadata("model", nvidiaResponse.getModel())
                    .addMetadata("responseId", nvidiaResponse.getId())
                    .build();

        } catch (Exception e) {
            logger.error("Error processing code explanation request", e);
            return AssistResponse.builder()
                    .error("Failed to explain code: " + e.getMessage())
                    .agentType(AgentType.CODE_EXPLANATION.name())
                    .timestamp(Instant.now().toString())
                    .build();
        }
    }

    /**
     * Processes a user prompt in streaming mode and returns a reactive stream of response chunks.
     * Prepends the system prompt to the user prompt before calling the NVIDIA API.
     *
     * @param prompt the user's code to explain
     * @return a Flux of String chunks representing the incremental response
     */
    @Override
    public Flux<String> processStreaming(String prompt) {
        logger.info("Processing streaming code explanation request");

        try {
            // Build request with system prompt prepended
            NvidiaRequest request = NvidiaRequest.builder()
                    .model(appConfig.getNvidiaModel())
                    .addSystemMessage(SYSTEM_PROMPT)
                    .addUserMessage(prompt)
                    .stream(true)
                    .build();

            // Call NVIDIA API streaming
            return nvidiaApiClient.sendRequestStreaming(request);

        } catch (Exception e) {
            logger.error("Error processing streaming code explanation request", e);
            return Flux.error(e);
        }
    }

    /**
     * Extracts content from the NVIDIA API response.
     * Handles the response structure and returns the generated text.
     *
     * @param response the NVIDIA API response
     * @return the extracted content string
     */
    private String extractContent(NvidiaResponse response) {
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            NvidiaResponse.Choice firstChoice = response.getChoices().get(0);
            if (firstChoice.getMessage() != null) {
                return firstChoice.getMessage().getContent();
            }
        }
        return "";
    }
}
