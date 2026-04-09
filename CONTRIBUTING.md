# Contributing to Gemma4-Code-Assistant-API

Thank you for your interest in contributing! This document provides guidelines and instructions for contributing to this project.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for everyone.

## How to Contribute

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the issue
- **Expected behavior** vs actual behavior
- **Environment details** (OS, Java version, etc.)
- **Logs or error messages** if applicable

### Suggesting Enhancements

Enhancement suggestions are welcome! Please provide:

- **Clear use case** for the enhancement
- **Detailed description** of the proposed functionality
- **Examples** of how it would work
- **Potential implementation approach** (optional)

### Pull Requests

1. **Fork the repository** and create your branch from `main`
2. **Make your changes** following our coding standards
3. **Add tests** for new functionality
4. **Update documentation** as needed
5. **Ensure all tests pass** (`mvn test`)
6. **Commit with clear messages** following conventional commits
7. **Submit a pull request**

## Development Setup

### Prerequisites

- Java 17+
- Maven 3.6+
- Git
- NVIDIA API key

### Local Development

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/Gemma4-Code-Assistant-API.git
cd Gemma4-Code-Assistant-API

# Create a branch
git checkout -b feature/your-feature-name

# Set up environment
cp .env.example .env
# Edit .env with your NVIDIA_API_KEY

# Build and test
mvn clean install
mvn test

# Run locally
mvn spring-boot:run
```

## Coding Standards

### Java Style Guide

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise
- Use dependency injection via constructor

### Code Organization

```java
// Good example
@Service
public class CodeGenerationAgent implements Agent {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerationAgent.class);
    private static final String SYSTEM_PROMPT = "...";
    
    private final NvidiaApiClient nvidiaApiClient;
    private final AppConfig appConfig;
    
    public CodeGenerationAgent(NvidiaApiClient nvidiaApiClient, AppConfig appConfig) {
        this.nvidiaApiClient = nvidiaApiClient;
        this.appConfig = appConfig;
    }
    
    @Override
    public AssistResponse process(String prompt, boolean streaming) {
        logger.info("Processing request");
        // Implementation
    }
}
```

### Testing Standards

- Write unit tests for all new functionality
- Aim for >80% code coverage
- Use meaningful test names: `shouldReturnErrorWhenApiKeyIsMissing()`
- Mock external dependencies
- Test both success and failure scenarios

```java
@Test
void shouldGenerateCodeSuccessfully() {
    // Given
    String prompt = "Write a hello world function";
    
    // When
    AssistResponse response = agent.process(prompt, false);
    
    // Then
    assertNotNull(response.getContent());
    assertEquals("CODE_GENERATION", response.getAgentType());
    assertNull(response.getError());
}
```

## Commit Message Guidelines

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples

```
feat(agent): add code optimization agent

Add new agent specialized in code optimization and performance improvements.
Includes system prompt and integration with orchestrator.

Closes #123
```

```
fix(client): handle timeout errors gracefully

Improve error handling for NVIDIA API timeouts by adding retry logic
and better error messages.

Fixes #456
```

## Documentation

- Update README.md for user-facing changes
- Add JavaDoc comments for public APIs
- Update API documentation in Swagger annotations
- Include code examples for new features

## Testing Checklist

Before submitting a PR, ensure:

- [ ] All tests pass (`mvn test`)
- [ ] Code compiles without warnings (`mvn clean compile`)
- [ ] New functionality has tests
- [ ] Documentation is updated
- [ ] Code follows style guidelines
- [ ] Commit messages follow conventions
- [ ] No sensitive data (API keys, etc.) in commits

## Review Process

1. **Automated checks** run on PR submission
2. **Code review** by maintainers
3. **Feedback addressed** by contributor
4. **Approval and merge** by maintainers

## Questions?

- Open a [GitHub Discussion](https://github.com/YOUR_USERNAME/Gemma4-Code-Assistant-API/discussions)
- Create an [Issue](https://github.com/YOUR_USERNAME/Gemma4-Code-Assistant-API/issues)

Thank you for contributing! 🎉
