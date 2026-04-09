package com.assistant.multiagent.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NvidiaResponse DTO.
 * Tests the structure, getters, setters, and nested classes.
 */
class NvidiaResponseTest {

    @Test
    void testDefaultConstructor() {
        NvidiaResponse response = new NvidiaResponse();
        
        assertNotNull(response);
        assertNotNull(response.getChoices());
        assertTrue(response.getChoices().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        NvidiaResponse response = new NvidiaResponse();
        
        response.setId("chatcmpl-123");
        response.setObject("chat.completion");
        response.setCreated(1234567890L);
        response.setModel("google/gemma-4-31b-it");
        
        assertEquals("chatcmpl-123", response.getId());
        assertEquals("chat.completion", response.getObject());
        assertEquals(1234567890L, response.getCreated());
        assertEquals("google/gemma-4-31b-it", response.getModel());
    }

    @Test
    void testChoicesListManipulation() {
        NvidiaResponse response = new NvidiaResponse();
        List<NvidiaResponse.Choice> choices = new ArrayList<>();
        
        NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "Test content");
        NvidiaResponse.Choice choice = new NvidiaResponse.Choice(0, message, "stop");
        choices.add(choice);
        
        response.setChoices(choices);
        
        assertEquals(1, response.getChoices().size());
        assertEquals(0, response.getChoices().get(0).getIndex());
        assertEquals("Test content", response.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void testChoiceDefaultConstructor() {
        NvidiaResponse.Choice choice = new NvidiaResponse.Choice();
        
        assertNotNull(choice);
        assertEquals(0, choice.getIndex());
        assertNull(choice.getMessage());
        assertNull(choice.getFinishReason());
    }

    @Test
    void testChoiceConstructorWithAllFields() {
        NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "Generated code");
        NvidiaResponse.Choice choice = new NvidiaResponse.Choice(0, message, "stop");
        
        assertEquals(0, choice.getIndex());
        assertNotNull(choice.getMessage());
        assertEquals("assistant", choice.getMessage().getRole());
        assertEquals("Generated code", choice.getMessage().getContent());
        assertEquals("stop", choice.getFinishReason());
    }

    @Test
    void testChoiceGettersAndSetters() {
        NvidiaResponse.Choice choice = new NvidiaResponse.Choice();
        NvidiaResponse.Message message = new NvidiaResponse.Message();
        
        choice.setIndex(1);
        choice.setMessage(message);
        choice.setFinishReason("length");
        
        assertEquals(1, choice.getIndex());
        assertSame(message, choice.getMessage());
        assertEquals("length", choice.getFinishReason());
    }

    @Test
    void testMessageDefaultConstructor() {
        NvidiaResponse.Message message = new NvidiaResponse.Message();
        
        assertNotNull(message);
        assertNull(message.getRole());
        assertNull(message.getContent());
    }

    @Test
    void testMessageConstructorWithAllFields() {
        NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "Hello world");
        
        assertEquals("assistant", message.getRole());
        assertEquals("Hello world", message.getContent());
    }

    @Test
    void testMessageGettersAndSetters() {
        NvidiaResponse.Message message = new NvidiaResponse.Message();
        
        message.setRole("assistant");
        message.setContent("Test response");
        
        assertEquals("assistant", message.getRole());
        assertEquals("Test response", message.getContent());
    }

    @Test
    void testCompleteResponseStructure() {
        // Create a complete response structure
        NvidiaResponse response = new NvidiaResponse();
        response.setId("chatcmpl-456");
        response.setObject("chat.completion");
        response.setCreated(1234567890L);
        response.setModel("google/gemma-4-31b-it");
        
        NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "public class Test {}");
        NvidiaResponse.Choice choice = new NvidiaResponse.Choice(0, message, "stop");
        
        List<NvidiaResponse.Choice> choices = new ArrayList<>();
        choices.add(choice);
        response.setChoices(choices);
        
        // Verify the complete structure
        assertEquals("chatcmpl-456", response.getId());
        assertEquals("chat.completion", response.getObject());
        assertEquals(1234567890L, response.getCreated());
        assertEquals("google/gemma-4-31b-it", response.getModel());
        assertEquals(1, response.getChoices().size());
        
        NvidiaResponse.Choice retrievedChoice = response.getChoices().get(0);
        assertEquals(0, retrievedChoice.getIndex());
        assertEquals("stop", retrievedChoice.getFinishReason());
        assertEquals("assistant", retrievedChoice.getMessage().getRole());
        assertEquals("public class Test {}", retrievedChoice.getMessage().getContent());
    }

    @Test
    void testMultipleChoices() {
        NvidiaResponse response = new NvidiaResponse();
        List<NvidiaResponse.Choice> choices = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "Content " + i);
            NvidiaResponse.Choice choice = new NvidiaResponse.Choice(i, message, "stop");
            choices.add(choice);
        }
        
        response.setChoices(choices);
        
        assertEquals(3, response.getChoices().size());
        for (int i = 0; i < 3; i++) {
            assertEquals(i, response.getChoices().get(i).getIndex());
            assertEquals("Content " + i, response.getChoices().get(i).getMessage().getContent());
        }
    }

    @Test
    void testEmptyContent() {
        NvidiaResponse.Message message = new NvidiaResponse.Message("assistant", "");
        
        assertEquals("assistant", message.getRole());
        assertEquals("", message.getContent());
        assertNotNull(message.getContent());
    }

    @Test
    void testNullValues() {
        NvidiaResponse response = new NvidiaResponse();
        
        response.setId(null);
        response.setObject(null);
        response.setModel(null);
        
        assertNull(response.getId());
        assertNull(response.getObject());
        assertNull(response.getModel());
    }
}
