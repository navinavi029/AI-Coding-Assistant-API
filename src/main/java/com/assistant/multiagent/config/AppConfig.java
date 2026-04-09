package com.assistant.multiagent.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class that manages environment-based settings.
 * Loads configuration from environment variables with sensible defaults.
 * Validates required configuration at startup (fail-fast approach).
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${NVIDIA_API_KEY:}")
    private String nvidiaApiKey;

    @Value("${NVIDIA_API_URL:https://integrate.api.nvidia.com/v1/chat/completions}")
    private String nvidiaApiUrl;

    @Value("${NVIDIA_MODEL:google/gemma-4-31b-it}")
    private String nvidiaModel;

    @Value("${SERVER_PORT:8080}")
    private int serverPort;

    @Value("${REQUEST_TIMEOUT_SECONDS:60}")
    private int requestTimeoutSeconds;

    /**
     * Validates configuration after bean construction.
     * Fails fast if NVIDIA_API_KEY is missing.
     */
    @PostConstruct
    public void validateConfiguration() {
        if (nvidiaApiKey == null || nvidiaApiKey.trim().isEmpty()) {
            logger.error("NVIDIA_API_KEY environment variable is not set. Application cannot start.");
            throw new IllegalStateException("NVIDIA_API_KEY is required but not configured");
        }

        // Log configuration (excluding sensitive values)
        logger.info("Configuration loaded successfully:");
        logger.info("  NVIDIA_API_URL: {}", nvidiaApiUrl);
        logger.info("  NVIDIA_MODEL: {}", nvidiaModel);
        logger.info("  SERVER_PORT: {}", serverPort);
        logger.info("  REQUEST_TIMEOUT_SECONDS: {}", requestTimeoutSeconds);
        logger.info("  NVIDIA_API_KEY: [CONFIGURED]");
    }

    public String getNvidiaApiKey() {
        return nvidiaApiKey;
    }

    public String getNvidiaApiUrl() {
        return nvidiaApiUrl;
    }

    public String getNvidiaModel() {
        return nvidiaModel;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getRequestTimeoutSeconds() {
        return requestTimeoutSeconds;
    }
}
