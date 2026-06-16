#!/bin/bash
# Study Buddy - Diagnostics & Troubleshooting Script

echo "================================"
echo "Study Buddy - Diagnostic Script"
echo "================================"
echo ""

# 1. Prüfe Java
echo "[1/5] Java-Version prüfen..."
if command -v java &> /dev/null
then
    java -version
    echo "✓ Java OK"
else
    echo "✗ Java nicht gefunden"
    exit 1
fi
echo ""

# 2. Prüfe Maven
echo "[2/5] Maven-Version prüfen..."
if command -v mvn &> /dev/null
then
    mvn -version | head -1
    echo "✓ Maven OK"
else
    echo "✗ Maven nicht gefunden"
    exit 1
fi
echo ""

# 3. Prüfe Node
echo "[3/5] Node-Version prüfen..."
if command -v node &> /dev/null
then
    node -v
    echo "✓ Node OK"
else
    echo "✗ Node nicht gefunden"
    exit 1
fi
echo ""

# 4. Prüfe Port 8080
echo "[4/5] Port 8080 prüfen..."
if lsof -i :8080 &> /dev/null || netstat -an 2>/dev/null | grep -q 8080
then
    echo "⚠ Port 8080 ist bereits in use"
    lsof -i :8080 || netstat -an | grep 8080
else
    echo "✓ Port 8080 frei"
fi
echo ""

# 5. Prüfe Datenbank
echo "[5/5] Datenbank-Dateien prüfen..."
if [ -f "backend/data/studybuddy.mv.db" ]
then
    echo "✓ Datenbank-Datei vorhanden"
    ls -lh backend/data/studybuddy.mv.db
else
    echo "⚠ Datenbank-Datei nicht vorhanden (wird beim Start erstellt)"
fi
echo ""

echo "================================"
echo "✓ Diagnose abgeschlossen"
echo "================================"
echo ""
echo "Nächste Schritte:"
echo "1. Backend starten: cd backend && mvn spring-boot:run"
echo "2. Frontend starten (neues Terminal): cd frontend && npm run dev"
echo "3. Browser öffnen: http://localhost:5173"

