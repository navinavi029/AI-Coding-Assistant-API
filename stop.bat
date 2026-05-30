@echo off
REM ============================================
REM AI Coding Assistant - Stop Script
REM ============================================

echo.
echo ==========================================
echo   Stopping Coding Assistant
echo ==========================================
echo.

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not running!
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo [INFO] Stopping container...
docker-compose stop

if errorlevel 1 (
    echo.
    echo [ERROR] Failed to stop container
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   Container stopped successfully!
echo ==========================================
echo.
echo To start again, run: setup.bat
echo To remove container, run: docker-compose down
echo.

pause
