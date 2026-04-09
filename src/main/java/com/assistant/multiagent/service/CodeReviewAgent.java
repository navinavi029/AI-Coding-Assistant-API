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
 * Agent implementation specialized in code review.
 * Analyzes code for bugs, security issues, performance problems, and style violations.
 * 
 * Validates: Requirements 8.2, 8.4, 1.5
 */
@Service
public class CodeReviewAgent implements Agent {

    private static final Logger logger = LoggerFactory.getLogger(CodeReviewAgent.class);

    private static final String SYSTEM_PROMPT = 
        "You are an expert code review assistant. Analyze the provided code for bugs, security issues, " +
        "performance problems, and style violations. Provide specific, actionable feedback.";

    private final NvidiaApiClient nvidiaApiClient;
    private final AppConfig appConfig;

    /**
     * Constructs a new CodeReviewAgent with injected dependencies.
     *
     * @param nvidiaApiClient the NVIDIA API client
     * @param appConfig the application configuration
     */
    public CodeReviewAgent(NvidiaApiClient nvidiaApiClient, AppConfig appConfig) {
        this.nvidiaApiClient = nvidiaApiClient;
        this.appConfig = appConfig;
    }

    /**
     * Returns the system prompt for code review.
     *
     * @return the system prompt string
     */
    @Override
    public String getSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    /**
     * Processes a user prompt and returns a structured response.
     * Prepends the system prompt to the user prompt before calling the NVIDIA API.
     *
     * @param prompt the user's code to review
     * @param streaming whether to use streaming mode (ignored for this method)
     * @return an AssistResponse containing the code review feedback and metadata
     */
    @Override
    public AssistResponse process(String prompt, boolean streaming) {
        logger.info("Processing code review request - Streaming: {}", streaming);

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
                    .agentType(AgentType.CODE_REVIEW.name())
                    .timestamp(Instant.now().toString())
                    .addMetadata("model", nvidiaResponse.getModel())
                    .addMetadata("responseId", nvidiaResponse.getId())
                    .build();

        } catch (Exception e) {
            logger.error("Error processing code review request", e);
            return AssistResponse.builder()
                    .error("Failed to review code: " + e.getMessage())
                    .agentType(AgentType.CODE_REVIEW.name())
                    .timestamp(Instant.now().toString())
                    .build();
        }
    }

    /**
     * Processes a user prompt in streaming mode and returns a reactive stream of response chunks.
     * Prepends the system prompt to the user prompt before calling the NVIDIA API.
     *
     * @param prompt the user's code to review
     * @return a Flux of String chunks representing the incremental response
     */
    @Override
    public Flux<String> processStreaming(String prompt) {
        logger.info("Processing streaming code review request");

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
            logger.error("Error processing streaming code review request", e);
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
