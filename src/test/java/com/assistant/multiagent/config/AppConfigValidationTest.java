package com.assistant.multiagent.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AppConfig validation behavior.
 * Verifies that the application fails fast when required configuration is missing.
 */
class AppConfigValidationTest {

    @Test
    void testApplicationFailsWhenApiKeyIsMissing() {
        // This test verifies that the Spring context fails to load when NVIDIA_API_KEY is missing
        // The @PostConstruct validation should throw IllegalStateException
        // Spring Boot will wrap this in a BeanCreationException
        
        Exception exception = assertThrows(Exception.class, () -> {
            // Attempt to create AppConfig with empty API key
            AppConfig config = new AppConfig();
            config.validateConfiguration();
        });
        
        // Verify the exception is IllegalStateException with appropriate message
        assertTrue(exception instanceof IllegalStateException);
        assertTrue(exception.getMessage().contains("NVIDIA_API_KEY"));
    }
}
