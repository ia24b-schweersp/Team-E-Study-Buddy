@echo off
REM Study Buddy Fullstack Application Launcher
REM Windows Batch Script

setlocal enabledelayedexpansion

title Study Buddy - Fullstack Application

cls
echo.
echo ========================================
echo   STUDY BUDDY - FULLSTACK LAUNCHER
echo ========================================
echo.
echo Diese Anwendung wird in 2 separaten Terminals gestartet.
echo.
pause

REM Check Java Version
echo Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java nicht gefunden. Bitte installiere Java 17+
    pause
    exit /b 1
)

REM Check Maven
echo Checking Maven...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven nicht gefunden. Bitte installiere Maven 3.6+
    pause
    exit /b 1
)

REM Check Node.js
echo Checking Node.js...
node --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Node.js nicht gefunden. Bitte installiere Node.js 18+
    pause
    exit /b 1
)

cls
echo ========================================
echo   Dependencies werden ueberprueft...
echo ========================================
echo.

REM Backend Setup
echo [1/4] Backend Dependencies...
cd backend
mvn clean install -q
cd ..

REM Frontend Setup
echo [2/4] Frontend Dependencies...
cd frontend
call npm install -q
cd ..

cls
echo ========================================
echo   Services werden gestartet...
echo ========================================
echo.
echo [3/4] Backend wird in separatem Terminal gestartet...
start "Study Buddy Backend [Port 8080]" cmd /k "cd backend && mvn spring-boot:run"

timeout /t 5 /nobreak

echo [4/4] Frontend wird in separatem Terminal gestartet...
start "Study Buddy Frontend [Port 5173]" cmd /k "cd frontend && npm run dev"

cls
echo ========================================
echo   SUCCESS!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:5173
echo.
echo Die Applikation oeffnet sich automatisch im Browser.
echo.
echo Druecke eine Taste zum Schliessen dieses Fensters...
pause >nul

exit /b 0

