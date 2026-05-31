@echo off
setlocal enabledelayedexpansion

echo.
echo =============================================
echo   AI Coding Assistant - Quick Run
echo =============================================
echo.

where docker >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker not found
    echo Install Docker Desktop from https://docker.com/
    pause
    exit /b 1
)

docker info >nul 2>&1
if errorlevel 1 (
    echo [WARNING] Docker not running - starting it now...
    if exist "C:\Program Files\Docker\Docker\Docker Desktop.exe" (
        start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    ) else if exist "C:\Program Files (x86)\Docker\Docker Desktop.exe" (
        start "" "C:\Program Files (x86)\Docker\Docker Desktop.exe"
    ) else (
        start "" "Docker Desktop.exe"
    )
    echo Waiting up to 60 seconds for Docker to start...
    set "count=0"
    :wait_loop
    timeout /t 3 /nobreak >nul
    docker info >nul 2>&1
    if !errorlevel! equ 0 goto :docker_ready
    set /a count+=3
    if !count! lss 60 goto :wait_loop
    echo [ERROR] Docker did not start after 60 seconds
    echo Please start Docker Desktop manually and try again
    pause
    exit /b 1
    :docker_ready
    echo [OK] Docker started
)

set "CID="
for /f "tokens=*" %%a in ('docker-compose ps -q assistant 2^>nul') do set "CID=%%a"

if defined CID (
    set "STATUS="
    for /f "tokens=*" %%a in ('docker inspect --format="{{.State.Status}}" !CID! 2^>nul') do set "STATUS=%%a"
    if "!STATUS!"=="running" (
        echo [OK] Container already running
    ) else (
        echo [INFO] Starting existing container...
        docker-compose start
        if errorlevel 1 (
            pause
            exit /b 1
        )
    )
    goto :show_urls
)

echo [INFO] No container found. Running full setup...
echo.
call setup.bat
exit /b %errorlevel%

:show_urls
echo.
echo =============================================
echo   Ready!
echo.
echo   API:      http://localhost:8080
echo   Swagger:  http://localhost:8080/swagger-ui/index.html
echo   Health:   http://localhost:8080/api/v1/health
echo.
echo   Stop:     stop.bat
echo =============================================
start http://localhost:8080/swagger-ui/index.html
pause
exit /b 0
