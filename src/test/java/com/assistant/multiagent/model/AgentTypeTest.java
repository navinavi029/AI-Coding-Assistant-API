package com.assistant.multiagent.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentTypeTest {

    @Test
    void testEnumValues() {
        assertEquals("code_generation", AgentType.CODE_GENERATION.getValue());
        assertEquals("code_review", AgentType.CODE_REVIEW.getValue());
        assertEquals("code_explanation", AgentType.CODE_EXPLANATION.getValue());
    }

    @Test
    void testFromStringWithValidValues() {
        assertEquals(AgentType.CODE_GENERATION, AgentType.fromString("code_generation"));
        assertEquals(AgentType.CODE_REVIEW, AgentType.fromString("code_review"));
        assertEquals(AgentType.CODE_EXPLANATION, AgentType.fromString("code_explanation"));
    }

    @Test
    void testFromStringCaseInsensitive() {
        assertEquals(AgentType.CODE_GENERATION, AgentType.fromString("CODE_GENERATION"));
        assertEquals(AgentType.CODE_REVIEW, AgentType.fromString("Code_Review"));
        assertEquals(AgentType.CODE_EXPLANATION, AgentType.fromString("CODE_explanation"));
    }

    @Test
    void testFromStringWithInvalidValue() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AgentType.fromString("invalid_type")
        );
        assertEquals("Invalid agent type: invalid_type", exception.getMessage());
    }

    @Test
    void testFromStringWithNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AgentType.fromString(null)
        );
        assertEquals("Agent type value cannot be null", exception.getMessage());
    }

    @Test
    void testFromStringWithEmptyString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AgentType.fromString("")
        );
        assertEquals("Invalid agent type: ", exception.getMessage());
    }
}
