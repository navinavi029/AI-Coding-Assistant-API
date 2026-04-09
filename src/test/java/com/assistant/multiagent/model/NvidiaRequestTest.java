package com.assistant.multiagent.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NvidiaRequest DTO.
 * Tests the builder pattern, nested Message class, and field accessors.
 */
class NvidiaRequestTest {

    @Test
    void testDefaultConstructor() {
        NvidiaRequest request = new NvidiaRequest();
        
        assertNotNull(request.getMessages());
        assertTrue(request.getMessages().isEmpty());
        assertEquals(0.7, request.getTemperature());
        assertEquals(2048, request.getMaxTokens());
        assertFalse(request.isStream());
        assertFalse(request.isEnableThinking());
    }

    @Test
    void testBuilderWithAllFields() {
        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addSystemMessage("You are a helpful assistant")
                .addUserMessage("Write a hello world program")
                .stream(true)
                .enableThinking(true)
                .temperature(0.8)
                .maxTokens(1024)
                .build();

        assertEquals("google/gemma-4-31b-it", request.getModel());
        assertEquals(2, request.getMessages().size());
        assertTrue(request.isStream());
        assertTrue(request.isEnableThinking());
        assertEquals(0.8, request.getTemperature());
        assertEquals(1024, request.getMaxTokens());
    }

    @Test
    void testBuilderWithDefaultValues() {
        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Test prompt")
                .build();

        assertEquals("google/gemma-4-31b-it", request.getModel());
        assertEquals(1, request.getMessages().size());
        assertFalse(request.isStream());
        assertFalse(request.isEnableThinking());
        assertEquals(0.7, request.getTemperature());
        assertEquals(2048, request.getMaxTokens());
    }

    @Test
    void testAddSystemMessage() {
        NvidiaRequest request = NvidiaRequest.builder()
                .addSystemMessage("System prompt")
                .build();

        assertEquals(1, request.getMessages().size());
        NvidiaRequest.Message message = request.getMessages().get(0);
        assertEquals("system", message.getRole());
        assertEquals("System prompt", message.getContent());
    }

    @Test
    void testAddUserMessage() {
        NvidiaRequest request = NvidiaRequest.builder()
                .addUserMessage("User prompt")
                .build();

        assertEquals(1, request.getMessages().size());
        NvidiaRequest.Message message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertEquals("User prompt", message.getContent());
    }

    @Test
    void testMultipleMessages() {
        NvidiaRequest request = NvidiaRequest.builder()
                .addSystemMessage("System instruction")
                .addUserMessage("First question")
                .addUserMessage("Second question")
                .build();

        assertEquals(3, request.getMessages().size());
        assertEquals("system", request.getMessages().get(0).getRole());
        assertEquals("user", request.getMessages().get(1).getRole());
        assertEquals("user", request.getMessages().get(2).getRole());
    }

    @Test
    void testMessageConstructor() {
        NvidiaRequest.Message message = new NvidiaRequest.Message("system", "Test content");
        
        assertEquals("system", message.getRole());
        assertEquals("Test content", message.getContent());
    }

    @Test
    void testMessageSetters() {
        NvidiaRequest.Message message = new NvidiaRequest.Message();
        message.setRole("user");
        message.setContent("Test content");
        
        assertEquals("user", message.getRole());
        assertEquals("Test content", message.getContent());
    }

    @Test
    void testSetters() {
        NvidiaRequest request = new NvidiaRequest();
        request.setModel("test-model");
        request.setStream(true);
        request.setEnableThinking(true);
        request.setTemperature(0.5);
        request.setMaxTokens(512);

        assertEquals("test-model", request.getModel());
        assertTrue(request.isStream());
        assertTrue(request.isEnableThinking());
        assertEquals(0.5, request.getTemperature());
        assertEquals(512, request.getMaxTokens());
    }

    @Test
    void testBuilderChaining() {
        NvidiaRequest.Builder builder = NvidiaRequest.builder();
        NvidiaRequest request = builder
                .model("model-1")
                .stream(true)
                .enableThinking(false)
                .temperature(0.9)
                .maxTokens(4096)
                .addSystemMessage("System")
                .addUserMessage("User")
                .build();

        assertNotNull(request);
        assertEquals("model-1", request.getModel());
        assertTrue(request.isStream());
        assertFalse(request.isEnableThinking());
        assertEquals(0.9, request.getTemperature());
        assertEquals(4096, request.getMaxTokens());
        assertEquals(2, request.getMessages().size());
    }
}
