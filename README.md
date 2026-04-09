# Gemma4-Code-Assistant-API

> A production-ready multi-agent coding assistant powered by Google's Gemma 4 (31B) model via NVIDIA API, built with Spring Boot 3.2

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

## 🚀 Features

- **Multi-Agent Architecture**: Three specialized AI agents for different coding tasks
  - 🔨 **Code Generation Agent**: Generate clean, efficient, well-documented code
  - 🔍 **Code Review Agent**: Analyze code quality, identify issues, suggest improvements
  - 📚 **Code Explanation Agent**: Explain complex code concepts in simple terms

- **Production-Ready**
  - ✅ Comprehensive error handling and logging
  - ✅ RESTful API with OpenAPI/Swagger documentation
  - ✅ Docker support for easy deployment
  - ✅ 85+ unit tests with high coverage
  - ✅ Configurable timeouts and retry logic

- **Modern Tech Stack**
  - Spring Boot 3.2.1 with WebFlux for reactive programming
  - Integration with NVIDIA API (Google Gemma 4 31B model)
  - Swagger UI for interactive API testing
  - Logback for structured logging

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Docker Deployment](#-docker-deployment)
- [Testing](#-testing)
- [Architecture](#-architecture)
- [Contributing](#-contributing)
- [License](#-license)

## ⚡ Quick Start

```bash
# Clone the repository
git clone https://github.com/<YOUR_USERNAME>/Gemma4-Code-Assistant-API.git
cd Gemma4-Code-Assistant-API

# Set your NVIDIA API key
export NVIDIA_API_KEY="your-api-key-here"

# Build and run
mvn clean package
java -jar target/multi-agent-assistant-1.0.0.jar

# Access Swagger UI
open http://localhost:8080/swagger-ui/index.html
```

## 📦 Prerequisites

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **NVIDIA API Key** - [Get yours here](https://build.nvidia.com/)

## 🔧 Installation

### Option 1: Local Development

```bash
# Clone the repository
git clone https://github.com/<YOUR_USERNAME>/Gemma4-Code-Assistant-API.git
cd Gemma4-Code-Assistant-API

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

### Option 2: Docker

```bash
# Build Docker image
docker-compose build

# Run with Docker Compose
docker-compose up
```

## ⚙️ Configuration

Create a `.env` file in the project root or set environment variables:

```bash
# Required
NVIDIA_API_KEY=your-nvidia-api-key-here

# Optional (with defaults)
NVIDIA_API_URL=https://integrate.api.nvidia.com/v1/chat/completions
NVIDIA_MODEL=google/gemma-4-31b-it
SERVER_PORT=8080
REQUEST_TIMEOUT_SECONDS=60
```

### Configuration via `application.yml`

```yaml
nvidia:
  api:
    key: ${NVIDIA_API_KEY}
    url: ${NVIDIA_API_URL:https://integrate.api.nvidia.com/v1/chat/completions}
    model: ${NVIDIA_MODEL:google/gemma-4-31b-it}
    request-timeout-seconds: ${REQUEST_TIMEOUT_SECONDS:60}

server:
  port: ${SERVER_PORT:8080}
```

## 🎯 Usage

### REST API Examples

#### 1. Generate Code

```bash
curl -X POST http://localhost:8080/api/v1/assist \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Write a Python function to calculate fibonacci numbers",
    "agentType": "CODE_GENERATION",
    "streaming": false
  }'
```

**Response:**
```json
{
  "content": "def fibonacci(n):\n    if n <= 1:\n        return n\n    return fibonacci(n-1) + fibonacci(n-2)",
  "agentType": "CODE_GENERATION",
  "timestamp": "2026-04-09T14:22:54.328657800Z",
  "error": null,
  "metadata": {
    "model": "google/gemma-4-31b-it",
    "responseId": "chatcmpl-abc123"
  }
}
```

#### 2. Review Code

```bash
curl -X POST http://localhost:8080/api/v1/assist \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Review this code: function add(a,b){return a+b}",
    "agentType": "CODE_REVIEW",
    "streaming": false
  }'
```

#### 3. Explain Code

```bash
curl -X POST http://localhost:8080/api/v1/assist \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Explain how async/await works in JavaScript",
    "agentType": "CODE_EXPLANATION",
    "streaming": false
  }'
```

### Streaming Responses

```bash
curl -X POST http://localhost:8080/api/v1/assist \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Write a REST API in Spring Boot",
    "agentType": "CODE_GENERATION",
    "streaming": true
  }'
```

## 📚 API Documentation

### Interactive Documentation

Access Swagger UI at: **http://localhost:8080/swagger-ui/index.html**

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/assist` | Process coding assistance request |
| GET | `/api/v1/health` | Health check endpoint |
| GET | `/api/v1/diagnostic/test-nvidia-connection` | Test NVIDIA API connectivity |

### Request Schema

```json
{
  "prompt": "string (required) - Your coding question or task",
  "agentType": "CODE_GENERATION | CODE_REVIEW | CODE_EXPLANATION (required)",
  "streaming": "boolean (optional, default: false)"
}
```

### Response Schema

```json
{
  "content": "string - AI-generated response",
  "agentType": "string - Agent that processed the request",
  "timestamp": "string - ISO 8601 timestamp",
  "error": "string | null - Error message if any",
  "metadata": {
    "model": "string - Model used",
    "responseId": "string - Unique response ID"
  }
}
```

## 🐳 Docker Deployment

### Build and Run

```bash
# Build image
docker build -t gemma4-code-assistant .

# Run container
docker run -p 8080:8080 \
  -e NVIDIA_API_KEY=your-key-here \
  gemma4-code-assistant
```

### Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=NvidiaApiClientTest
```

### Test Coverage

```bash
mvn clean test jacoco:report
```

View coverage report at: `target/site/jacoco/index.html`

### Test Statistics

- **Total Tests**: 85
- **Unit Tests**: 85
- **Integration Tests**: Included
- **Coverage**: High coverage across all components

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Application                       │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            │ HTTP/REST
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   AssistantController                        │
│                  (REST API Endpoints)                        │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   AgentOrchestrator                          │
│              (Routes to appropriate agent)                   │
└───────────────────────────┬─────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│    Code      │   │    Code      │   │    Code      │
│ Generation   │   │   Review     │   │ Explanation  │
│    Agent     │   │    Agent     │   │    Agent     │
└──────┬───────┘   └──────┬───────┘   └──────┬───────┘
       │                  │                   │
       └──────────────────┼───────────────────┘
                          │
                          ▼
                ┌──────────────────┐
                │ NvidiaApiClient  │
                │  (WebClient)     │
                └─────────┬────────┘
                          │
                          │ HTTPS
                          ▼
                ┌──────────────────┐
                │   NVIDIA API     │
                │ (Gemma 4 31B)    │
                └──────────────────┘
```

### Component Descriptions

- **AssistantController**: REST API endpoints, request validation
- **AgentOrchestrator**: Routes requests to appropriate specialized agent
- **Agents**: Specialized AI agents with domain-specific prompts
- **NvidiaApiClient**: Handles communication with NVIDIA API
- **Models**: DTOs for request/response serialization

### Key Design Patterns

- **Strategy Pattern**: Different agents for different tasks
- **Builder Pattern**: Fluent API for request construction
- **Reactive Programming**: WebFlux for non-blocking I/O
- **Dependency Injection**: Spring's IoC container

## 📁 Project Structure

```
Gemma4-Code-Assistant-API/
├── src/
│   ├── main/
│   │   ├── java/com/assistant/multiagent/
│   │   │   ├── client/              # NVIDIA API client
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── exception/           # Exception handlers
│   │   │   ├── model/               # DTOs and models
│   │   │   ├── service/             # Business logic (agents)
│   │   │   └── MultiAgentAssistantApplication.java
│   │   └── resources/
│   │       ├── application.yml      # Application config
│   │       └── logback-spring.xml   # Logging config
│   └── test/                        # Unit and integration tests
├── docker-compose.yml               # Docker Compose config
├── Dockerfile                       # Docker image definition
├── pom.xml                          # Maven dependencies
├── README.md                        # This file
├── LICENSE                          # MIT License
└── .gitignore                       # Git ignore rules
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google** for the Gemma 4 model
- **NVIDIA** for providing the API infrastructure
- **Spring Team** for the excellent Spring Boot framework
- **OpenAPI Initiative** for API documentation standards

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/<YOUR_USERNAME>/Gemma4-Code-Assistant-API/issues)
- **Discussions**: [GitHub Discussions](https://github.com/<YOUR_USERNAME>/Gemma4-Code-Assistant-API/discussions)

## 🗺️ Roadmap

- [ ] Add support for more AI models
- [ ] Implement caching for common requests
- [ ] Add rate limiting
- [ ] Create Python SDK
- [ ] Add WebSocket support for real-time streaming
- [ ] Implement user authentication
- [ ] Add metrics and monitoring dashboard

---

**Made with ❤️ using Spring Boot and Google Gemma 4**

