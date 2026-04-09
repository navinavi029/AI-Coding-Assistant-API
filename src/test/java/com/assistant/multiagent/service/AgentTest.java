package com.assistant.multiagent.service;

import com.assistant.multiagent.model.AssistResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Agent interface contract.
 * Tests verify that agent implementations adhere to the expected behavior.
 * 
 * Validates: Requirements 1.5, 8.4
 */
class AgentTest {

    /**
     * Mock implementation of Agent for testing purposes.
     */
    private static class MockAgent implements Agent {
        
        private final String systemPrompt;
        private final String responseContent;
        
        public MockAgent(String systemPrompt, String responseContent) {
            this.systemPrompt = systemPrompt;
            this.responseContent = responseContent;
        }

        @Override
        public AssistResponse process(String prompt, boolean streaming) {
            return AssistResponse.builder()
                    .content(responseContent)
                    .agentType("mock")
                    .timestamp("2024-01-15T10:30:00Z")
                    .build();
        }

        @Override
        public Flux<String> processStreaming(String prompt) {
            return Flux.just("chunk1", "chunk2", "chunk3");
        }

        @Override
        public String getSystemPrompt() {
            return systemPrompt;
        }
    }

    @Test
    void testProcessReturnsAssistResponse() {
        // Arrange
        Agent agent = new MockAgent("Test system prompt", "Test response");
        
        // Act
        AssistResponse response = agent.process("Test prompt", false);
        
        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals("Test response", response.getContent());
        assertEquals("mock", response.getAgentType());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testProcessStreamingReturnsFlux() {
        // Arrange
        Agent agent = new MockAgent("Test system prompt", "Test response");
        
        // Act
        Flux<String> stream = agent.processStreaming("Test prompt");
        
        // Assert
        assertNotNull(stream, "Stream should not be null");
        StepVerifier.create(stream)
                .expectNext("chunk1")
                .expectNext("chunk2")
                .expectNext("chunk3")
                .verifyComplete();
    }

    @Test
    void testGetSystemPromptReturnsString() {
        // Arrange
        String expectedPrompt = "You are a test agent";
        Agent agent = new MockAgent(expectedPrompt, "Test response");
        
        // Act
        String systemPrompt = agent.getSystemPrompt();
        
        // Assert
        assertNotNull(systemPrompt, "System prompt should not be null");
        assertEquals(expectedPrompt, systemPrompt);
    }

    @Test
    void testProcessWithStreamingFlag() {
        // Arrange
        Agent agent = new MockAgent("Test system prompt", "Test response");
        
        // Act
        AssistResponse nonStreamingResponse = agent.process("Test prompt", false);
        AssistResponse streamingResponse = agent.process("Test prompt", true);
        
        // Assert
        assertNotNull(nonStreamingResponse);
        assertNotNull(streamingResponse);
        // Both should return valid responses regardless of streaming flag
        assertEquals("Test response", nonStreamingResponse.getContent());
        assertEquals("Test response", streamingResponse.getContent());
    }

    @Test
    void testProcessStreamingWithEmptyPrompt() {
        // Arrange
        Agent agent = new MockAgent("Test system prompt", "Test response");
        
        // Act
        Flux<String> stream = agent.processStreaming("");
        
        // Assert
        assertNotNull(stream, "Stream should not be null even with empty prompt");
        StepVerifier.create(stream)
                .expectNext("chunk1")
                .expectNext("chunk2")
                .expectNext("chunk3")
                .verifyComplete();
    }

    @Test
    void testSystemPromptIsConsistent() {
        // Arrange
        String expectedPrompt = "Consistent system prompt";
        Agent agent = new MockAgent(expectedPrompt, "Test response");
        
        // Act
        String prompt1 = agent.getSystemPrompt();
        String prompt2 = agent.getSystemPrompt();
        
        // Assert
        assertEquals(prompt1, prompt2, "System prompt should be consistent across calls");
    }
}
