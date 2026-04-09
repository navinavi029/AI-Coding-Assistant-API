package com.assistant.multiagent.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AssistRequest DTO.
 * Tests validation annotations and basic functionality.
 */
class AssistRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        AssistRequest request = new AssistRequest(
            "Write a Java function",
            AgentType.CODE_GENERATION,
            false
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid request should have no violations");
    }

    @Test
    void testPromptCannotBeNull() {
        AssistRequest request = new AssistRequest(
            null,
            AgentType.CODE_GENERATION,
            false
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Prompt is required", violations.iterator().next().getMessage());
    }

    @Test
    void testPromptCannotBeEmpty() {
        AssistRequest request = new AssistRequest(
            "",
            AgentType.CODE_GENERATION,
            false
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Prompt is required", violations.iterator().next().getMessage());
    }

    @Test
    void testPromptCannotBeBlank() {
        AssistRequest request = new AssistRequest(
            "   ",
            AgentType.CODE_GENERATION,
            false
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Prompt is required", violations.iterator().next().getMessage());
    }

    @Test
    void testAgentTypeCannotBeNull() {
        AssistRequest request = new AssistRequest(
            "Write a Java function",
            null,
            false
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Agent type is required", violations.iterator().next().getMessage());
    }

    @Test
    void testStreamingDefaultsToFalse() {
        AssistRequest request = new AssistRequest();
        assertFalse(request.isStreaming(), "Streaming should default to false");
    }

    @Test
    void testGettersAndSetters() {
        AssistRequest request = new AssistRequest();
        
        request.setPrompt("Test prompt");
        assertEquals("Test prompt", request.getPrompt());
        
        request.setAgentType(AgentType.CODE_REVIEW);
        assertEquals(AgentType.CODE_REVIEW, request.getAgentType());
        
        request.setStreaming(true);
        assertTrue(request.isStreaming());
    }

    @Test
    void testAllAgentTypesAreValid() {
        for (AgentType type : AgentType.values()) {
            AssistRequest request = new AssistRequest(
                "Test prompt",
                type,
                false
            );

            Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
            assertTrue(violations.isEmpty(), 
                "Request with agent type " + type + " should be valid");
        }
    }

    @Test
    void testStreamingCanBeTrue() {
        AssistRequest request = new AssistRequest(
            "Test prompt",
            AgentType.CODE_EXPLANATION,
            true
        );

        Set<ConstraintViolation<AssistRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertTrue(request.isStreaming());
    }
}
