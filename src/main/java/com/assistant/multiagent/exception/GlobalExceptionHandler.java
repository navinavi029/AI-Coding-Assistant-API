package com.assistant.multiagent.exception;

import com.assistant.multiagent.client.NvidiaApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles all exceptions thrown by controllers and provides consistent error responses.
 * 
 * Validates: Requirements 7.1, 7.2, 7.3, 7.6
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles NvidiaApiException thrown by the NVIDIA API client.
     * Maps the exception to an appropriate HTTP status code based on the error type.
     *
     * @param ex the NvidiaApiException
     * @param request the web request
     * @return ResponseEntity with error details and appropriate HTTP status
     */
    @ExceptionHandler(NvidiaApiException.class)
    public ResponseEntity<Map<String, Object>> handleNvidiaApiException(
            NvidiaApiException ex, WebRequest request) {
        
        logger.error("NVIDIA API error: {} - Status: {}, Details: {}", 
                    ex.getMessage(), ex.getStatusCode(), ex.getErrorDetails());

        HttpStatus status = determineHttpStatus(ex.getStatusCode());
        
        Map<String, Object> errorResponse = createErrorResponse(
            ex.getMessage(),
            status.value(),
            request.getDescription(false).replace("uri=", "")
        );

        if (ex.getErrorDetails() != null) {
            errorResponse.put("details", ex.getErrorDetails());
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handles validation errors from @Valid annotation on request bodies.
     * Returns HTTP 400 with field-specific error messages.
     *
     * @param ex the MethodArgumentNotValidException
     * @param request the web request
     * @return ResponseEntity with validation error details and HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        logger.warn("Validation error: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> errorResponse = createErrorResponse(
            "Validation failed",
            HttpStatus.BAD_REQUEST.value(),
            request.getDescription(false).replace("uri=", "")
        );
        errorResponse.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException thrown by the application.
     * Returns HTTP 400 with error message.
     *
     * @param ex the IllegalArgumentException
     * @param request the web request
     * @return ResponseEntity with error details and HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        logger.warn("Invalid argument: {}", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions.
     * Returns HTTP 500 with generic error message.
     *
     * @param ex the Exception
     * @param request the web request
     * @return ResponseEntity with error details and HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> errorResponse = createErrorResponse(
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a standardized error response map.
     *
     * @param message the error message
     * @param status the HTTP status code
     * @param path the request path
     * @return Map containing error, timestamp, path, and status
     */
    private Map<String, Object> createErrorResponse(String message, int status, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("path", path);
        errorResponse.put("status", status);
        return errorResponse;
    }

    /**
     * Determines the appropriate HTTP status based on the NVIDIA API status code.
     *
     * @param apiStatusCode the status code from the NVIDIA API
     * @return the corresponding HttpStatus
     */
    private HttpStatus determineHttpStatus(int apiStatusCode) {
        if (apiStatusCode == 503) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else if (apiStatusCode == 504) {
            return HttpStatus.GATEWAY_TIMEOUT;
        } else if (apiStatusCode >= 500) {
            return HttpStatus.BAD_GATEWAY;
        } else if (apiStatusCode >= 400) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.BAD_GATEWAY;
        }
    }
}
