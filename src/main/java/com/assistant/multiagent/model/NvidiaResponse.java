package com.assistant.multiagent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a response from the NVIDIA Gemini API.
 * This class encapsulates the API response structure including choices,
 * messages, and metadata about the generation.
 * 
 * Validates: Requirements 2.4
 */
public class NvidiaResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    /**
     * Default constructor.
     */
    public NvidiaResponse() {
        this.choices = new ArrayList<>();
    }

    /**
     * Gets the unique identifier for this response.
     *
     * @return the response ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this response.
     *
     * @param id the response ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the object type (typically "chat.completion").
     *
     * @return the object type string
     */
    public String getObject() {
        return object;
    }

    /**
     * Sets the object type.
     *
     * @param object the object type string
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Gets the Unix timestamp when the response was created.
     *
     * @return the creation timestamp
     */
    public long getCreated() {
        return created;
    }

    /**
     * Sets the Unix timestamp when the response was created.
     *
     * @param created the creation timestamp
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Gets the model identifier used for generation.
     *
     * @return the model string
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier.
     *
     * @param model the model string
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the list of generated choices.
     *
     * @return the choices list
     */
    public List<Choice> getChoices() {
        return choices;
    }

    /**
     * Sets the list of generated choices.
     *
     * @param choices the choices list
     */
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    /**
     * Nested class representing a single choice in the API response.
     * Each choice contains an index, the generated message, and finish reason.
     */
    public static class Choice {
        private int index;
        private Message message;
        private String finishReason;

        /**
         * Default constructor.
         */
        public Choice() {
        }

        /**
         * Constructor with all fields.
         *
         * @param index the choice index
         * @param message the generated message
         * @param finishReason the reason generation finished
         */
        public Choice(int index, Message message, String finishReason) {
            this.index = index;
            this.message = message;
            this.finishReason = finishReason;
        }

        /**
         * Gets the choice index.
         *
         * @return the index
         */
        public int getIndex() {
            return index;
        }

        /**
         * Sets the choice index.
         *
         * @param index the index
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * Gets the generated message.
         *
         * @return the message
         */
        public Message getMessage() {
            return message;
        }

        /**
         * Sets the generated message.
         *
         * @param message the message
         */
        public void setMessage(Message message) {
            this.message = message;
        }

        /**
         * Gets the reason generation finished.
         *
         * @return the finish reason string
         */
        public String getFinishReason() {
            return finishReason;
        }

        /**
         * Sets the reason generation finished.
         *
         * @param finishReason the finish reason string
         */
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
    }

    /**
     * Nested class representing a message in the API response.
     * Each message has a role (typically "assistant") and content.
     */
    public static class Message {
        private String role;
        private String content;

        /**
         * Default constructor.
         */
        public Message() {
        }

        /**
         * Constructor with all fields.
         *
         * @param role the role of the message sender
         * @param content the message content
         */
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        /**
         * Gets the role of the message sender.
         *
         * @return the role string
         */
        public String getRole() {
            return role;
        }

        /**
         * Sets the role of the message sender.
         *
         * @param role the role string
         */
        public void setRole(String role) {
            this.role = role;
        }

        /**
         * Gets the message content.
         *
         * @return the content string
         */
        public String getContent() {
            return content;
        }

        /**
         * Sets the message content.
         *
         * @param content the content string
         */
        public void setContent(String content) {
            this.content = content;
        }
    }
}
