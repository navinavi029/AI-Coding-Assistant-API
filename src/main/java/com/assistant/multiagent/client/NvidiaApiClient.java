package com.assistant.multiagent.client;

import com.assistant.multiagent.config.AppConfig;
import com.assistant.multiagent.model.NvidiaRequest;
import com.assistant.multiagent.model.NvidiaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * Service class for communicating with the NVIDIA Gemini API.
 * Handles both streaming and non-streaming requests with comprehensive error handling.
 * 
 * Validates: Requirements 2.1, 2.3, 2.6, 7.1, 7.2, 7.3, 7.5
 */
@Service
public class NvidiaApiClient {

    private static final Logger logger = LoggerFactory.getLogger(NvidiaApiClient.class);

    private final WebClient webClient;
    private final AppConfig appConfig;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new NvidiaApiClient with injected dependencies.
     *
     * @param webClient the configured WebClient instance
     * @param appConfig the application configuration
     */
    public NvidiaApiClient(WebClient webClient, AppConfig appConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sends a non-streaming request to the NVIDIA API.
     * Constructs HTTP POST request with Authorization header and request body.
     * Parses response into NvidiaResponse object.
     *
     * @param request the NVIDIA API request
     * @return the parsed NVIDIA API response
     * @throws NvidiaApiException if network error, timeout, or API error occurs
     */
    public NvidiaResponse sendRequest(NvidiaRequest request) {
        logger.info("Sending non-streaming request to NVIDIA API - Model: {}", request.getModel());
        logger.debug("Request details - MaxTokens: {}, Temperature: {}, TopP: {}, Stream: {}", 
            request.getMaxTokens(), request.getTemperature(), request.getTopP(), request.isStream());
        
        // Log the actual JSON being sent
        try {
            String jsonPayload = objectMapper.writeValueAsString(request);
            logger.info("JSON Payload being sent to NVIDIA API: {}", jsonPayload);
        } catch (Exception e) {
            logger.warn("Failed to serialize request for logging", e);
        }
        
        try {
            NvidiaResponse response = webClient.post()
                    .uri(appConfig.getNvidiaApiUrl())
                    .header("Authorization", "Bearer " + appConfig.getNvidiaApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> 
                        clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                int statusCode = clientResponse.statusCode().value();
                                logger.error("NVIDIA API returned error - Status: {}, Body: {}", statusCode, errorBody);
                                return Mono.error(new NvidiaApiException(
                                    "NVIDIA API error", 
                                    statusCode, 
                                    errorBody
                                ));
                            })
                    )
                    .bodyToMono(NvidiaResponse.class)
                    .timeout(Duration.ofSeconds(appConfig.getRequestTimeoutSeconds()))
                    .block();

            logger.info("Received response from NVIDIA API - ID: {}, Model: {}", 
                response.getId(), response.getModel());
            
            return response;

        } catch (WebClientRequestException e) {
            // Network errors (connection refused, DNS failure, etc.)
            logger.error("Network error communicating with NVIDIA API", e);
            throw new NvidiaApiException("Service temporarily unavailable", 503, e);
            
        } catch (NvidiaApiException e) {
            // Already wrapped, just rethrow
            throw e;
            
        } catch (Exception e) {
            // Timeout or other unexpected errors
            if (e.getCause() instanceof TimeoutException || e.getMessage().contains("timeout")) {
                logger.warn("Request to NVIDIA API timed out");
                throw new NvidiaApiException("Request timeout", 504, e);
            }
            
            logger.error("Unexpected error during NVIDIA API request", e);
            throw new NvidiaApiException("Internal server error", 500, e);
        }
    }

    /**
     * Sends a streaming request to the NVIDIA API.
     * Returns a Flux that emits response chunks as they arrive.
     * Automatically sets the stream parameter to true in the request.
     *
     * @param request the NVIDIA API request with streaming enabled
     * @return a Flux of response chunks
     */
    public Flux<String> sendRequestStreaming(NvidiaRequest request) {
        // Ensure stream parameter is set to true for streaming requests (Requirement 9.1)
        request.setStream(true);
        
        logger.info("Sending streaming request to NVIDIA API - Model: {}", request.getModel());
        
        return webClient.post()
                .uri(appConfig.getNvidiaApiUrl())
                .header("Authorization", "Bearer " + appConfig.getNvidiaApiKey())
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> 
                    clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            int statusCode = clientResponse.statusCode().value();
                            logger.error("NVIDIA API returned error during streaming - Status: {}, Body: {}", 
                                statusCode, errorBody);
                            return Mono.error(new NvidiaApiException(
                                "NVIDIA API error", 
                                statusCode, 
                                errorBody
                            ));
                        })
                )
                .bodyToFlux(String.class)
                .timeout(Duration.ofSeconds(appConfig.getRequestTimeoutSeconds()))
                .doOnNext(chunk -> logger.debug("Received streaming chunk from NVIDIA API"))
                .doOnComplete(() -> logger.info("Streaming response completed"))
                .doOnError(e -> {
                    if (e instanceof WebClientRequestException) {
                        logger.error("Network error during streaming", e);
                    } else if (e.getCause() instanceof TimeoutException || e.getMessage().contains("timeout")) {
                        logger.warn("Streaming request timed out");
                    } else {
                        logger.error("Error during streaming", e);
                    }
                })
                .onErrorMap(WebClientRequestException.class, e -> 
                    new NvidiaApiException("Service temporarily unavailable", 503, e)
                )
                .onErrorMap(NvidiaApiException.class, e -> e)
                .onErrorMap(e -> {
                    if (e.getCause() instanceof TimeoutException || 
                        (e.getMessage() != null && e.getMessage().contains("timeout"))) {
                        return new NvidiaApiException("Request timeout", 504, e);
                    }
                    return new NvidiaApiException("Internal server error", 500, e);
                });
    }
}
