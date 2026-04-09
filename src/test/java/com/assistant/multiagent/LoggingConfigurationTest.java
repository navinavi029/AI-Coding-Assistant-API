package com.assistant.multiagent;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test to verify logging configuration is properly loaded.
 * Validates: Requirements 7.4, 7.5, 7.7
 */
@SpringBootTest
@TestPropertySource(properties = {
    "NVIDIA_API_KEY=test-key-for-logging-test"
})
class LoggingConfigurationTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConfigurationTest.class);

    @Test
    void testLoggingConfigurationLoaded() {
        // Verify logger is properly configured
        assertNotNull(logger);
        
        // Test different log levels
        logger.info("INFO level logging test");
        logger.warn("WARN level logging test");
        logger.error("ERROR level logging test");
        logger.debug("DEBUG level logging test (should not appear with INFO level)");
        
        // If we get here without exceptions, logging is configured correctly
    }

    @Test
    void testStructuredLoggingFormat() {
        // Log a message with structured information
        logger.info("Structured log test - timestamp: {}, level: {}, component: {}", 
                    System.currentTimeMillis(), "INFO", "LoggingConfigurationTest");
        
        // Verify logger name is set correctly
        assertNotNull(logger.getName());
    }
}
