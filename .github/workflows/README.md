# GitHub Actions CI/CD Pipeline

This directory contains the automated CI/CD workflows for the Multi-Agent Coding Assistant project.

## Workflows

### CI/CD Pipeline (`ci.yml`)

Automatically runs on every push to the `main` branch and on all pull requests targeting `main`.

#### Jobs

1. **Build and Test**
   - Sets up Java 17 (Temurin distribution)
   - Caches Maven dependencies for faster builds
   - Builds the project with Maven
   - Runs all unit and integration tests
   - Uploads test results as artifacts
   - Generates test reports

2. **Docker Build Validation**
   - Builds the Docker image
   - Validates the image was created successfully
   - Runs a health check against the containerized application

## Required GitHub Secrets

To run the CI/CD pipeline successfully, the following secrets must be configured in your GitHub repository:

### NVIDIA_API_KEY

- **Purpose**: Required for integration tests that interact with the NVIDIA API
- **How to set**: 
  1. Go to your repository on GitHub
  2. Navigate to Settings → Secrets and variables → Actions
  3. Click "New repository secret"
  4. Name: `NVIDIA_API_KEY`
  5. Value: Your NVIDIA API key (obtain from https://build.nvidia.com/)
  6. Click "Add secret"

**Note**: If you don't have an NVIDIA API key, some integration tests may be skipped or fail. The build will still succeed if only unit tests pass.

## Local Testing

To test the workflow locally before pushing:

```bash
# Install act (GitHub Actions local runner)
# On Windows with Chocolatey:
choco install act-cli

# On macOS with Homebrew:
brew install act

# Run the workflow locally
act -j build-and-test
act -j docker-build-validation
```

## Workflow Triggers

The CI/CD pipeline is triggered by:

- **Push events** to the `main` branch
- **Pull request events** targeting the `main` branch

## Build Status

The build status badge can be added to the README.md:

```markdown
![CI/CD Pipeline](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/ci.yml/badge.svg)
```

Replace `YOUR_USERNAME` and `YOUR_REPO` with your actual GitHub username and repository name.
