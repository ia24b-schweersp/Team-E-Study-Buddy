# 🚀 Study Buddy - Quick Start Guide

## 📋 Voraussetzungen

- **Java 17+** → [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.6+** → [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** → [Download](https://nodejs.org/)
- **Git** (optional)

## ⚡ Schnellstart (5 Minuten)

### 1️⃣ Backend starten

```bash
# Terminal 1 - Backend
cd backend
mvn clean install
mvn spring-boot:run
```

**Erwartete Ausgabe:**
```
Started StudyBuddyApplication in X seconds
```

✅ Backend läuft auf: **http://localhost:8080**

### 2️⃣ Frontend starten

```bash
# Terminal 2 - Frontend
cd frontend
npm install
npm run dev
```

**Erwartete Ausgabe:**
```
VITE v5.0.0 ready in XXX ms
➜ Local: http://localhost:5173/
```

✅ Frontend öffnet sich automatisch: **http://localhost:5173**

## 🧪 Erste Schritte im UI

### Test-Szenario:



1. **Registrierung:**
   - Benutzername: `testuser`
   - Email: `test@example.com`
   - Passwort: `password123`
   - Bestätigung: `password123`
   - ✅ Klick auf "Registrieren"

2. **Profil erstellen:**
   - Vorname: `Max`
   - Nachname: `Mustermann`
   - Bio: `Ich liebe Mathe und Englisch`
   - Schule: `Gymnasium Muststadt`
   - ✅ Klick auf "Profil speichern"

3. **Logout:**
   - ✅ Klick auf "Logout" in der Navigation

4. **Login:**
   - Email: `test@example.com`
   - Passwort: `password123`
   - ✅ Klick auf "Anmelden"

## 🗄️ Datenbank anschauen

**H2 Console öffnen:**
1. Gehe zu: http://localhost:8080/h2-console
2. **URL:** `jdbc:h2:file:./data/studybuddy;MODE=MySQL`
3. **Username:** `sa`
4. **Password:** (leer lassen)
5. ✅ Klick auf "Connect"

**Tabellen mit Daten ansehen:**
```sql
SELECT * FROM users;
SELECT * FROM profiles;
```

## 📁 Projektstruktur

```
Team-E-Study-Buddy2/
├── backend/               # Spring Boot REST API
│   ├── src/main/java/    # Java Source Code
│   └── pom.xml           # Maven Dependencies
│
├── frontend/              # Vite + Vanilla JS
│   ├── src/
│   │   ├── main.js       # Main App Logic
│   │   ├── api.js        # API Client
│   │   └── style.css     # Styling
│   ├── index.html        # Main HTML
│   └── package.json      # NPM Dependencies
│
└── README.md             # Detaillierte Dokumentation
```

## 🔌 API Endpoints testen

### Mit cURL/Postman:

**Registrierung:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 🐛 Troubleshooting

| Problem | Lösung |
|---------|--------|
| **Port 8080 bereits belegt** | `netstat -ano \| findstr :8080` dann den Prozess beenden |
| **npm install fehlgeschlagen** | `npm cache clean --force` und `npm install` wiederholen |
| **"Cannot find module" Error** | `cd frontend` → `npm install` |
| **Maven nicht gefunden** | Java PATH prüfen: `java -version` |
| **Datenbank-Fehler** | Lösche `backend/data/` Folder und starte neu |

## 💡 Tipps

- 📊 **H2 Console:** Perfekt zum Debuggen - http://localhost:8080/h2-console
- 🔍 **Browser DevTools:** F12 → Network Tab zum API-Debugging nutzen
- 📝 **Logs:** Backend-Logs im Terminal - sehr hilfreich!
- 🔄 **Hot Reload:** Frontend reloaded automatisch beim Speichern

## 📚 Nächste Schritte

1. **Code verstehen:** Schau dir `backend/src/main/java` an
2. **Frontend anpassen:** Editiere `frontend/src/style.css` für Design-Änderungen
3. **Features hinzufügen:** Neue API-Endpoints in `AuthController.java`
4. **Deployment:** Mit Docker: `docker-compose up`

## 🎯 Häufig verwendete Befehle

```bash
# Backend
cd backend
mvn clean install          # Dependencies installieren
mvn spring-boot:run        # App starten

# Frontend
cd frontend
npm install               # Dependencies installieren
npm run dev               # Dev-Server mit Hot-Reload
npm run build             # Für Produktion bauen

# Beide zusammen (separate Terminals!)
# Terminal 1:
cd backend && mvn spring-boot:run

# Terminal 2:
cd frontend && npm run dev
```

## ✨ Erfolgreich!

Wenn alles funktioniert:
- ✅ Backend läuft auf Port 8080
- ✅ Frontend läuft auf Port 5173
- ✅ Datenbank H2 erstellt automatisch Tabellen
- ✅ Benutzer können sich registrieren, anmelden und Profil erstellen

**Viel Spaß mit Study Buddy! 📚✨**

