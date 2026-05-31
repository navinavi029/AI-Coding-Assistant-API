package com.assistant.multiagent.service;

import com.assistant.multiagent.model.AssistResponse;
import reactor.core.publisher.Flux;

/**
 * Interface defining the contract for all agent implementations.
 * Each agent has a specific system prompt and processes user prompts through the NVIDIA API.
 * 
 * Validates: Requirements 1.5, 8.4
 */
public interface Agent {

    /**
     * Processes a user prompt and returns a structured response.
     *
     * @param prompt the user's coding question or task
     * @return an AssistResponse containing the AI-generated content and metadata
     */
    AssistResponse process(String prompt);

    /**
     * Processes a user prompt in streaming mode and returns a reactive stream of response chunks.
     * Each chunk represents a partial response from the NVIDIA API.
     *
     * @param prompt the user's coding question or task
     * @return a Flux of String chunks representing the incremental response
     */
    Flux<String> processStreaming(String prompt);

    /**
     * Returns the agent-specific system prompt that defines the agent's behavior and expertise.
     * This prompt is prepended to user prompts when making NVIDIA API requests.
     *
     * @return the system prompt string for this agent
     */
    String getSystemPrompt();
}
