package com.assistant.multiagent.service;

import com.assistant.multiagent.model.AgentType;
import com.assistant.multiagent.model.AssistRequest;
import com.assistant.multiagent.model.AssistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.EnumMap;
import java.util.Map;

/**
 * Service class that orchestrates routing of requests to appropriate agents.
 * This class acts as a central dispatcher, selecting the correct agent based on
 * the AgentType specified in the request.
 * 
 * Validates: Requirements 1.1, 1.3, 8.5, 8.6
 */
@Service
public class AgentOrchestrator {

    private static final Logger logger = LoggerFactory.getLogger(AgentOrchestrator.class);

    private final Map<AgentType, Agent> agents;

    /**
     * Constructs a new AgentOrchestrator with all agent implementations injected.
     * Creates a mapping between AgentType enum values and their corresponding agent implementations.
     *
     * @param codeGenerationAgent the code generation agent
     * @param codeReviewAgent the code review agent
     * @param codeExplanationAgent the code explanation agent
     */
    public AgentOrchestrator(
            CodeGenerationAgent codeGenerationAgent,
            CodeReviewAgent codeReviewAgent,
            CodeExplanationAgent codeExplanationAgent) {
        
        this.agents = new EnumMap<>(AgentType.class);
        this.agents.put(AgentType.CODE_GENERATION, codeGenerationAgent);
        this.agents.put(AgentType.CODE_REVIEW, codeReviewAgent);
        this.agents.put(AgentType.CODE_EXPLANATION, codeExplanationAgent);

        logger.info("AgentOrchestrator initialized with {} agents", agents.size());
    }

    /**
     * Processes a request by routing it to the appropriate agent based on the agent type.
     * This method handles non-streaming requests.
     *
     * @param request the assist request containing prompt, agent type, and streaming flag
     * @return an AssistResponse containing the AI-generated content and metadata
     * @throws IllegalArgumentException if the agent type is invalid or not found
     */
    public AssistResponse processRequest(AssistRequest request) {
        AgentType agentType = request.getAgentType();
        logger.info("Processing request with agent type: {}", agentType);

        Agent agent = agents.get(agentType);
        if (agent == null) {
            logger.error("Invalid agent type: {}", agentType);
            throw new IllegalArgumentException("Invalid agent type: " + agentType);
        }

        return agent.process(request.getPrompt(), request.isStreaming());
    }

    /**
     * Processes a request by routing it to the appropriate agent for streaming responses.
     * This method handles streaming requests and returns a reactive stream of response chunks.
     *
     * @param request the assist request containing prompt, agent type, and streaming flag
     * @return a Flux of String chunks representing the incremental response
     * @throws IllegalArgumentException if the agent type is invalid or not found
     */
    public Flux<String> processRequestStreaming(AssistRequest request) {
        AgentType agentType = request.getAgentType();
        logger.info("Processing streaming request with agent type: {}", agentType);

        Agent agent = agents.get(agentType);
        if (agent == null) {
            logger.error("Invalid agent type: {}", agentType);
            return Flux.error(new IllegalArgumentException("Invalid agent type: " + agentType));
        }

        return agent.processStreaming(request.getPrompt());
    }
}
