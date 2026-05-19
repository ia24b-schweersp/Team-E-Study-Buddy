# 🎯 Study Buddy - Vollständige Anleitung für lokale Entwicklung

## 📋 Voraussetzungen erfüllen

### 1️⃣ Java installieren
- **Minimum**: Java 17
- Download: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- **Verify**: Öffne PowerShell und tippe:
  ```powershell
  java -version
  ```
  Sollte zeigen: `openjdk version "17.x.x"`

### 2️⃣ Maven installieren
- **Minimum**: Maven 3.6
- Download: https://maven.apache.org/download.cgi
- **Verify**: 
  ```powershell
  mvn -version
  ```
  Sollte Maven-Version anzeigen

### 3️⃣ Node.js installieren
- **Minimum**: Node.js 18
- Download: https://nodejs.org/
- **Verify**:
  ```powershell
  node --version
  npm --version
  ```
  Sollte zeigen: v18.x.x oder höher

## 🚀 Schnellstart - 3 Schritte

### Option A: Automatisch (empfohlen!)

```powershell
# Doppelklick auf diese Datei im Explorer:
run.bat
```

Dies startet automatisch beide Services!

### Option B: Manuell

#### 📝 Terminal 1 - Backend starten:
```powershell
cd C:\workarea\html\Team-E-Study-Buddy2
cd backend
mvn clean install
mvn spring-boot:run
```

**Warte bis du siehst:**
```
Started StudyBuddyApplication in X.XXX seconds
```

#### 📝 Terminal 2 - Frontend starten:
```powershell
cd C:\workarea\html\Team-E-Study-Buddy2
cd frontend
npm install
npm run dev
```

**Browser sollte automatisch öffnen:** http://localhost:5173

## ✅ Funktionalität testen

### 🔐 Registrierung testen:
1. Klick auf "Registrierung"
2. Fülle aus:
   - Benutzername: `alice123`
   - Email: `alice@example.com`
   - Passwort: `secure123`
   - Bestätigung: `secure123`
3. Klick "Registrieren"
4. ✅ Du solltest automatisch angemeldet werden

### 👤 Profil erstellen:
1. Jetzt siehst du "Mein Profil" Seite
2. Fülle aus:
   - Vorname: `Alice`
   - Nachname: `Schmidt`
   - Bio: `Ich lerne gerne neue Dinge!`
   - Schule: `Gymnasium Beispielstadt`
3. Klick "Profil speichern"
4. ✅ Success-Message erscheint oben rechts

### 🔓 Logout & Login testen:
1. Klick "Logout" oben rechts
2. Du siehst wieder Login-Seite
3. Klick "Login"
4. Fülle ein:
   - Email: `alice@example.com`
   - Passwort: `secure123`
5. Klick "Anmelden"
6. ✅ Dein Profil sollte geladen werden

## 🗄️ Datenbank verwalten

### H2 Console öffnen:
1. Öffne Browser: http://localhost:8080/h2-console
2. **Settings:**
   - URL: `jdbc:h2:file:./data/studybuddy;MODE=MySQL`
   - Username: `sa`
   - Password: (leer lassen)
   - Klick "Connect"

### Beispiel SQL Queries:

```sql
-- Alle Benutzer anschauen
SELECT id, username, email, created_at FROM users;

-- Alle Profile anschauen
SELECT p.id, p.user_id, p.first_name, p.last_name, p.bio FROM profiles p;

-- Benutzer mit Profil joinen
SELECT u.id, u.username, u.email, p.first_name, p.last_name 
FROM users u 
LEFT JOIN profiles p ON u.id = p.user_id;

-- Spezifischen Benutzer löschen (falls nötig)
DELETE FROM profiles WHERE user_id = 1;
DELETE FROM users WHERE id = 1;
```

## 🔍 Debugging

### 📊 Browser DevTools (F12)

**Tab: Network**
- Zeigt alle API-Requests
- Klick auf Request → "Preview" um Response zu sehen
- Perfekt zum Debuggen von Login/Register

**Tab: Console**
- Zeige User-Session: `JSON.parse(localStorage.getItem('currentUser'))`
- Lösche Session: `localStorage.clear()`

**Tab: Application → Local Storage**
- `currentUser` speichert: userId, email, username

### 📝 Backend Logs

Schau in das Terminal, wo `mvn spring-boot:run` läuft:

```
[DEBUG] GET /api/profile/1 - Profil abrufen
[INFO]  Login erfolgreich für Benutzer: 1
[ERROR] Email oder Passwort ist ungültig
```

## ⚠️ Häufige Fehler & Lösungen

### ❌ "Fehler beim Login: Request Timeout"
**Ursache:** Backend läuft nicht
**Lösung:**
1. Check: `http://localhost:8080` im Browser
2. Wenn nicht erreichbar → Terminal mit Backend starten
3. Warte 10 Sekunden für Startup
4. Refresh im Frontend Browser

### ❌ "Cannot find module 'vite'"
**Ursache:** npm dependencies nicht installiert
**Lösung:**
```powershell
cd frontend
npm install
npm run dev
```

### ❌ "Port 8080 bereits in Verwendung"
**Ursache:** Anderer Prozess nutzt Port 8080
**Lösung:**
```powershell
# Finde den Prozess
netstat -ano | findstr :8080

# Oder: Andere Port verwenden
# Bearbeite backend/src/main/resources/application.properties
# server.port=8081
```

### ❌ "Datenbank Connection Error"
**Ursache:** H2 File-Locks
**Lösung:**
```powershell
# Lösche Datenbank-Dateien
cd backend
Remove-Item -Recurse data/
# Starte Backend neu
mvn spring-boot:run
```

## 📦 Die wichtigsten Dateien verstehen

### Backend

**AuthService.java** - Die Geschäftslogik
```java
public AuthResponse register(RegisterRequest request) {
    // Validiert Registrierung
    // Speichert User in DB
    // Gibt Response zurück
}
```

**AuthController.java** - Die REST Endpoints
```java
@PostMapping("/register")
public ResponseEntity<AuthResponse> register(...) {
    // Ruft AuthService auf
    // Gibt HTTP Response zurück
}
```

**application.properties** - Konfiguration
```properties
spring.datasource.url=jdbc:h2:file:./data/studybuddy
# H2 Datenbank speichert in data/ Folder
```

### Frontend

**index.html** - Alle UI Views
```html
<div id="loginView">...</div>
<div id="registerView">...</div>
<div id="profileView">...</div>
```

**main.js** - App-Logik & Interaktionen
```javascript
handleLogin() {
    // Liest Form aus
    // Ruft API auf
    // Speichert Session
    // Wechselt View
}
```

**api.js** - Backend Communication
```javascript
async login(email, password) {
    // Sendet POST zum Backend
    // Bekommt Response
    // Gibt Daten zurück
}
```

**style.css** - Alles Design
```css
:root {
    --primary-color: #90EE90;
    /* Hellgrün für das ganze Design */
}
```

## 🎯 Verständnis-Checkliste

Nachdem du die App getestet hast, versuche folgende Fragen zu beantworten:

- [ ] Wo wird das User-Passwort gespeichert? (`users` Tabelle in H2)
- [ ] Wie bleibe ich angemeldet, wenn ich den Browser neuladen? (`localStorage`)
- [ ] Wo ist die Login-API definiert? (`AuthController.java`)
- [ ] Wie sieht eine erfolgreiche Login-Response aus? (JSON: `{userId, email, username, success}`)
- [ ] Warum ist das Frontend grün? (CSS Variable `--primary-color: #90EE90`)
- [ ] Wie viele HTML-Dateien gibt es? (Eine: `index.html` mit allen Views)
- [ ] Was macht der "Service Layer"? (Geschäftslogik, Validierung)

## 📞 API-Requests mit Tools testen

### Mit Postman oder cURL:

**Registrierung:**
```bash
curl -X POST http://localhost:8080/api/auth/register `
  -H "Content-Type: application/json" `
  -d @-
```

Dann eingeben:
```json
{
  "username": "bob",
  "email": "bob@example.com",
  "password": "test123",
  "confirmPassword": "test123"
}
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"bob@example.com\",\"password\":\"test123\"}"
```

## 📚 Code-Struktur verstehen

### Backend-Flow bei Login:

```
1. UI: User klickt "Anmelden"
   ↓
2. Frontend: handleLogin() in main.js
   ↓
3. API: ApiService.login() in api.js
   ↓
4. HTTP: POST /api/auth/login
   ↓
5. Backend: AuthController.login()
   ↓
6. Service: AuthService.login()
   ↓
7. Repository: userRepository.findByEmail()
   ↓
8. Database: SELECT * FROM users WHERE email = ?
   ↓
9. Response: JSON mit userId, email, success
   ↓
10. Frontend: Speichert in localStorage
    Zeigt Profile-View
    Aktualisiert Navbar
```

### Frontend-Flow für Navigation:

```
HTML (5 Views in index.html)
  ↓
JavaScript (App-Klasse)
  ↓
showView() Methode
  ↓
CSS Klassen (active/hidden)
  ↓
User sieht nur eine View
```

## 🎓 Nächste Schritte zum Lernen

1. **Backend verstehen:**
   - Öffne `AuthService.java`
   - Lies die `register()` Methode
   - Schau was sie macht

2. **Frontend verstehen:**
   - Öffne `main.js`
   - Find `handleLogin()`
   - Trace den Code bis zur API

3. **Datenbank verstehen:**
   - Öffne H2 Console
   - Schreib SQL: `SELECT * FROM users`
   - Sieh deine Testdaten

4. **Feature hinzufügen:**
   - Merk dir: Controller → Service → Repository Pattern
   - Kopiere eine bestehende Funktion
   - Ändere sie ab

## 💾 Speichern & Verwalten

### Code pushen zu Git:
```powershell
git add .
git commit -m "Study Buddy Fullstack App v1.0"
git push
```

### Backup machen:
```powershell
# Backup ohne node_modules (groß!)
$exclude = @('backend\target', 'frontend\node_modules', '.git')
# oder: Kopiere nur src/, src/main/java, frontend/src
```

---

**🎉 Viel Spaß beim Entwickeln!**

Bei Fragen: Schau in DEVELOPER_GUIDE.md oder Project_STATUS.md

