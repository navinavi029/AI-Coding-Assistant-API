package com.assistant.multiagent.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing a response from the assistant API.
 * This class encapsulates the AI-generated content, agent type information,
 * timestamp, error details, and additional metadata.
 * 
 * Validates: Requirements 4.5, 4.6, 4.7
 */
public class AssistResponse {

    private String content;
    private String agentType;
    private String timestamp;
    private String error;
    private Map<String, Object> metadata;

    /**
     * Default constructor.
     */
    public AssistResponse() {
        this.metadata = new HashMap<>();
    }

    /**
     * Private constructor for builder pattern.
     *
     * @param builder the builder instance
     */
    private AssistResponse(Builder builder) {
        this.content = builder.content;
        this.agentType = builder.agentType;
        this.timestamp = builder.timestamp;
        this.error = builder.error;
        this.metadata = builder.metadata;
    }

    /**
     * Gets the AI-generated content.
     *
     * @return the content string
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the AI-generated content.
     *
     * @param content the content string
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the agent type that processed the request.
     *
     * @return the agent type string
     */
    public String getAgentType() {
        return agentType;
    }

    /**
     * Sets the agent type.
     *
     * @param agentType the agent type string
     */
    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    /**
     * Gets the timestamp in ISO 8601 format.
     *
     * @return the timestamp string
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp string in ISO 8601 format
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the error message if an error occurred.
     *
     * @return the error message, or null if no error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     *
     * @param error the error message
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets the metadata map.
     *
     * @return the metadata map
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Sets the metadata map.
     *
     * @param metadata the metadata map
     */
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    /**
     * Creates a new builder instance.
     *
     * @return a new Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing AssistResponse instances.
     */
    public static class Builder {
        private String content;
        private String agentType;
        private String timestamp;
        private String error;
        private Map<String, Object> metadata = new HashMap<>();

        /**
         * Sets the content.
         *
         * @param content the AI-generated content
         * @return this builder
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets the agent type.
         *
         * @param agentType the agent type string
         * @return this builder
         */
        public Builder agentType(String agentType) {
            this.agentType = agentType;
            return this;
        }

        /**
         * Sets the timestamp.
         *
         * @param timestamp the timestamp in ISO 8601 format
         * @return this builder
         */
        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Sets the error message.
         *
         * @param error the error message
         * @return this builder
         */
        public Builder error(String error) {
            this.error = error;
            return this;
        }

        /**
         * Sets the metadata map.
         *
         * @param metadata the metadata map
         * @return this builder
         */
        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Adds a single metadata entry.
         *
         * @param key the metadata key
         * @param value the metadata value
         * @return this builder
         */
        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        /**
         * Builds the AssistResponse instance.
         *
         * @return a new AssistResponse
         */
        public AssistResponse build() {
            return new AssistResponse(this);
        }
    }
}
