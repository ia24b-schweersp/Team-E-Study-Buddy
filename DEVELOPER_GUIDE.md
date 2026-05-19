# 🛠️ Study Buddy - Developer Guide

## Architecture Overview

### Backend (Java/Spring Boot)

```
Controller Layer (REST API)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (Data Access)
        ↓
Database (H2)
```

### Frontend (Vanilla JavaScript)

```
index.html (Views)
        ↓
main.js (App Logic & DOM Handling)
        ↓
api.js (HTTP Requests)
        ↓
Backend REST API
```

## Backend Structure

### 📦 Packages

```
com.studybuddy
├── controller/           # REST Endpoints
│   ├── AuthController    # /api/auth/*
│   └── ProfileController # /api/profile/*
├── service/              # Business Logic
│   ├── AuthService       # Registration, Login
│   └── ProfileService    # Profile CRUD
├── repository/           # Data Access (JPA)
│   ├── UserRepository
│   └── ProfileRepository
├── model/                # JPA Entities
│   ├── User
│   └── Profile
├── dto/                  # Data Transfer Objects
│   ├── *Request
│   └── *Response
└── StudyBuddyApplication # Main Spring Boot App
```

### 🔄 Data Flow: Registrierung

```
Frontend (index.html)
    ↓
registerForm.submit()
    ↓
App.handleRegister()
    ↓
api.js: ApiService.register()
    ↓
POST /api/auth/register
    ↓
AuthController.register()
    ↓
AuthService.register()
    ↓
UserRepository.save()
    ↓
H2 Database: INSERT INTO users
    ↓
Response: AuthResponse (userId, success, message)
    ↓
localStorage.setItem('currentUser')
    ↓
showProfile() + showMessage()
```

## Frontend Structure

### 📄 File Organization

```
frontend/
├── index.html           # Alle Views (Login, Register, Profile)
├── vite.config.js       # Vite Konfiguration
├── package.json         # NPM Dependencies
└── src/
    ├── main.js          # App Klasse & Event Handling
    ├── api.js           # ApiService Klasse
    └── style.css        # Globales Styling
```

### 🎨 View System

Alle Views sind in **index.html** definiert mit IDs:
- `#loginView` - Login Formular
- `#registerView` - Registrierungs-Formular
- `#profileView` - Profil Anzeige/Bearbeitung

**View-Wechsel:**
```javascript
// Versteckt alle Views, zeigt nur eine
showView('loginView') // oder registerView, profileView
```

### 🌐 API-Kommunikation

**Beispiel: Login**

```javascript
// Frontend (main.js)
const response = await ApiService.login(email, password);

// api.js
static async login(email, password) {
    return this.request('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password })
    });
}

// Resultat: { userId, email, username, message, success }
```

## Development Workflows

### 🐛 Backend Debugging

1. **Logs anschauen:**
   ```
   Console beim Spring Boot Start
   Suche nach: [DEBUG] oder [ERROR]
   ```

2. **H2 Console nutzen:**
   ```
   http://localhost:8080/h2-console
   Schreibe SQL-Queries zum Datenbankzustand prüfen
   ```

3. **Request/Response prüfen:**
   ```
   Browser DevTools → Network Tab
   Klick auf API-Request → Preview Tab
   ```

### 🎯 Frontend Debugging

1. **Browser DevTools (F12):**
   - Console: Fehler + console.log() Ausgaben
   - Network: API-Requests anschauen
   - Application: localStorage prüfen

2. **localStorage inspecten:**
   ```javascript
   // In Console:
   console.log(JSON.parse(localStorage.getItem('currentUser')))
   localStorage.clear() // Reset Session
   ```

3. **Vite Dev Server Logs:**
   - Fenster wo `npm run dev` läuft
   - Zeigt Hot-Reload und Build-Errors

## Adding New Features

### Szenario: Neue Authentifizierungsmethode (z.B. Google Login)

#### Backend-Seite:

1. **Neue DTO erstellen:**
   ```java
   // backend/src/main/java/com/studybuddy/dto/GoogleLoginRequest.java
   public class GoogleLoginRequest {
       private String googleToken;
   }
   ```

2. **Neue Controller-Methode:**
   ```java
   @PostMapping("/google-login")
   public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest req) {
       // Google Token validieren
       // User finden oder erstellen
       // AuthResponse zurückgeben
   }
   ```

3. **Service-Methode updaten:**
   ```java
   public AuthResponse googleLogin(String googleToken) {
       // Token-Verifikation
       // User-Lookup
       // Response erstellen
   }
   ```

#### Frontend-Seite:

1. **Neue View in index.html:**
   ```html
   <div id="googleLoginView" class="view">
       <button onclick="app.handleGoogleLogin()">Mit Google anmelden</button>
   </div>
   ```

2. **Handler in main.js:**
   ```javascript
   async handleGoogleLogin() {
       const token = await getGoogleToken(); // Google SDK nutzen
       const response = await ApiService.request('/auth/google-login', {
           method: 'POST',
           body: JSON.stringify({ googleToken: token })
       });
       // Response verarbeiten
   }
   ```

3. **API-Methode in api.js:**
   ```javascript
   static async googleLogin(googleToken) {
       return this.request('/auth/google-login', {
           method: 'POST',
           body: JSON.stringify({ googleToken })
       });
   }
   ```

## Testing Checklist

- [ ] **Registration:**
  - [ ] Mit gültigen Daten registrieren
  - [ ] Email existiert bereits (Error)
  - [ ] Passwörter stimmen nicht überein (Error)

- [ ] **Login:**
  - [ ] Mit gültigen Credentials anmelden
  - [ ] Falsches Passwort (Error)
  - [ ] Email existiert nicht (Error)

- [ ] **Profile:**
  - [ ] Profil erstellen nach Login
  - [ ] Profil speichern und laden
  - [ ] Profil aktualisieren

- [ ] **Navigation:**
  - [ ] Views wechseln funktioniert
  - [ ] Logout funktioniert
  - [ ] Session bleibt nach Browser-Refresh

- [ ] **Messages:**
  - [ ] Success-Messages zeigen
  - [ ] Error-Messages zeigen
  - [ ] Auto-Remove nach 5 Sekunden

## Common Issues & Solutions

### Backend

| Issue | Cause | Solution |
|-------|-------|----------|
| `Address already in use: 8080` | Port belegt | `netstat -ano \| findstr :8080` → Prozess killen |
| `Failed to initialize Database` | H2 Permission | Lösche `backend/data/` Folder |
| `Cannot find symbol` (Compiler Error) | Typo oder faller Import | Prüfe @Entity, @Autowired Annotations |
| `Null Pointer Exception` | User nicht gefunden | Optional.isEmpty() prüfen in Service |

### Frontend

| Issue | Cause | Solution |
|-------|-------|----------|
| `Cannot find module` | node_modules fehlt | `npm install` |
| `CORS Error` | Backend antwortet nicht/falsche URL | Prüfe http://localhost:8080 erreichbar |
| `localStorage undefined` | localStorage API nicht verfügbar | Browser muss JavaScript unterstützen |
| `Module parse failed` | ES6 Syntax Problem | Prüfe Vite config, verwende `import/export` |

## Performance Tips

1. **Backend:**
   - Nutze `@Transactional` für Data-Konsistenz
   - Lazy-Loading für Relations verwenden
   - Connection Pooling optimieren

2. **Frontend:**
   - API-Requests debounce (schnelle Klicks)
   - Große DOMs via Virtual Scrolling
   - CSS-Selektoren simplify

3. **Database:**
   - Indizes auf Suchfeldern (email, username)
   - Query-Performance mit H2 Console prüfen

## Deployment Preparation

### Backend Production

```xml
<!-- pom.xml Änderungen -->
<properties>
    <java.version>17</java.version>
    <spring.profiles.active>production</spring.profiles.active>
</properties>

<!-- Passwort-Hashing hinzufügen -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

### Frontend Production Build

```bash
cd frontend
npm run build

# Resultat in dist/ Folder
# Deploy dist/ Contents zum Web Server
```

### Environment Konfiguration

```properties
# application.properties (production)
spring.datasource.url=jdbc:mysql://prod-db:3306/studybuddy
spring.jpa.hibernate.ddl-auto=validate
spring.security.enable-cross-site-request-forgery.enabled=true
```

---

**Happy Coding! 🚀**

