# Multi-Agent Coding Assistant

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green)

## Overview

An intelligent Java-based application that leverages multiple specialized AI agents to provide comprehensive coding assistance. Built with Spring Boot and integrated with NVIDIA's AI API, this system orchestrates different agents to handle code generation, code review, and code explanation tasks efficiently.

## Features

- **Code Generation** - Generate code snippets, functions, and complete implementations
- **Code Review** - Automated code review with suggestions and best practices
- **Code Explanation** - Detailed explanations of code functionality and design patterns
- **NVIDIA API Integration** - Powered by NVIDIA's advanced AI models
- **Multi-Agent Orchestration** - Intelligent routing to specialized agents
- **RESTful API** - Simple HTTP endpoints for easy integration
- **Docker Support** - Containerized deployment
- **OpenAPI Documentation** - Interactive API documentation via Swagger UI

## Prerequisites

- **Docker Desktop** - [Download here](https://www.docker.com/products/docker-desktop)
- **NVIDIA API Key** - [Get your key here](https://build.nvidia.com/)

## Quick Start

### One-Click Setup (Recommended)

```batch
setup.bat
```

On first run, you'll be prompted for your NVIDIA API key.

**Options:**
- `setup.bat` - Normal build and start
- `setup.bat rebuild` - Force rebuild of Docker image

### Manual Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/multi-agent-assistant.git
   cd multi-agent-assistant
   ```

2. **Configure environment**
   ```bash
   copy .env.example .env
   ```
   Edit `.env` and add your NVIDIA API key.

3. **Build and run**
   ```bash
   docker-compose up -d
   ```

The application will start on `http://localhost:8080`.

## Configuration

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `NVIDIA_API_KEY` | Your NVIDIA API key | Yes | - |
| `NVIDIA_API_URL` | NVIDIA API endpoint | No | `https://integrate.api.nvidia.com/v1` |
| `NVIDIA_MODEL` | AI model to use | No | `meta/llama-3.1-8b-instruct` |
| `SERVER_PORT` | Application port | No | `8080` |
| `LOG_LEVEL` | Logging level | No | `INFO` |

## Usage

### Health Check

```bash
curl http://localhost:8080/health
```

### Code Generation

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_GENERATION",
    "prompt": "Create a Java function to calculate factorial"
  }'
```

### Code Review

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_REVIEW",
    "prompt": "Review this code: public void process() { String s = null; s.length(); }"
  }'
```

### Code Explanation

```bash
curl -X POST http://localhost:8080/api/assist \
  -H "Content-Type: application/json" \
  -d '{
    "agentType": "CODE_EXPLANATION",
    "prompt": "Explain this code: Stream.of(1,2,3).map(x -> x * 2).collect(Collectors.toList())"
  }'
```

## Docker Commands

```bash
# Start
setup.bat
# or
docker-compose up -d

# Stop
stop.bat
# or
docker-compose down

# View logs
docker-compose logs -f

# Restart
docker-compose restart

# Rebuild
setup.bat rebuild
```

## API Documentation

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Project Structure

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
    └── java/com/assistant/multiagent/  # Tests
```

## Development

### Running Tests

```bash
# In Docker
docker-compose exec app mvn test

# Specific test
docker-compose exec app mvn test -Dtest=NvidiaApiClientTest

# With coverage
docker-compose exec app mvn clean test jacoco:report
```

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Indentation: 4 spaces
- Line length: 120 characters max
- Javadoc required for public classes and methods

### Making Changes

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make changes and test
3. Rebuild: `setup.bat rebuild`
4. Test via Swagger UI: `http://localhost:8080/swagger-ui.html`

## Troubleshooting

### NVIDIA API Connection Issues

If you get timeout errors:

1. **Verify API key activation**
   - Go to https://build.nvidia.com
   - Sign in and navigate to your model page
   - Accept Terms of Service if prompted
   - Verify API access is activated

2. **Test API key**
   - Use the Playground feature on NVIDIA's website
   - If it works there, the key is valid

3. **Check API key format**
   - Should start with `nvapi-`
   - No extra spaces or quotes in `.env`
   - Use the latest key (not expired)

4. **Generate new API key**
   - Go to https://build.nvidia.com
   - Generate a new key
   - Update `.env` file
   - Restart: `docker-compose restart`

5. **Try alternative model**
   ```
   NVIDIA_MODEL=meta/llama-3.1-8b-instruct
   ```

6. **Test connection**
   - Open: http://localhost:8080/swagger-ui.html
   - Find "Diagnostic" section
   - Execute "GET /api/v1/diagnostic/test-nvidia-connection"

### Common Errors

- **TimeoutException** → API key not activated or network issue
- **401 Unauthorized** → Invalid API key
- **403 Forbidden** → No access to this model
- **429 Too Many Requests** → Rate limit exceeded
- **500 Internal Server Error** → NVIDIA API issue (retry later)

## Security

### Best Practices

- Keep NVIDIA API key secure, never commit to version control
- Use environment variables for sensitive configuration
- Run with minimal required permissions
- Use HTTPS for API communications
- Implement rate limiting for production
- Regularly review logs for suspicious activity

### Reporting Vulnerabilities

**Do NOT report security vulnerabilities through public GitHub issues.**

Report via:
1. GitHub Security Advisories (preferred)
2. Email to project maintainers with "SECURITY" in subject

Include:
- Vulnerability type and location
- Steps to reproduce
- Proof-of-concept (if possible)
- Impact assessment

**Response timeline:**
- Initial response: 48 hours
- Status updates: Every 7 days
- Resolution: Within 30 days for critical issues

## Contributing

Contributions are welcome! Please follow these guidelines:

### Setup

1. Fork the repository
2. Clone your fork
3. Create a feature branch
4. Make changes with tests
5. Submit a pull request

### Branch Naming

- `feature/` - New features
- `bugfix/` - Bug fixes
- `docs/` - Documentation
- `refactor/` - Code refactoring
- `test/` - Test improvements

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>

<body>

<footer>
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Example:
```
feat(agent): add code optimization agent

Implement a new agent that analyzes code and suggests optimizations.

Closes #123
```

### Pull Request Checklist

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] Tests added/updated
- [ ] All tests pass
- [ ] No new warnings
- [ ] Commit messages follow conventions
- [ ] Branch up-to-date with main

### Testing Requirements

- New features must include unit tests
- Bug fixes must include reproduction test
- Aim for 80%+ code coverage
- Use JUnit 5 and Mockito

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Version**: 1.0.0  
**Spring Boot**: 3.2.1  
**Java**: 17
