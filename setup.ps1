# Setup Script für Study Buddy Fullstack-Anwendung
# Für Windows PowerShell

Write-Host "================================" -ForegroundColor Green
Write-Host "Study Buddy - Fullstack Setup" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host ""

# Schritt 1: Backend-Setup
Write-Host "🔧 Starte Backend Setup..." -ForegroundColor Cyan

$backendPath = ".\backend"

if (Test-Path $backendPath) {
    Set-Location $backendPath

    Write-Host "📦 Installiere Backend Dependencies..." -ForegroundColor Yellow
    mvn clean install -q

    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Backend Dependencies installiert" -ForegroundColor Green
    } else {
        Write-Host "❌ Backend Setup fehlgeschlagen" -ForegroundColor Red
        exit 1
    }

    Set-Location ..
} else {
    Write-Host "❌ Backend-Verzeichnis nicht gefunden" -ForegroundColor Red
    exit 1
}

# Schritt 2: Frontend-Setup
Write-Host ""
Write-Host "🔧 Starte Frontend Setup..." -ForegroundColor Cyan

$frontendPath = ".\frontend"

if (Test-Path $frontendPath) {
    Set-Location $frontendPath

    Write-Host "📦 Installiere Frontend Dependencies..." -ForegroundColor Yellow

    if (Get-Command npm -ErrorAction SilentlyContinue) {
        npm install

        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Frontend Dependencies installiert" -ForegroundColor Green
        } else {
            Write-Host "❌ Frontend Setup fehlgeschlagen" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "❌ npm nicht gefunden. Bitte installiere Node.js" -ForegroundColor Red
        exit 1
    }

    Set-Location ..
} else {
    Write-Host "❌ Frontend-Verzeichnis nicht gefunden" -ForegroundColor Red
    exit 1
}

# Erfolg
Write-Host ""
Write-Host "================================" -ForegroundColor Green
Write-Host "✅ Setup abgeschlossen!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host ""
Write-Host "📖 Nächste Schritte:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Backend starten (in neuem Terminal):" -ForegroundColor Yellow
Write-Host "   cd backend" -ForegroundColor White
Write-Host "   mvn spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "2. Frontend starten (in anderem Terminal):" -ForegroundColor Yellow
Write-Host "   cd frontend" -ForegroundColor White
Write-Host "   npm run dev" -ForegroundColor White
Write-Host ""
Write-Host "3. Öffne http://localhost:5173 im Browser" -ForegroundColor Yellow
Write-Host ""

