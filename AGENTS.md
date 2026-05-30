# AGENTS.md — AI Coding Assistant API

## Quick start

```bash
# One-click (Windows)
setup.bat              # prompts for API key, builds, starts
setup.bat rebuild      # force rebuild

# Stop
stop.bat
```

**Prerequisite**: Docker Desktop. Java/Maven not needed locally — all build inside Docker.

## Key architecture

```
POST /api/v1/assist  →  AssistantController  →  AgentOrchestrator  →  Agent (interface)
                                                                      ├── CodeGenerationAgent
                                                                      ├── CodeReviewAgent
                                                                      └── CodeExplanationAgent
                                                                           └── NvidiaApiClient (WebClient)
                                                                                └── integrate.api.nvidia.com/v1/chat/completions
```

- Single controller (`AssistantController`) with two routes: POST `/api/v1/assist` (JSON) and POST `/api/v1/assist` with `Accept: text/event-stream` (SSE).
- `AgentOrchestrator` uses `EnumMap<AgentType, Agent>`. New agent = new `@Service` impl + add to orchestrator constructor.
- All agents build `NvidiaRequest` (system prompt + user prompt), call client, extract `NvidiaResponse.choices[0].message.content`.
- Streaming returns `Flux<ServerSentEvent<String>>`.

## Config

| Env var | Default |
|---------|---------|
| `NVIDIA_API_KEY` | required (fail-fast) |
| `NVIDIA_API_URL` | `https://integrate.api.nvidia.com/v1/chat/completions` |
| `NVIDIA_MODEL` | `meta/llama-3.1-8b-instruct` |
| `SERVER_PORT` | 8080 |
| `REQUEST_TIMEOUT_SECONDS` | 60 (docker-compose: 120) |

## Testing

```bash
docker-compose exec assistant mvn test
docker-compose exec assistant mvn test -Dtest=NvidiaApiClientTest
docker-compose exec assistant mvn clean test jacoco:report
```

- JUnit 5 + Mockito + okhttp3 `MockWebServer` + `reactor-test`.
- Runtime image has no Maven — tests run via `docker-compose exec` (which uses the build image's maven) or locally with `mvn` installed.

## Docker

- Multi-stage build: `maven:3.9-eclipse-temurin-17` → `eclipse-temurin:17-jre-alpine`.
- Runs as non-root `appuser`. Healthcheck: `wget --spider /api/v1/health` every 30s.
- Service name is `assistant` (not `app`).

## Adding a new agent

1. Create `@Service` implementing `Agent`.
2. Add enum to `AgentType`.
3. Register in `AgentOrchestrator` constructor.
