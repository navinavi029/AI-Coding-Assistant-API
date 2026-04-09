package com.assistant.multiagent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for health check endpoints.
 * Provides a simple health check endpoint to verify the application is running.
 * 
 * Validates: Requirements 3.7, 3.8
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Application health check endpoints")
public class HealthController {

    /**
     * Health check endpoint that returns the application status.
     * This endpoint is used by monitoring systems and load balancers to verify
     * that the application is operational.
     *
     * @return ResponseEntity with HTTP 200 and JSON body containing status "UP"
     */
    @Operation(
        summary = "Health check",
        description = "Returns the application health status. Used by monitoring systems and load balancers."
    )
    @ApiResponse(responseCode = "200", description = "Application is healthy")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
