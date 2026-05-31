# AGENTS.md — AI Coding Assistant API

## Quick start

```bash
# One-click (Windows)
run.bat                # start or resume (auto-starts Docker, etc.)
setup.bat              # first-time: prompts for API key, builds, starts
setup.bat rebuild      # force clean rebuild

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

<!-- gitnexus:start -->
# GitNexus — Code Intelligence

This project is indexed by GitNexus as **AI-Coding-Assistant-API** (624 symbols, 1831 relationships, 22 execution flows). Use the GitNexus MCP tools to understand code, assess impact, and navigate safely.

> If any GitNexus tool warns the index is stale, run `npx gitnexus analyze` in terminal first.

## Always Do

- **MUST run impact analysis before editing any symbol.** Before modifying a function, class, or method, run `gitnexus_impact({target: "symbolName", direction: "upstream"})` and report the blast radius (direct callers, affected processes, risk level) to the user.
- **MUST run `gitnexus_detect_changes()` before committing** to verify your changes only affect expected symbols and execution flows.
- **MUST warn the user** if impact analysis returns HIGH or CRITICAL risk before proceeding with edits.
- When exploring unfamiliar code, use `gitnexus_query({query: "concept"})` to find execution flows instead of grepping. It returns process-grouped results ranked by relevance.
- When you need full context on a specific symbol — callers, callees, which execution flows it participates in — use `gitnexus_context({name: "symbolName"})`.

## Never Do

- NEVER edit a function, class, or method without first running `gitnexus_impact` on it.
- NEVER ignore HIGH or CRITICAL risk warnings from impact analysis.
- NEVER rename symbols with find-and-replace — use `gitnexus_rename` which understands the call graph.
- NEVER commit changes without running `gitnexus_detect_changes()` to check affected scope.

## Resources

| Resource | Use for |
|----------|---------|
| `gitnexus://repo/AI-Coding-Assistant-API/context` | Codebase overview, check index freshness |
| `gitnexus://repo/AI-Coding-Assistant-API/clusters` | All functional areas |
| `gitnexus://repo/AI-Coding-Assistant-API/processes` | All execution flows |
| `gitnexus://repo/AI-Coding-Assistant-API/process/{name}` | Step-by-step execution trace |

## CLI

| Task | Read this skill file |
|------|---------------------|
| Understand architecture / "How does X work?" | `.claude/skills/gitnexus/gitnexus-exploring/SKILL.md` |
| Blast radius / "What breaks if I change X?" | `.claude/skills/gitnexus/gitnexus-impact-analysis/SKILL.md` |
| Trace bugs / "Why is X failing?" | `.claude/skills/gitnexus/gitnexus-debugging/SKILL.md` |
| Rename / extract / split / refactor | `.claude/skills/gitnexus/gitnexus-refactoring/SKILL.md` |
| Tools, resources, schema reference | `.claude/skills/gitnexus/gitnexus-guide/SKILL.md` |
| Index, status, clean, wiki CLI commands | `.claude/skills/gitnexus/gitnexus-cli/SKILL.md` |

<!-- gitnexus:end -->
