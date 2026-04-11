# Contributing to Multi-Agent Coding Assistant

Thank you for your interest in contributing to the Multi-Agent Coding Assistant! We welcome contributions from the community and are grateful for your support. This document provides guidelines and instructions for contributing to the project.

## Table of Contents

- [Development Environment Setup](#development-environment-setup)
- [Code Style and Conventions](#code-style-and-conventions)
- [Testing Requirements](#testing-requirements)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting Guidelines](#issue-reporting-guidelines)
- [Code Review Process](#code-review-process)
- [Code of Conduct](#code-of-conduct)

## Development Environment Setup

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+** for dependency management and building
- **Docker** (optional, for containerized deployment)
- **Git** for version control
- A Java IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions recommended)

### Local Setup

1. **Fork the repository** on GitHub

2. **Clone your fork** to your local machine:
   ```bash
   git clone https://github.com/YOUR-USERNAME/multi-agent-assistant.git
   cd multi-agent-assistant
   ```

3. **Add the upstream repository** as a remote:
   ```bash
   git remote add upstream https://github.com/ORIGINAL-OWNER/multi-agent-assistant.git
   ```

4. **Configure environment variables**:
   ```bash
   cp .env.example .env
   ```
   Edit `.env` and add your NVIDIA API key:
   ```
   NVIDIA_API_KEY=your-actual-api-key-here
   ```

5. **Install dependencies and build**:
   ```bash
   mvn clean install
   ```

### Running the Application

**Using Maven:**
```bash
mvn spring-boot:run
```

**Using Docker:**
```bash
docker-compose up --build
```

The application will start on `http://localhost:8080`. You can access:
- API endpoints at `http://localhost:8080/api/assist`
- Swagger UI at `http://localhost:8080/swagger-ui.html`
- Health check at `http://localhost:8080/health`

## Code Style and Conventions

### Java Coding Standards

We follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with some project-specific conventions:

- **Indentation**: 4 spaces (no tabs)
- **Line length**: Maximum 120 characters
- **Braces**: Use K&R style (opening brace on same line)
- **Imports**: No wildcard imports, organize imports alphabetically
- **Javadoc**: Required for all public classes, methods, and fields

### Package Structure

The project follows a standard Spring Boot package structure:

```
com.assistant.multiagent
├── client/          # External API clients (NVIDIA API)
├── config/          # Configuration classes
├── controller/      # REST API controllers
├── exception/       # Exception handling
├── model/           # Data models and DTOs
└── service/         # Business logic and agent implementations
```

### Naming Conventions

- **Classes**: PascalCase (e.g., `CodeGenerationAgent`)
- **Methods**: camelCase (e.g., `processRequest`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `DEFAULT_TIMEOUT`)
- **Variables**: camelCase (e.g., `apiResponse`)
- **Packages**: lowercase (e.g., `com.assistant.multiagent.service`)

### Code Quality

- Use meaningful variable and method names
- Keep methods focused and concise (ideally under 50 lines)
- Avoid code duplication (DRY principle)
- Use dependency injection via Spring's `@Autowired` or constructor injection
- Handle exceptions appropriately with proper error messages
- Add logging for important operations using SLF4J

## Testing Requirements

### Unit Test Requirements

All code changes must include appropriate tests:

- **New features**: Must include unit tests covering main functionality and edge cases
- **Bug fixes**: Must include a test that reproduces the bug and verifies the fix
- **Refactoring**: Existing tests must continue to pass

### Test Coverage

- Aim for at least **80% code coverage** for new code
- Critical business logic should have **90%+ coverage**
- Use JUnit 5 for unit tests
- Use Mockito for mocking dependencies
- Use MockWebServer for testing HTTP clients

### Running Tests

**Run all tests:**
```bash
mvn test
```

**Run specific test class:**
```bash
mvn test -Dtest=NvidiaApiClientTest
```

**Run tests with coverage report:**
```bash
mvn clean test jacoco:report
```

Coverage reports will be generated in `target/site/jacoco/index.html`.

### Test Organization

- Place tests in `src/test/java` mirroring the source structure
- Name test classes with `Test` suffix (e.g., `NvidiaApiClientTest`)
- Use descriptive test method names (e.g., `testProcessRequestWithValidInput`)
- Group related tests using nested test classes with `@Nested`

## Pull Request Process

### Fork and Clone Workflow

1. **Create a feature branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** and commit them with clear messages

3. **Keep your branch updated** with upstream:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

4. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

5. **Open a Pull Request** on GitHub

### Branch Naming Conventions

Use descriptive branch names with prefixes:

- `feature/` - New features (e.g., `feature/add-code-optimization-agent`)
- `bugfix/` - Bug fixes (e.g., `bugfix/fix-null-pointer-in-orchestrator`)
- `docs/` - Documentation updates (e.g., `docs/update-api-examples`)
- `refactor/` - Code refactoring (e.g., `refactor/simplify-agent-interface`)
- `test/` - Test additions or improvements (e.g., `test/add-integration-tests`)

### Commit Message Format

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, no logic change)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Example:**
```
feat(agent): add code optimization agent

Implement a new agent that analyzes code and suggests optimizations
for performance and readability.

Closes #123
```

### Pull Request Checklist

Before submitting your PR, ensure:

- [ ] Code follows the project's style guidelines
- [ ] Self-review of code completed
- [ ] Comments added for complex logic
- [ ] Documentation updated (if applicable)
- [ ] Tests added or updated
- [ ] All tests pass locally
- [ ] No new warnings or errors introduced
- [ ] Commit messages follow conventions
- [ ] Branch is up-to-date with main

## Issue Reporting Guidelines

### Using Issue Templates

When reporting issues, please use the appropriate template:

- **Bug Report**: For reporting bugs or unexpected behavior
- **Feature Request**: For suggesting new features or enhancements

### Providing Necessary Information

**For Bug Reports:**
- Clear description of the issue
- Steps to reproduce the problem
- Expected vs. actual behavior
- Environment details (OS, Java version, etc.)
- Relevant logs or error messages
- Screenshots (if applicable)

**For Feature Requests:**
- Problem description (what need does this address?)
- Proposed solution
- Alternative solutions considered
- Additional context or examples

### Before Submitting an Issue

1. **Search existing issues** to avoid duplicates
2. **Check the documentation** to ensure it's not a usage question
3. **Verify the issue** with the latest version
4. **Provide minimal reproduction** if reporting a bug

## Code Review Process

### Review Criteria

Pull requests will be reviewed based on:

- **Functionality**: Does the code work as intended?
- **Code Quality**: Is the code clean, readable, and maintainable?
- **Testing**: Are there adequate tests with good coverage?
- **Documentation**: Is the code properly documented?
- **Style**: Does the code follow project conventions?
- **Performance**: Are there any performance concerns?
- **Security**: Are there any security vulnerabilities?

### Response Expectations

- **Initial Review**: Within 2-3 business days
- **Follow-up Reviews**: Within 1-2 business days after updates
- **Merge Decision**: After approval from at least one maintainer

### Addressing Review Feedback

- Respond to all review comments
- Make requested changes in new commits (don't force-push during review)
- Mark conversations as resolved after addressing them
- Request re-review after making changes

### Review Etiquette

- Be respectful and constructive in feedback
- Assume good intentions
- Focus on the code, not the person
- Provide specific, actionable suggestions
- Acknowledge good work and improvements

## Code of Conduct

This project adheres to a Code of Conduct that all contributors are expected to follow. Please read [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) to understand the standards of behavior we expect from our community.

By participating in this project, you agree to abide by its terms.

---

Thank you for contributing to the Multi-Agent Coding Assistant! Your efforts help make this project better for everyone.
