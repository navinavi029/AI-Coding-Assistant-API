package com.assistant.multiagent.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AssistResponse DTO.
 */
class AssistResponseTest {

    @Test
    void testDefaultConstructor() {
        AssistResponse response = new AssistResponse();
        
        assertNull(response.getContent());
        assertNull(response.getAgentType());
        assertNull(response.getTimestamp());
        assertNull(response.getError());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        AssistResponse response = new AssistResponse();
        
        response.setContent("Generated code");
        response.setAgentType("code_generation");
        response.setTimestamp("2024-01-15T10:30:00Z");
        response.setError("Test error");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("model", "google/gemma-4-31b-it");
        response.setMetadata(metadata);
        
        assertEquals("Generated code", response.getContent());
        assertEquals("code_generation", response.getAgentType());
        assertEquals("2024-01-15T10:30:00Z", response.getTimestamp());
        assertEquals("Test error", response.getError());
        assertEquals(1, response.getMetadata().size());
        assertEquals("google/gemma-4-31b-it", response.getMetadata().get("model"));
    }

    @Test
    void testBuilderPattern() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("processingTimeMs", 1234L);
        
        AssistResponse response = AssistResponse.builder()
                .content("Test content")
                .agentType("code_review")
                .timestamp("2024-01-15T10:30:00Z")
                .error(null)
                .metadata(metadata)
                .build();
        
        assertEquals("Test content", response.getContent());
        assertEquals("code_review", response.getAgentType());
        assertEquals("2024-01-15T10:30:00Z", response.getTimestamp());
        assertNull(response.getError());
        assertEquals(1, response.getMetadata().size());
        assertEquals(1234L, response.getMetadata().get("processingTimeMs"));
    }

    @Test
    void testBuilderWithAddMetadata() {
        AssistResponse response = AssistResponse.builder()
                .content("Explanation text")
                .agentType("code_explanation")
                .timestamp("2024-01-15T10:30:00Z")
                .addMetadata("model", "google/gemma-4-31b-it")
                .addMetadata("processingTimeMs", 2000L)
                .build();
        
        assertEquals("Explanation text", response.getContent());
        assertEquals("code_explanation", response.getAgentType());
        assertEquals("2024-01-15T10:30:00Z", response.getTimestamp());
        assertNull(response.getError());
        assertEquals(2, response.getMetadata().size());
        assertEquals("google/gemma-4-31b-it", response.getMetadata().get("model"));
        assertEquals(2000L, response.getMetadata().get("processingTimeMs"));
    }

    @Test
    void testBuilderWithError() {
        AssistResponse response = AssistResponse.builder()
                .content(null)
                .agentType("code_generation")
                .timestamp("2024-01-15T10:30:00Z")
                .error("Service temporarily unavailable")
                .build();
        
        assertNull(response.getContent());
        assertEquals("code_generation", response.getAgentType());
        assertEquals("2024-01-15T10:30:00Z", response.getTimestamp());
        assertEquals("Service temporarily unavailable", response.getError());
        assertTrue(response.getMetadata().isEmpty());
    }

    @Test
    void testBuilderChaining() {
        AssistResponse response = AssistResponse.builder()
                .content("Code snippet")
                .agentType("code_generation")
                .timestamp("2024-01-15T10:30:00Z")
                .addMetadata("key1", "value1")
                .addMetadata("key2", 123)
                .addMetadata("key3", true)
                .build();
        
        assertEquals(3, response.getMetadata().size());
        assertEquals("value1", response.getMetadata().get("key1"));
        assertEquals(123, response.getMetadata().get("key2"));
        assertEquals(true, response.getMetadata().get("key3"));
    }

    @Test
    void testTimestampFormat() {
        // Test ISO 8601 format
        String isoTimestamp = "2024-01-15T10:30:00Z";
        AssistResponse response = AssistResponse.builder()
                .timestamp(isoTimestamp)
                .build();
        
        assertEquals(isoTimestamp, response.getTimestamp());
    }

    @Test
    void testMetadataImmutability() {
        Map<String, Object> originalMetadata = new HashMap<>();
        originalMetadata.put("key", "value");
        
        AssistResponse response = AssistResponse.builder()
                .metadata(originalMetadata)
                .build();
        
        // Modify original map
        originalMetadata.put("newKey", "newValue");
        
        // Response should have the reference to the same map
        assertEquals(2, response.getMetadata().size());
    }

    @Test
    void testEmptyBuilder() {
        AssistResponse response = AssistResponse.builder().build();
        
        assertNull(response.getContent());
        assertNull(response.getAgentType());
        assertNull(response.getTimestamp());
        assertNull(response.getError());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }
}
