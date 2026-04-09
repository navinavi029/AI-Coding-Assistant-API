package com.assistant.multiagent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object representing a request to the NVIDIA Gemini API.
 * This class encapsulates the model configuration, messages, and generation parameters.
 * 
 * Validates: Requirements 2.2, 2.4, 2.7
 */
public class NvidiaRequest {

    private String model;
    private List<Message> messages;
    private boolean stream;
    
    @JsonProperty("chat_template_kwargs")
    private Map<String, Object> chatTemplateKwargs;
    
    private double temperature = 0.7;
    
    @JsonProperty("max_tokens")
    private int maxTokens = 2048;
    
    @JsonProperty("top_p")
    private double topP = 0.95;

    /**
     * Default constructor.
     */
    public NvidiaRequest() {
        this.messages = new ArrayList<>();
        this.chatTemplateKwargs = new HashMap<>();
    }

    /**
     * Private constructor for builder pattern.
     *
     * @param builder the builder instance
     */
    private NvidiaRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.stream = builder.stream;
        this.chatTemplateKwargs = builder.chatTemplateKwargs;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.topP = builder.topP;
    }

    /**
     * Gets the model identifier.
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
     * Gets the list of messages.
     *
     * @return the messages list
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages.
     *
     * @param messages the messages list
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Checks if streaming is enabled.
     *
     * @return true if streaming is enabled, false otherwise
     */
    public boolean isStream() {
        return stream;
    }

    /**
     * Sets the streaming flag.
     *
     * @param stream true to enable streaming, false otherwise
     */
    public void setStream(boolean stream) {
        this.stream = stream;
    }

    /**
     * Gets the chat template kwargs map.
     *
     * @return the chat template kwargs map
     */
    public Map<String, Object> getChatTemplateKwargs() {
        return chatTemplateKwargs;
    }

    /**
     * Sets the chat template kwargs map.
     *
     * @param chatTemplateKwargs the chat template kwargs map
     */
    public void setChatTemplateKwargs(Map<String, Object> chatTemplateKwargs) {
        this.chatTemplateKwargs = chatTemplateKwargs;
    }
    
    /**
     * Checks if thinking mode is enabled.
     *
     * @return true if thinking mode is enabled, false otherwise
     */
    @JsonIgnore
    public boolean isEnableThinking() {
        return chatTemplateKwargs != null && 
               Boolean.TRUE.equals(chatTemplateKwargs.get("enable_thinking"));
    }

    /**
     * Sets the thinking mode flag.
     *
     * @param enableThinking true to enable thinking mode, false otherwise
     */
    @JsonIgnore
    public void setEnableThinking(boolean enableThinking) {
        if (this.chatTemplateKwargs == null) {
            this.chatTemplateKwargs = new HashMap<>();
        }
        this.chatTemplateKwargs.put("enable_thinking", enableThinking);
    }

    /**
     * Gets the temperature parameter for response generation.
     *
     * @return the temperature value
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature parameter.
     *
     * @param temperature the temperature value (typically 0.0 to 1.0)
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the maximum number of tokens to generate.
     *
     * @return the max tokens value
     */
    public int getMaxTokens() {
        return maxTokens;
    }

    /**
     * Sets the maximum number of tokens.
     *
     * @param maxTokens the max tokens value
     */
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    /**
     * Gets the top_p parameter for nucleus sampling.
     *
     * @return the top_p value
     */
    public double getTopP() {
        return topP;
    }

    /**
     * Sets the top_p parameter.
     *
     * @param topP the top_p value (typically 0.0 to 1.0)
     */
    public void setTopP(double topP) {
        this.topP = topP;
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
     * Nested class representing a message in the conversation.
     * Each message has a role (system or user) and content.
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
         * @param role the role of the message sender ("system" or "user")
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
         * @param role the role string ("system" or "user")
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

    /**
     * Builder class for constructing NvidiaRequest instances.
     */
    public static class Builder {
        private String model;
        private List<Message> messages = new ArrayList<>();
        private boolean stream = false;
        private Map<String, Object> chatTemplateKwargs = new HashMap<>();
        private double temperature = 0.7;
        private int maxTokens = 2048;
        private double topP = 0.95;

        /**
         * Sets the model identifier.
         *
         * @param model the model string
         * @return this builder
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * Sets the messages list.
         *
         * @param messages the messages list
         * @return this builder
         */
        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        /**
         * Adds a single message to the messages list.
         *
         * @param role the role of the message sender
         * @param content the message content
         * @return this builder
         */
        public Builder addMessage(String role, String content) {
            this.messages.add(new Message(role, content));
            return this;
        }

        /**
         * Adds a system message.
         *
         * @param content the system message content
         * @return this builder
         */
        public Builder addSystemMessage(String content) {
            return addMessage("system", content);
        }

        /**
         * Adds a user message.
         *
         * @param content the user message content
         * @return this builder
         */
        public Builder addUserMessage(String content) {
            return addMessage("user", content);
        }

        /**
         * Sets the streaming flag.
         *
         * @param stream true to enable streaming, false otherwise
         * @return this builder
         */
        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        /**
         * Sets the thinking mode flag.
         *
         * @param enableThinking true to enable thinking mode, false otherwise
         * @return this builder
         */
        public Builder enableThinking(boolean enableThinking) {
            this.chatTemplateKwargs.put("enable_thinking", enableThinking);
            return this;
        }
        
        /**
         * Sets the top_p parameter.
         *
         * @param topP the top_p value
         * @return this builder
         */
        public Builder topP(double topP) {
            this.topP = topP;
            return this;
        }

        /**
         * Sets the temperature parameter.
         *
         * @param temperature the temperature value
         * @return this builder
         */
        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * Sets the maximum number of tokens.
         *
         * @param maxTokens the max tokens value
         * @return this builder
         */
        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        /**
         * Builds the NvidiaRequest instance.
         *
         * @return a new NvidiaRequest
         */
        public NvidiaRequest build() {
            return new NvidiaRequest(this);
        }
    }
}
