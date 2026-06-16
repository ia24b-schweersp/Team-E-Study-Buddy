@echo off
REM Study Buddy - Diagnostics & Quick Start Script (Windows)

echo.
echo ================================
echo Study Buddy - Diagnostics
echo ================================
echo.

REM 1. Prüfe Java
echo [1/5] Java-Version prüfen...
where java >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    java -version
    echo OK: Java gefunden
) else (
    echo ERROR: Java nicht gefunden
    echo Bitte Java 17+ installieren
    pause
    exit /b 1
)
echo.

REM 2. Prüfe Maven
echo [2/5] Maven-Version prüfen...
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    mvn -version | findstr "Apache"
    echo OK: Maven gefunden
) else (
    echo ERROR: Maven nicht gefunden
    echo Bitte Maven installieren
    pause
    exit /b 1
)
echo.

REM 3. Prüfe Node
echo [3/5] Node-Version prüfen...
where node >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    node -v
    echo OK: Node gefunden
) else (
    echo ERROR: Node nicht gefunden
    echo Bitte Node 18+ installieren
    pause
    exit /b 1
)
echo.

REM 4. Prüfe npm
echo [4/5] npm-Version prüfen...
where npm >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    npm -v
    echo OK: npm gefunden
) else (
    echo ERROR: npm nicht gefunden
    pause
    exit /b 1
)
echo.

REM 5. Prüfe Port 8080
echo [5/5] Port 8080 prüfen...
netstat -an | findstr ":8080" >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo WARNING: Port 8080 ist bereits in use
    netstat -ano | findstr ":8080"
) else (
    echo OK: Port 8080 frei
)
echo.

echo ================================
echo Diagnose abgeschlossen
echo ================================
echo.
echo.
echo WICHTIG - Folge diesen Schritten:
echo.
echo 1. BACKEND STARTEN (Terminal 1):
echo    cd backend
echo    mvn clean compile
echo    mvn spring-boot:run
echo.
echo 2. FRONTEND STARTEN (Terminal 2):
echo    cd frontend
echo    npm install
echo    npm run dev
echo.
echo 3. BROWSER ÖFFNEN:
echo    http://localhost:5173
echo.
echo 4. REGISTRIERE MINDESTENS 2 BENUTZER:
echo    - Benutzer 1: alice / alice@test.com / test123
echo    - Benutzer 2: bob / bob@test.com / test123
echo.
echo 5. PROFIL KOMPLETT AUSFÜLLEN:
echo    - Vorname, Nachname, Bio, Schule (ALLE Felder!)
echo    - Speichern
echo.
echo 6. MIT ERSTEM BENUTZER ANMELDEN:
echo    - Sollte Dashboard mit Matches sehen
echo.
echo Wenn noch Probleme:
echo → Siehe: TROUBLESHOOTING_404.md
echo.
pause

