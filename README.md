# Multi-Agent Coding Assistant

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green)

## Overview

The Multi-Agent Coding Assistant is an intelligent Java-based application that leverages multiple specialized AI agents to provide comprehensive coding assistance. Built with Spring Boot and integrated with NVIDIA's AI API, this system orchestrates different agents to handle code generation, code review, and code explanation tasks efficiently.

The application uses a multi-agent architecture where each agent specializes in a specific domain of coding assistance. The orchestrator intelligently routes requests to the appropriate agent based on the task type, ensuring optimal performance and accuracy. This modular design allows for easy extension and maintenance while providing a unified API interface for all coding assistance operations.

Whether you're generating new code, reviewing existing implementations, or seeking explanations for complex code structures, the Multi-Agent Coding Assistant provides AI-powered support through a simple REST API, making it easy to integrate into your development workflow.

## Features

- **Code Generation**: Generate code snippets, functions, and complete implementations based on natural language descriptions
- **Code Review**: Automated code review with suggestions for improvements, best practices, and potential issues
- **Code Explanation**: Detailed explanations of code functionality, logic flow, and design patterns
- **NVIDIA API Integration**: Powered by NVIDIA's advanced AI models for high-quality responses
- **Multi-Agent Orchestration**: Intelligent routing to specialized agents for optimal task handling
- **RESTful API**: Simple HTTP endpoints for easy integration
- **Docker Support**: Containerized deployment for consistent environments
- **OpenAPI Documentation**: Interactive API documentation via Swagger UI

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Docker Deployment](#docker-deployment)
- [Development Setup](#development-setup)
- [Testing](#testing)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)
- [Security](#security)
- [Code of Conduct](#code-of-conduct)

## Prerequisites

Before running the Multi-Agent Coding Assistant, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+** for dependency management and building
- **Docker** (optional, for containerized deployment)
- **NVIDIA API Key** for AI model access

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/multi-agent-assistant.git
cd multi-agent-assistant
```

### Step 2: Configure Environment Variables

Copy the example environment file and configure your NVIDIA API key:

```bash
cp .env.example .env
```

Edit `.env` and add your NVIDIA API key:

```
NVIDIA_API_KEY=your_nvidia_api_key_here
```

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Configuration

The application uses environment variables for configuration. All available variables are documented in `.env.example`:

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `NVIDIA_API_KEY` | Your NVIDIA API key for AI model access | Yes | - |
| `NVIDIA_API_URL` | NVIDIA API endpoint URL | No | `https://integrate.api.nvidia.com/v1` |
| `NVIDIA_MODEL` | AI model to use | No | `meta/llama-3.1-8b-instruct` |
| `SERVER_PORT` | Application server port | No | `8080` |
| `LOG_LEVEL` | Logging level (DEBUG, INFO, WARN, ERROR) | No | `INFO` |

## Usage

### Health Check

Check if the application is running:

```bash
curl http://localhost:8080/health
```

Response:
```json
{
  "status": "UP",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Code Generation

Generate code based on a description:

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_GENERATION",
    "prompt": "Create a Java function to calculate factorial"
  }'
```

Response:
```json
{
  "agentType": "CODE_GENERATION",
  "response": "public static long factorial(int n) {\n    if (n < 0) throw new IllegalArgumentException(\"n must be non-negative\");\n    if (n == 0 || n == 1) return 1;\n    long result = 1;\n    for (int i = 2; i <= n; i++) {\n        result *= i;\n    }\n    return result;\n}",
  "timestamp": "2024-01-15T10:35:00Z"
}
```

### Code Review

Request a code review:

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_REVIEW",
    "prompt": "Review this code: public void process() { String s = null; s.length(); }"
  }'
```

Response:
```json
{
  "agentType": "CODE_REVIEW",
  "response": "Issues found:\n1. NullPointerException risk: Variable 's' is initialized to null and immediately dereferenced.\n2. Missing null check before calling s.length().\n\nSuggestions:\n- Add null validation\n- Consider using Optional<String>\n- Add proper error handling",
  "timestamp": "2024-01-15T10:40:00Z"
}
```

### Code Explanation

Get an explanation of code:

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_EXPLANATION",
    "prompt": "Explain this code: Stream.of(1,2,3).map(x -> x * 2).collect(Collectors.toList())"
  }'
```

Response:
```json
{
  "agentType": "CODE_EXPLANATION",
  "response": "This code uses Java Streams to transform a collection:\n1. Stream.of(1,2,3) creates a stream of integers\n2. map(x -> x * 2) applies a transformation, multiplying each element by 2\n3. collect(Collectors.toList()) gathers results into a List\n\nResult: [2, 4, 6]\n\nThis is a functional programming approach for data transformation.",
  "timestamp": "2024-01-15T10:45:00Z"
}
```

## Docker Deployment

### Build Docker Image

```bash
docker build -t multi-agent-assistant:1.0.0 .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

This will start the application on port 8080. The docker-compose configuration includes:
- Automatic restart on failure
- Environment variable configuration
- Health check monitoring
- Volume mounting for logs

### Stop the Container

```bash
docker-compose down
```

## Development Setup

### Local Development Environment

1. **Import into IDE**: Import the project as a Maven project into your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)

2. **Configure Run Configuration**: Set up a run configuration with the following VM options:
   ```
   -Dspring.profiles.active=dev
   ```

3. **Enable Hot Reload**: Use Spring Boot DevTools for automatic restart on code changes (already included in dependencies)

4. **Run in Debug Mode**: Start the application in debug mode to enable breakpoints and step-through debugging

### Project Structure

```
src/
├── main/
│   ├── java/com/assistant/multiagent/
│   │   ├── client/          # NVIDIA API client
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── exception/       # Exception handlers
│   │   ├── model/           # Data models
│   │   └── service/         # Business logic and agents
│   └── resources/
│       ├── application.yml  # Application configuration
│       └── logback-spring.xml  # Logging configuration
└── test/
    └── java/com/assistant/multiagent/  # Unit and integration tests
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=NvidiaApiClientTest
```

### Run Tests with Coverage

```bash
mvn clean test jacoco:report
```

Coverage reports will be generated in `target/site/jacoco/index.html`.

### Test Categories

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions and API integration
- **Controller Tests**: Test REST endpoints with MockMvc

## API Documentation

### Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

The OpenAPI 3.0 specification is available at:

```
http://localhost:8080/v3/api-docs
```

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Health check endpoint |
| GET | `/diagnostic` | System diagnostic information |
| POST | `/api/assist` | Main assistance endpoint for all agent types |

## Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on:

- Development environment setup
- Code style and conventions
- Testing requirements
- Pull request process
- Issue reporting guidelines

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Security

For information on reporting security vulnerabilities, please see our [Security Policy](SECURITY.md).

## Code of Conduct

This project adheres to a Code of Conduct that all contributors are expected to follow. Please read [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) to understand the expectations for participation in this community.

---

**Project Version**: 1.0.0  
**Spring Boot Version**: 3.2.1  
**Java Version**: 17
