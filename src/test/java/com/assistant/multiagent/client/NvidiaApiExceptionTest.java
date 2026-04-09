package com.assistant.multiagent.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NvidiaApiException.
 */
class NvidiaApiExceptionTest {
    
    @Test
    void testConstructorWithMessage() {
        String message = "API error occurred";
        NvidiaApiException exception = new NvidiaApiException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(0, exception.getStatusCode());
        assertNull(exception.getErrorDetails());
        assertNull(exception.getCause());
    }
    
    @Test
    void testConstructorWithMessageAndStatusCode() {
        String message = "Bad request";
        int statusCode = 400;
        NvidiaApiException exception = new NvidiaApiException(message, statusCode);
        
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getErrorDetails());
        assertNull(exception.getCause());
    }
    
    @Test
    void testConstructorWithMessageStatusCodeAndErrorDetails() {
        String message = "Invalid request";
        int statusCode = 422;
        String errorDetails = "Missing required field: model";
        NvidiaApiException exception = new NvidiaApiException(message, statusCode, errorDetails);
        
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errorDetails, exception.getErrorDetails());
        assertNull(exception.getCause());
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Network error";
        Throwable cause = new RuntimeException("Connection timeout");
        NvidiaApiException exception = new NvidiaApiException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(0, exception.getStatusCode());
        assertNull(exception.getErrorDetails());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void testConstructorWithMessageStatusCodeAndCause() {
        String message = "Service unavailable";
        int statusCode = 503;
        Throwable cause = new RuntimeException("Backend down");
        NvidiaApiException exception = new NvidiaApiException(message, statusCode, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getErrorDetails());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void testConstructorWithAllParameters() {
        String message = "Authentication failed";
        int statusCode = 401;
        String errorDetails = "Invalid API key";
        Throwable cause = new RuntimeException("Auth error");
        NvidiaApiException exception = new NvidiaApiException(message, statusCode, errorDetails, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errorDetails, exception.getErrorDetails());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void testExceptionIsRuntimeException() {
        NvidiaApiException exception = new NvidiaApiException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
    
    @Test
    void testStatusCodeEdgeCases() {
        // Test with 0 status code
        NvidiaApiException exception1 = new NvidiaApiException("Test", 0);
        assertEquals(0, exception1.getStatusCode());
        
        // Test with negative status code (edge case)
        NvidiaApiException exception2 = new NvidiaApiException("Test", -1);
        assertEquals(-1, exception2.getStatusCode());
        
        // Test with large status code
        NvidiaApiException exception3 = new NvidiaApiException("Test", 599);
        assertEquals(599, exception3.getStatusCode());
    }
    
    @Test
    void testNullErrorDetails() {
        NvidiaApiException exception = new NvidiaApiException("Test", 500, (String) null);
        assertNull(exception.getErrorDetails());
    }
    
    @Test
    void testEmptyErrorDetails() {
        String emptyDetails = "";
        NvidiaApiException exception = new NvidiaApiException("Test", 500, emptyDetails);
        assertEquals(emptyDetails, exception.getErrorDetails());
    }
}
