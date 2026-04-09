package com.assistant.multiagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AppConfig class.
 * Tests configuration loading, defaults, and validation.
 */
@SpringBootTest(classes = AppConfig.class)
@TestPropertySource(properties = {
    "NVIDIA_API_KEY=test-api-key-12345"
})
class AppConfigTest {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testConfigurationLoadsWithDefaults() {
        // Verify that default values are loaded correctly
        assertEquals("https://integrate.api.nvidia.com/v1/chat/completions", appConfig.getNvidiaApiUrl());
        assertEquals("google/gemma-4-31b-it", appConfig.getNvidiaModel());
        assertEquals(8080, appConfig.getServerPort());
        assertEquals(60, appConfig.getRequestTimeoutSeconds());
    }

    @Test
    void testApiKeyIsLoaded() {
        // Verify that the API key is loaded from test properties
        assertEquals("test-api-key-12345", appConfig.getNvidiaApiKey());
    }

    @Test
    void testGettersReturnCorrectValues() {
        // Verify all getters work correctly
        assertNotNull(appConfig.getNvidiaApiKey());
        assertNotNull(appConfig.getNvidiaApiUrl());
        assertNotNull(appConfig.getNvidiaModel());
        assertTrue(appConfig.getServerPort() > 0);
        assertTrue(appConfig.getRequestTimeoutSeconds() > 0);
    }
}
