package com.assistant.multiagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Main application class for the Multi-Agent Coding Assistant.
 * Initializes the Spring Boot application and logs startup information.
 * 
 * Validates: Requirements 5.7, 6.6
 */
@SpringBootApplication
public class MultiAgentAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiAgentAssistantApplication.class, args);
    }

    /**
     * Component that logs startup information when the application is ready.
     */
    @Component
    static class StartupLogger {
        
        private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);
        
        private final Environment environment;
        
        public StartupLogger(Environment environment) {
            this.environment = environment;
        }
        
        /**
         * Logs startup information including server port and available endpoints.
         * Triggered when the application is fully started and ready to accept requests.
         */
        @EventListener(ApplicationReadyEvent.class)
        public void logStartupInfo() {
            String port = environment.getProperty("server.port", "8080");
            
            logger.info("=".repeat(60));
            logger.info("AI Coding Assistant API started successfully!");
            logger.info("=".repeat(60));
            logger.info("Server running on port: {}", port);
            logger.info("Available endpoints:");
            logger.info("  - POST http://localhost:{}/api/v1/assist", port);
            logger.info("  - POST http://localhost:{}/api/v1/assist (streaming)", port);
            logger.info("  - GET  http://localhost:{}/api/v1/health", port);
            logger.info("=".repeat(60));
        }
    }
}
