@echo off
setlocal enabledelayedexpansion

echo.
echo =============================================
echo   Multi-Agent Coding Assistant - Docker Setup
echo =============================================
echo.

set "ERRORS=0"

echo [1/4] Checking Dependencies
echo.

echo Checking: Docker...
where docker >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   [ERROR] Docker not found
    echo   [FIX] Install Docker Desktop from https://docker.com/
    set "ERRORS=1"
    goto :check_compose
)

docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   [WARNING] Docker not running
    echo   Starting Docker Desktop...
    
    if exist "C:\Program Files\Docker\Docker\Docker Desktop.exe" (
        start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    ) else if exist "C:\Program Files (x86)\Docker\Docker Desktop.exe" (
        start "" "C:\Program Files (x86)\Docker\Docker Desktop.exe"
    ) else (
        start "" "Docker Desktop.exe"
    )
    
    echo   Waiting 60 seconds for Docker to start
    call :wait_for_docker
    goto :check_compose
)

echo   [OK] Docker running

:check_compose
echo.
echo Checking: Docker Compose...
where docker-compose >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   [ERROR] docker-compose not found
    set "ERRORS=1"
    goto :check_env
)
echo   [OK] Docker Compose available

:check_env
echo.
echo Checking: NVIDIA API Key...
if not exist ".env" (
    echo   [WARNING] .env not found - will prompt for API key
    goto :verify_done
)
findstr /C:"NVIDIA_API_KEY=" .env >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   [WARNING] NVIDIA_API_KEY not in .env
) else (
    echo   [OK] .env configured
)

:verify_done
if %ERRORS% equ 1 (
    echo.
    echo =============================================
    echo   [ERROR] Dependencies check failed!
    echo =============================================
    pause
    exit /b 1
)

echo.
echo [2/4] All dependencies OK
echo.

echo [3/4] Configuring environment
if not exist ".env" (
    echo.
    echo Get your NVIDIA API Key from: https://build.nvidia.com/
    echo.
    set /p API_KEY="NVIDIA API Key: "

    (
        echo # NVIDIA API Configuration
        echo NVIDIA_API_KEY=%API_KEY%
        echo.
        echo NVIDIA_API_URL=https://integrate.api.nvidia.com/v1/chat/completions
        echo NVIDIA_MODEL=google/gemma-4-31b-it
        echo SERVER_PORT=8080
        echo REQUEST_TIMEOUT_SECONDS=60
        echo LOGGING_LEVEL=INFO
    ) > .env

    echo [OK] .env created
) else (
    echo [OK] .env exists
)

echo.
echo [4/4] Building and starting container
echo.

if "%1"=="rebuild" (
    echo Rebuilding with no cache
    docker-compose build --no-cache
) else (
    docker-compose build
)

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

docker-compose up -d

if %ERRORLEVEL% equ 0 (
    echo.
    echo =============================================
    echo   Started successfully!
    echo.
    echo   API:      http://localhost:8080
    echo   Swagger:  http://localhost:8080/swagger-ui.html
    echo   Health:   http://localhost:8080/health
    echo.
    echo   Stop:     docker-compose down
    echo =============================================
    echo.
    echo Waiting for application to be ready...
    timeout /t 5 /nobreak >nul
    echo Opening Swagger UI in browser...
    start http://localhost:8080/swagger-ui.html
    echo.
    pause
    exit /b 0
) else (
    echo [ERROR] Failed to start
    echo.
    pause
    exit /b 1
)

:wait_for_docker
set "count=0"
:wait_loop
timeout /t 3 /nobreak >nul
docker info >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo   [OK] Docker started
    goto :eof
)
set /a count+=3
if %count% LSS 60 goto :wait_loop
echo   [ERROR] Docker did not start after 60s
echo   Please start Docker Desktop manually
set "ERRORS=1"
goto :eof