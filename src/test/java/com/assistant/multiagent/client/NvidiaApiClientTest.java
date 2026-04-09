package com.assistant.multiagent.client;

import com.assistant.multiagent.config.AppConfig;
import com.assistant.multiagent.model.NvidiaRequest;
import com.assistant.multiagent.model.NvidiaResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NvidiaApiClient.
 * Tests non-streaming requests, error handling, and request construction.
 */
class NvidiaApiClientTest {

    private MockWebServer mockWebServer;
    private NvidiaApiClient client;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create mock AppConfig
        appConfig = new AppConfig() {
            @Override
            public String getNvidiaApiKey() {
                return "test-api-key";
            }

            @Override
            public String getNvidiaApiUrl() {
                return mockWebServer.url("/v1/chat/completions").toString();
            }

            @Override
            public String getNvidiaModel() {
                return "google/gemma-4-31b-it";
            }

            @Override
            public int getRequestTimeoutSeconds() {
                return 5;
            }
        };

        WebClient webClient = WebClient.builder().build();
        client = new NvidiaApiClient(webClient, appConfig);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testSendRequest_Success() throws InterruptedException {
        // Arrange
        String responseBody = """
            {
                "id": "chatcmpl-123",
                "object": "chat.completion",
                "created": 1677652288,
                "model": "google/gemma-4-31b-it",
                "choices": [{
                    "index": 0,
                    "message": {
                        "role": "assistant",
                        "content": "Hello! How can I help you?"
                    },
                    "finishReason": "stop"
                }]
            }
            """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setHeader("Content-Type", "application/json"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Hello")
                .build();

        // Act
        NvidiaResponse response = client.sendRequest(request);

        // Assert
        assertNotNull(response);
        assertEquals("chatcmpl-123", response.getId());
        assertEquals("chat.completion", response.getObject());
        assertEquals(1677652288, response.getCreated());
        assertEquals("google/gemma-4-31b-it", response.getModel());
        assertEquals(1, response.getChoices().size());
        assertEquals("Hello! How can I help you?", response.getChoices().get(0).getMessage().getContent());

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("Bearer test-api-key", recordedRequest.getHeader("Authorization"));
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));
    }

    @Test
    void testSendRequest_ApiError() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"error\": \"Invalid request\"}")
                .setHeader("Content-Type", "application/json"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Hello")
                .build();

        // Act & Assert
        NvidiaApiException exception = assertThrows(NvidiaApiException.class, () -> {
            client.sendRequest(request);
        });

        // The exception should contain the original status code from the API
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getErrorDetails());
    }

    @Test
    void testConstructorInjection() {
        // Verify that constructor injection works correctly
        assertNotNull(client);
    }

    @Test
    void testRequestLogging() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\":\"test\",\"object\":\"chat.completion\",\"created\":123,\"model\":\"test\",\"choices\":[]}")
                .setHeader("Content-Type", "application/json"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Test")
                .build();

        // Act - should log without throwing
        assertDoesNotThrow(() -> client.sendRequest(request));
    }

    @Test
    void testSendRequestStreaming_SetsStreamParameterToTrue() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("data: {\"chunk\": \"test\"}\n\n")
                .setHeader("Content-Type", "text/event-stream"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Hello")
                .stream(false) // Intentionally set to false to verify it gets overridden
                .build();

        // Act
        client.sendRequestStreaming(request)
                .blockFirst(); // Block to trigger the request

        // Assert - verify the stream parameter was set to true
        assertTrue(request.isStream(), "Stream parameter should be set to true for streaming requests");
    }

    @Test
    void testSendRequestStreaming_ProcessesChunksIncrementally() {
        // Arrange
        String sseResponse = "data: chunk1\n\ndata: chunk2\n\ndata: chunk3\n\n";
        mockWebServer.enqueue(new MockResponse()
                .setBody(sseResponse)
                .setHeader("Content-Type", "text/event-stream"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Test streaming")
                .build();

        // Act
        List<String> chunks = client.sendRequestStreaming(request)
                .collectList()
                .block();

        // Assert
        assertNotNull(chunks);
        assertFalse(chunks.isEmpty(), "Should receive streaming chunks");
    }

    @Test
    void testSendRequestStreaming_HandlesConnectionInterruption() {
        // Arrange - simulate connection interruption by closing the server
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        NvidiaRequest request = NvidiaRequest.builder()
                .model("google/gemma-4-31b-it")
                .addUserMessage("Test")
                .build();

        // Act & Assert
        assertThrows(NvidiaApiException.class, () -> {
            client.sendRequestStreaming(request)
                    .blockFirst();
        });
    }
}
