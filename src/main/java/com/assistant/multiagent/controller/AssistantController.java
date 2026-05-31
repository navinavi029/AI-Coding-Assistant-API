package com.assistant.multiagent.controller;

import com.assistant.multiagent.model.AssistRequest;
import com.assistant.multiagent.model.AssistResponse;
import com.assistant.multiagent.service.AgentOrchestrator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import org.springframework.http.codec.ServerSentEvent;

/**
 * REST controller for the assistant API endpoints.
 * Handles both streaming and non-streaming requests for coding assistance.
 * 
 * Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 4.4
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Coding Assistant", description = "AI-powered coding assistance endpoints")
public class AssistantController {

    private static final Logger logger = LoggerFactory.getLogger(AssistantController.class);

    private final AgentOrchestrator agentOrchestrator;

    /**
     * Constructs a new AssistantController with the agent orchestrator injected.
     *
     * @param agentOrchestrator the orchestrator for routing requests to agents
     */
    public AssistantController(AgentOrchestrator agentOrchestrator) {
        this.agentOrchestrator = agentOrchestrator;
    }

    /**
     * Handles non-streaming assist requests.
     * Validates the request body and routes it to the appropriate agent via the orchestrator.
     *
     * @param request the assist request containing prompt, agent type, and streaming flag
     * @return ResponseEntity with HTTP 200 and AssistResponse JSON body
     */
    @Operation(
        summary = "Get coding assistance",
        description = "Submit a coding request to one of the specialized AI agents (CODE_GENERATION, CODE_REVIEW, or CODE_EXPLANATION)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(schema = @Schema(implementation = AssistResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request - missing or invalid fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "502", description = "External service error (NVIDIA API)"),
        @ApiResponse(responseCode = "503", description = "Service temporarily unavailable"),
        @ApiResponse(responseCode = "504", description = "Request timeout")
    })
    @PostMapping("/assist")
    public ResponseEntity<AssistResponse> assist(
            @Parameter(description = "Coding assistance request with prompt and agent type", required = true)
            @Valid @RequestBody AssistRequest request) {
        logger.info("Received assist request - agentType: {}, streaming: {}", 
                    request.getAgentType(), request.isStreaming());

        // If streaming is requested, this endpoint should not be used
        if (request.isStreaming()) {
            logger.warn("Streaming requested but non-streaming endpoint called");
        }

        AssistResponse response = agentOrchestrator.processRequest(request);
        
        logger.info("Completed assist request - agentType: {}", request.getAgentType());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Handles streaming assist requests using Server-Sent Events (SSE).
     * Validates the request body and routes it to the appropriate agent for streaming responses.
     * Each chunk of the response is emitted as a separate SSE event.
     *
     * @param request the assist request containing prompt, agent type, and streaming flag
     * @return Flux of ServerSentEvent containing response chunks and completion status
     */
    @Operation(
        summary = "Get streaming coding assistance",
        description = "Submit a coding request and receive real-time streaming responses via Server-Sent Events (SSE)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Streaming response started"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/assist", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> assistStreaming(
            @Parameter(description = "Coding assistance request with streaming enabled", required = true)
            @Valid @RequestBody AssistRequest request) {
        logger.info("Received streaming assist request - agentType: {}", request.getAgentType());

        return agentOrchestrator.processRequestStreaming(request)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(chunk)
                        .build())
                .onErrorResume(error -> {
                    logger.error("Error during streaming assist request - agentType: {}, error: {}",
                            request.getAgentType(), error.getMessage(), error);
                    return Flux.just(ServerSentEvent.<String>builder()
                            .event("error")
                            .data("{\"error\": \"" + error.getMessage() + "\"}")
                            .build());
                })
                .concatWith(Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("done")
                                .data("{\"status\": \"complete\"}")
                                .build()
                ))
                .doOnComplete(() -> logger.info("Completed streaming assist request - agentType: {}", 
                                                request.getAgentType()));
    }
}
