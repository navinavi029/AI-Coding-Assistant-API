#!/usr/bin/env pwsh
# Pre-Push Security Check Script

Write-Host "Running Pre-Push Security Check..." -ForegroundColor Cyan
Write-Host ""

$errors = 0
$warnings = 0

# Check 1: .gitignore exists
Write-Host "Checking .gitignore exists..." -NoNewline
if (Test-Path .gitignore) {
    Write-Host " PASS" -ForegroundColor Green
} else {
    Write-Host " FAIL" -ForegroundColor Red
    $errors++
}

# Check 2: .env is NOT present
Write-Host "Checking .env is not present..." -NoNewline
if (Test-Path .env) {
    Write-Host " WARNING" -ForegroundColor Yellow
    $warnings++
} else {
    Write-Host " PASS" -ForegroundColor Green
}

# Check 3: Test scripts exist (will be ignored)
Write-Host "Checking test scripts (will be ignored)..." -NoNewline
$testCount = 0
if (Test-Path "test_nvidia_api.py") { $testCount++ }
if (Test-Path "test_nvidia_direct.ps1") { $testCount++ }
if (Test-Path "test_api_simple.ps1") { $testCount++ }
Write-Host " PASS ($testCount found)" -ForegroundColor Green

# Check 4: Scan source files for API keys
Write-Host "Scanning source files for API keys..." -NoNewline
$sourceFiles = Get-ChildItem -Path src -Recurse -Include *.java,*.yml,*.yaml -File -ErrorAction SilentlyContinue
$foundKeys = $false
foreach ($file in $sourceFiles) {
    $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
    if ($content -match "nvapi-[a-zA-Z0-9_-]{40}") {
        Write-Host " FAIL" -ForegroundColor Red
        Write-Host "  Found API key in: $($file.Name)" -ForegroundColor Red
        $errors++
        $foundKeys = $true
        break
    }
}
if (-not $foundKeys) {
    Write-Host " PASS" -ForegroundColor Green
}

# Check 5: application.yml uses env vars
Write-Host "Checking application.yml uses env vars..." -NoNewline
if (Test-Path "src/main/resources/application.yml") {
    $appYml = Get-Content "src/main/resources/application.yml" -Raw
    if ($appYml -match "NVIDIA_API_KEY") {
        Write-Host " PASS" -ForegroundColor Green
    } else {
        Write-Host " FAIL" -ForegroundColor Red
        $errors++
    }
} else {
    Write-Host " WARNING" -ForegroundColor Yellow
    $warnings++
}

# Check 6: .env.example exists
Write-Host "Checking .env.example exists..." -NoNewline
if (Test-Path .env.example) {
    Write-Host " PASS" -ForegroundColor Green
} else {
    Write-Host " WARNING" -ForegroundColor Yellow
    $warnings++
}

# Check 7: README exists and has placeholders
Write-Host "Checking README uses placeholders..." -NoNewline
if (Test-Path README.md) {
    $readme = Get-Content README.md -Raw
    if ($readme -match "nvapi-[a-zA-Z0-9_-]{40}") {
        Write-Host " FAIL" -ForegroundColor Red
        Write-Host "  README contains real API key!" -ForegroundColor Red
        $errors++
    } elseif ($readme -match "YOUR_USERNAME") {
        Write-Host " WARNING" -ForegroundColor Yellow
        Write-Host "  README has YOUR_USERNAME placeholder" -ForegroundColor Yellow
        $warnings++
    } else {
        Write-Host " PASS" -ForegroundColor Green
    }
} else {
    Write-Host " FAIL" -ForegroundColor Red
    $errors++
}

# Summary
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Security Check Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($errors -eq 0 -and $warnings -eq 0) {
    Write-Host "ALL CHECKS PASSED" -ForegroundColor Green
    Write-Host ""
    Write-Host "Repository is SAFE to push to GitHub!" -ForegroundColor Green
    exit 0
} elseif ($errors -eq 0) {
    Write-Host "$warnings WARNING(S) FOUND" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Repository is safe to push, but review warnings." -ForegroundColor Yellow
    exit 0
} else {
    Write-Host "$errors ERROR(S) FOUND" -ForegroundColor Red
    if ($warnings -gt 0) {
        Write-Host "$warnings WARNING(S) FOUND" -ForegroundColor Yellow
    }
    Write-Host ""
    Write-Host "DO NOT PUSH - Fix errors first!" -ForegroundColor Red
    exit 1
}
