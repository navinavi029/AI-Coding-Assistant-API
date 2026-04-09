package com.assistant.multiagent.model;

/**
 * Enum representing the types of agents available in the multi-agent system.
 * Each agent type corresponds to a specialized coding assistance capability.
 */
public enum AgentType {
    CODE_GENERATION("code_generation"),
    CODE_REVIEW("code_review"),
    CODE_EXPLANATION("code_explanation");

    private final String value;

    /**
     * Constructor for AgentType enum.
     *
     * @param value the string representation of the agent type
     */
    AgentType(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of the agent type.
     *
     * @return the string representation
     */
    public String getValue() {
        return value;
    }

    /**
     * Parses a string value and returns the corresponding AgentType enum constant.
     *
     * @param value the string value to parse
     * @return the corresponding AgentType enum constant
     * @throws IllegalArgumentException if the value does not match any agent type
     */
    public static AgentType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Agent type value cannot be null");
        }

        for (AgentType type : AgentType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid agent type: " + value);
    }
}
