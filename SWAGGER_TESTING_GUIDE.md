# Swagger UI Testing Guide - Multi-Agent Coding Assistant

## 🌐 Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8081/swagger-ui/index.html
```

---

## 📋 Step-by-Step Testing Instructions

### 1. Health Check Endpoint (Warmup Test)

**Purpose**: Verify the application is running

1. In Swagger UI, find the **"Health"** section
2. Click on **GET /api/v1/health**
3. Click the **"Try it out"** button
4. Click **"Execute"**

**Expected Response:**
```json
{
  "status": "UP"
}
```

---

### 2. Code Generation Agent

**Purpose**: Generate new code based on requirements

#### Example 1: Simple Function

1. Find the **"Coding Assistant"** section
2. Click on **POST /api/v1/assist**
3. Click **"Try it out"**
4. Replace the request body with:

```json
{
  "prompt": "Write a Java function to calculate factorial of a number",
  "agentType": "CODE_GENERATION",
  "streaming": false
}
```

5. Click **"Execute"**

**Expected Response:**
```json
{
  "content": "Here's a Java function to calculate factorial:\n\n```java\npublic static long factorial(int n) {\n    if (n < 0) throw new IllegalArgumentException(\"n must be non-negative\");\n    if (n == 0 || n == 1) return 1;\n    return n * factorial(n - 1);\n}\n```",
  "agentType": "CODE_GENERATION",
  "timestamp": "2026-04-09T13:45:00Z",
  "error": null,
  "metadata": {
    "model": "google/gemma-4-31b-it"
  }
}
```

#### Example 2: REST API Endpoint

```json
{
  "prompt": "Create a Spring Boot REST controller for managing users with CRUD operations",
  "agentType": "CODE_GENERATION",
  "streaming": false
}
```

#### Example 3: Algorithm Implementation

```json
{
  "prompt": "Implement binary search algorithm in Java with comments",
  "agentType": "CODE_GENERATION",
  "streaming": false
}
```

#### Example 4: Database Query

```json
{
  "prompt": "Write a SQL query to find the top 5 customers by total purchase amount",
  "agentType": "CODE_GENERATION",
  "streaming": false
}
```

---

### 3. Code Review Agent

**Purpose**: Analyze code for bugs, security issues, and improvements

#### Example 1: Review Simple Code

```json
{
  "prompt": "Review this code:\n\npublic void processData(String data) {\n    System.out.println(data.toLowerCase());\n}",
  "agentType": "CODE_REVIEW",
  "streaming": false
}
```

**What to expect**: The agent will identify potential NullPointerException issues

#### Example 2: Security Review

```json
{
  "prompt": "Review this code for security issues:\n\npublic String getUserData(String userId) {\n    String query = \"SELECT * FROM users WHERE id = '\" + userId + \"'\";\n    return database.execute(query);\n}",
  "agentType": "CODE_REVIEW",
  "streaming": false
}
```

**What to expect**: The agent will identify SQL injection vulnerability

#### Example 3: Performance Review

```json
{
  "prompt": "Review this code for performance issues:\n\npublic List<User> getActiveUsers() {\n    List<User> allUsers = userRepository.findAll();\n    List<User> activeUsers = new ArrayList<>();\n    for (User user : allUsers) {\n        if (user.isActive()) {\n            activeUsers.add(user);\n        }\n    }\n    return activeUsers;\n}",
  "agentType": "CODE_REVIEW",
  "streaming": false
}
```

**What to expect**: The agent will suggest using database filtering instead of in-memory filtering

#### Example 4: Best Practices Review

```json
{
  "prompt": "Review this code:\n\npublic class Calculator {\n    public int x;\n    public int y;\n    \n    public int add() {\n        return x + y;\n    }\n}",
  "agentType": "CODE_REVIEW",
  "streaming": false
}
```

**What to expect**: The agent will suggest making fields private and using proper encapsulation

---

### 4. Code Explanation Agent

**Purpose**: Explain how code works in clear, understandable terms

#### Example 1: Explain Algorithm

```json
{
  "prompt": "Explain how binary search works",
  "agentType": "CODE_EXPLANATION",
  "streaming": false
}
```

#### Example 2: Explain Code Snippet

```json
{
  "prompt": "Explain this code:\n\npublic static <T> List<T> filter(List<T> list, Predicate<T> predicate) {\n    return list.stream()\n               .filter(predicate)\n               .collect(Collectors.toList());\n}",
  "agentType": "CODE_EXPLANATION",
  "streaming": false
}
```

#### Example 3: Explain Concept

```json
{
  "prompt": "Explain what dependency injection is and why it's useful in Spring Boot",
  "agentType": "CODE_EXPLANATION",
  "streaming": false
}
```

#### Example 4: Explain Design Pattern

```json
{
  "prompt": "Explain the Singleton design pattern with a Java example",
  "agentType": "CODE_EXPLANATION",
  "streaming": false
}
```

---

### 5. Streaming Response (Advanced)

**Purpose**: Get real-time streaming responses via Server-Sent Events

**Note**: Swagger UI may not display streaming responses well. Use curl or a proper SSE client for best results.

```json
{
  "prompt": "Explain recursion in Java with examples",
  "agentType": "CODE_EXPLANATION",
  "streaming": true
}
```

**To test streaming properly, use curl:**
```bash
curl -X POST http://localhost:8081/api/v1/assist \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{
    "prompt": "Explain recursion in Java",
    "agentType": "CODE_EXPLANATION",
    "streaming": true
  }'
```

---

## 🧪 Testing Error Scenarios

### Test 1: Missing Prompt (Should return 400)

```json
{
  "prompt": "",
  "agentType": "CODE_GENERATION",
  "streaming": false
}
```

**Expected**: HTTP 400 Bad Request with validation error

### Test 2: Invalid Agent Type (Should return 500)

```json
{
  "prompt": "Test prompt",
  "agentType": "INVALID_TYPE",
  "streaming": false
}
```

**Expected**: HTTP 500 Internal Server Error (Jackson deserialization error)

### Test 3: Missing Agent Type (Should return 400)

```json
{
  "prompt": "Test prompt",
  "streaming": false
}
```

**Expected**: HTTP 400 Bad Request with validation error

---

## 💡 Tips for Best Results

### 1. Be Specific in Your Prompts
❌ Bad: "Write code"
✅ Good: "Write a Java function to validate email addresses using regex"

### 2. Provide Context for Code Review
❌ Bad: "Review this code: x = y + z"
✅ Good: "Review this code for thread safety:\n\npublic class Counter {\n    private int count = 0;\n    public void increment() { count++; }\n}"

### 3. Ask Clear Questions for Explanations
❌ Bad: "Explain Java"
✅ Good: "Explain how Java's garbage collection works and when objects are eligible for collection"

### 4. Use Proper Formatting
- Use `\n` for line breaks in JSON strings
- Escape quotes inside strings with `\"`
- Keep code snippets readable

---

## 📊 Understanding Response Fields

### Successful Response Structure:
```json
{
  "content": "The AI-generated response",
  "agentType": "CODE_GENERATION",
  "timestamp": "2026-04-09T13:45:00Z",
  "error": null,
  "metadata": {
    "model": "google/gemma-4-31b-it"
  }
}
```

### Error Response Structure:
```json
{
  "error": "Error message",
  "timestamp": "2026-04-09T13:45:00Z",
  "path": "/api/v1/assist",
  "status": 400
}
```

---

## 🎯 Quick Test Scenarios

### Scenario 1: Full Development Workflow

1. **Generate**: Create a User entity class
2. **Review**: Check the generated code for issues
3. **Explain**: Understand the annotations used

### Scenario 2: Learning Path

1. **Explain**: What is Spring Boot?
2. **Generate**: Create a simple Spring Boot REST API
3. **Review**: Check the generated API for best practices

### Scenario 3: Code Improvement

1. **Review**: Submit existing code for review
2. **Generate**: Ask for improved version based on review feedback
3. **Explain**: Understand why the improvements are better

---

## 🔍 Monitoring Responses

### Check Application Logs
While testing, you can monitor the application logs to see:
- Request processing
- Agent selection
- NVIDIA API calls
- Response generation

The logs will show entries like:
```
INFO  c.a.m.controller.AssistantController - Received assist request - agentType: CODE_GENERATION, streaming: false
INFO  c.a.m.service.AgentOrchestrator - Processing request with agent type: CODE_GENERATION
INFO  c.a.m.client.NvidiaApiClient - Sending request to NVIDIA API
```

---

## 🚀 Advanced Usage

### Chaining Requests

1. Generate code with CODE_GENERATION
2. Copy the generated code
3. Review it with CODE_REVIEW
4. Ask for explanation with CODE_EXPLANATION

### Iterative Refinement

1. Generate initial code
2. Review and note issues
3. Generate improved version with specific requirements
4. Repeat until satisfied

---

## ⚠️ Important Notes

1. **API Key Required**: The application needs a valid NVIDIA_API_KEY environment variable
2. **Response Time**: Non-streaming requests may take 2-5 seconds
3. **Token Limits**: Very long prompts may be truncated
4. **Rate Limits**: NVIDIA API may have rate limits depending on your plan

---

## 📞 Troubleshooting

### Issue: "Service temporarily unavailable"
- Check if NVIDIA_API_KEY is set correctly
- Verify internet connectivity
- Check NVIDIA API status

### Issue: "Request timeout"
- Try a shorter prompt
- Check network latency
- Increase REQUEST_TIMEOUT_SECONDS environment variable

### Issue: Empty or null content
- Verify NVIDIA_API_KEY is valid (not a test key)
- Check application logs for API errors
- Ensure prompt is clear and specific

---

## 🎓 Example Test Session

Here's a complete test session you can follow:

1. **Health Check**: Verify app is running
2. **Simple Generation**: "Write a hello world function in Java"
3. **Code Review**: Review the generated function
4. **Explanation**: "Explain what the main method does in Java"
5. **Complex Generation**: "Create a Spring Boot service class for user management"
6. **Security Review**: Review the service class for security issues
7. **Error Test**: Submit empty prompt to test validation

---

**Happy Testing! 🎉**

For more information, see the main README.md file.
