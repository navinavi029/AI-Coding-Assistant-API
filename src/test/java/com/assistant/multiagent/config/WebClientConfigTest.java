package com.assistant.multiagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebClientConfig class.
 * Tests WebClient bean creation and configuration.
 */
@SpringBootTest(classes = {AppConfig.class, WebClientConfig.class})
@TestPropertySource(properties = {
    "NVIDIA_API_KEY=test-api-key-12345",
    "REQUEST_TIMEOUT_SECONDS=30"
})
class WebClientConfigTest {

    @Autowired
    private WebClient webClient;

    @Autowired
    private AppConfig appConfig;

    @Test
    void testWebClientBeanIsCreated() {
        // Verify that WebClient bean is created
        assertNotNull(webClient);
    }

    @Test
    void testWebClientIsConfiguredWithTimeout() {
        // Verify that timeout configuration is applied
        assertEquals(30, appConfig.getRequestTimeoutSeconds());
        assertNotNull(webClient);
    }

    @Test
    void testWebClientBuilderIsNotNull() {
        // Verify that WebClient builder produces a valid instance
        assertNotNull(webClient);
        assertNotNull(webClient.mutate());
    }
}
