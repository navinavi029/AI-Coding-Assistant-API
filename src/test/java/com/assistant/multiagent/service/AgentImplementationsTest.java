package com.assistant.multiagent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for agent implementations.
 * Verifies that each agent has the correct system prompt and basic functionality.
 * 
 * Validates: Requirements 8.1, 8.2, 8.3, 8.4
 */
class AgentImplementationsTest {

    private CodeGenerationAgent codeGenerationAgent;
    private CodeReviewAgent codeReviewAgent;
    private CodeExplanationAgent codeExplanationAgent;

    @BeforeEach
    void setUp() {
        // Create agents with null dependencies since we're only testing system prompts
        codeGenerationAgent = new CodeGenerationAgent(null, null);
        codeReviewAgent = new CodeReviewAgent(null, null);
        codeExplanationAgent = new CodeExplanationAgent(null, null);
    }

    @Test
    void testCodeGenerationAgentSystemPrompt() {
        // Act
        String systemPrompt = codeGenerationAgent.getSystemPrompt();

        // Assert
        assertNotNull(systemPrompt, "System prompt should not be null");
        assertTrue(systemPrompt.contains("code generation assistant"), 
                "System prompt should identify as code generation assistant");
        assertTrue(systemPrompt.contains("clean, efficient, and well-documented code"),
                "System prompt should mention code quality expectations");
    }

    @Test
    void testCodeReviewAgentSystemPrompt() {
        // Act
        String systemPrompt = codeReviewAgent.getSystemPrompt();

        // Assert
        assertNotNull(systemPrompt, "System prompt should not be null");
        assertTrue(systemPrompt.contains("code review assistant"),
                "System prompt should identify as code review assistant");
        assertTrue(systemPrompt.contains("bugs, security issues"),
                "System prompt should mention bug detection");
        assertTrue(systemPrompt.contains("actionable feedback"),
                "System prompt should mention actionable feedback");
    }

    @Test
    void testCodeExplanationAgentSystemPrompt() {
        // Act
        String systemPrompt = codeExplanationAgent.getSystemPrompt();

        // Assert
        assertNotNull(systemPrompt, "System prompt should not be null");
        assertTrue(systemPrompt.contains("code explanation assistant"),
                "System prompt should identify as code explanation assistant");
        assertTrue(systemPrompt.contains("clearly and concisely"),
                "System prompt should mention clarity");
        assertTrue(systemPrompt.contains("basic programming knowledge"),
                "System prompt should mention target audience");
    }

    @Test
    void testAllAgentsImplementAgentInterface() {
        // Assert
        assertTrue(codeGenerationAgent instanceof Agent,
                "CodeGenerationAgent should implement Agent interface");
        assertTrue(codeReviewAgent instanceof Agent,
                "CodeReviewAgent should implement Agent interface");
        assertTrue(codeExplanationAgent instanceof Agent,
                "CodeExplanationAgent should implement Agent interface");
    }

    @Test
    void testSystemPromptsAreDistinct() {
        // Act
        String genPrompt = codeGenerationAgent.getSystemPrompt();
        String reviewPrompt = codeReviewAgent.getSystemPrompt();
        String explainPrompt = codeExplanationAgent.getSystemPrompt();

        // Assert
        assertNotEquals(genPrompt, reviewPrompt,
                "Code generation and review prompts should be different");
        assertNotEquals(genPrompt, explainPrompt,
                "Code generation and explanation prompts should be different");
        assertNotEquals(reviewPrompt, explainPrompt,
                "Code review and explanation prompts should be different");
    }

    @Test
    void testSystemPromptsAreConsistent() {
        // Act
        String prompt1 = codeGenerationAgent.getSystemPrompt();
        String prompt2 = codeGenerationAgent.getSystemPrompt();

        // Assert
        assertEquals(prompt1, prompt2,
                "System prompt should be consistent across multiple calls");
    }
}
