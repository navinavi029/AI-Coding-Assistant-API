package com.assistant.multiagent.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object representing a request to the assistant API.
 * This class encapsulates the user's prompt, the desired agent type,
 * and streaming preferences.
 * 
 * Validates: Requirements 4.1, 4.2, 4.3
 */
@Schema(description = "Request for coding assistance from an AI agent")
public class AssistRequest {

    @Schema(description = "The coding question or task to be processed", 
            example = "Write a Java function to calculate factorial",
            required = true)
    @NotBlank(message = "Prompt is required")
    private String prompt;

    @Schema(description = "Type of agent to handle the request", 
            example = "CODE_GENERATION",
            allowableValues = {"CODE_GENERATION", "CODE_REVIEW", "CODE_EXPLANATION"},
            required = true)
    @NotNull(message = "Agent type is required")
    private AgentType agentType;

    @Schema(description = "Enable streaming response via Server-Sent Events", 
            example = "false",
            defaultValue = "false")
    private boolean streaming = false;

    /**
     * Default constructor.
     */
    public AssistRequest() {
    }

    /**
     * Constructor with all fields.
     *
     * @param prompt the user's coding question or task
     * @param agentType the type of agent to handle the request
     * @param streaming whether to use streaming response mode
     */
    public AssistRequest(String prompt, AgentType agentType, boolean streaming) {
        this.prompt = prompt;
        this.agentType = agentType;
        this.streaming = streaming;
    }

    /**
     * Gets the user's prompt.
     *
     * @return the prompt string
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the user's prompt.
     *
     * @param prompt the prompt string
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * Gets the agent type.
     *
     * @return the agent type
     */
    public AgentType getAgentType() {
        return agentType;
    }

    /**
     * Sets the agent type.
     *
     * @param agentType the agent type
     */
    public void setAgentType(AgentType agentType) {
        this.agentType = agentType;
    }

    /**
     * Checks if streaming is enabled.
     *
     * @return true if streaming is enabled, false otherwise
     */
    public boolean isStreaming() {
        return streaming;
    }

    /**
     * Sets the streaming flag.
     *
     * @param streaming true to enable streaming, false otherwise
     */
    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }
}
