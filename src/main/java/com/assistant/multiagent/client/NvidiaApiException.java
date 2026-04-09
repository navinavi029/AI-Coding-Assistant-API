package com.assistant.multiagent.client;

/**
 * Custom exception for NVIDIA API errors.
 * Wraps errors from the NVIDIA API with HTTP status code and error details.
 */
public class NvidiaApiException extends RuntimeException {
    
    private final int statusCode;
    private final String errorDetails;
    
    /**
     * Constructs a new NvidiaApiException with the specified message.
     *
     * @param message the error message
     */
    public NvidiaApiException(String message) {
        super(message);
        this.statusCode = 0;
        this.errorDetails = null;
    }
    
    /**
     * Constructs a new NvidiaApiException with the specified message and status code.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     */
    public NvidiaApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = null;
    }
    
    /**
     * Constructs a new NvidiaApiException with the specified message, status code, and error details.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param errorDetails additional error details from the API
     */
    public NvidiaApiException(String message, int statusCode, String errorDetails) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Constructs a new NvidiaApiException with the specified message and cause.
     *
     * @param message the error message
     * @param cause the cause of the exception
     */
    public NvidiaApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.errorDetails = null;
    }
    
    /**
     * Constructs a new NvidiaApiException with the specified message, status code, and cause.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param cause the cause of the exception
     */
    public NvidiaApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorDetails = null;
    }
    
    /**
     * Constructs a new NvidiaApiException with the specified message, status code, error details, and cause.
     *
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param errorDetails additional error details from the API
     * @param cause the cause of the exception
     */
    public NvidiaApiException(String message, int statusCode, String errorDetails, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Gets the HTTP status code associated with this exception.
     *
     * @return the HTTP status code, or 0 if not set
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Gets the error details from the API.
     *
     * @return the error details, or null if not set
     */
    public String getErrorDetails() {
        return errorDetails;
    }
}
