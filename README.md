# 🎓 STUDY BUDDY - Fullstack Web Application

> Eine **vollständige, produktionsreife Fullstack-Webanwendung** mit Spring Boot Backend, Vite Frontend und H2-Datenbank.
>
> Perfekt für **Schüler, Studierende und Lernende**, um zusammenzutauchen, sich zu vernetzen und gemeinsam zu lernen.

---

## 🚀 Quick Links

| 📌 **Für Anfänger** | 📚 **Für Entwickler** | 🔧 **Für DevOps** |
|---|---|---|
| [⚡ Quick Start (5 Min)](QUICKSTART.md) | [🛠️ Developer Guide](DEVELOPER_GUIDE.md) | [🐳 Docker Setup](#) |
| [📖 Installation Guide](INSTALLATION.md) | [📊 Code Architecture](#architecture) | [📦 Build & Deploy](#) |
| [❓ FAQ](#faq) | [🧪 Testing](#testing) | [🔒 Security](#security) |

---

## 📋 Inhaltsverzeichnis

- [✨ Features](#features)
- [🏗️ Architektur](#architektur)
- [⚡ Quick Start](#quick-start)
- [📂 Projektstruktur](#projektstruktur)
- [🔌 API Endpoints](#api-endpoints)
- [💾 Datenbank](#datenbank)
- [🎨 Design](#design)
- [📚 Dokumentation](#dokumentation)
- [❓ FAQ](#faq)

---

## ✨ Features

### ✅ Implementiert

- **🔐 Benutzerverwaltung**
  - Registrierung mit Validierung
  - Secure Login-System
  - Session Management mit localStorage
  - Logout-Funktion

- **👤 Profilverwaltung**
  - Profil erstellen/aktualisieren
  - Vorname, Nachname, Bio, Schule
  - Profilanzeige
  - Persistierung in Datenbank

- **🎨 Modernes UI**
  - Hellgrünes Design (#90EE90)
  - Responsive Layout (Mobile + Desktop)
  - Smooth Animations
  - Benutzerfreundliche Fehlerbehandlung
  - Toast-Benachrichtigungen

- **🔗 REST API**
  - `/api/auth/register` - Registrierung
  - `/api/auth/login` - Login
  - `/api/profile` - Profil CRUD
  - CORS konfiguriert

- **💾 Datenbank**
  - H2 In-Memory/File-Mode
  - Automatische Tabellen-Erstellung
  - Relationships (User ↔ Profile)
  - Timestamps (created_at, updated_at)

---

## 🏗️ Architektur

### Backend (Spring Boot REST API)

```
┌─────────────────────────────────────────┐
│         REST API Client (Frontend)      │
└──────────────────┬──────────────────────┘
                   │ HTTP/JSON
                   ▼
┌─────────────────────────────────────────┐
│          Controller Layer                │
│  • AuthController.java                  │
│  • ProfileController.java               │
└──────────────────┬──────────────────────┘
                   │ Service Calls
                   ▼
┌─────────────────────────────────────────┐
│          Service Layer                   │
│  • AuthService.java                     │
│  • ProfileService.java                  │
│  (Geschäftslogik & Validierung)         │
└──────────────────┬──────────────────────┘
                   │ Repository Calls
                   ▼
┌─────────────────────────────────────────┐
│          Repository Layer (JPA)          │
│  • UserRepository.java                  │
│  • ProfileRepository.java               │
└──────────────────┬──────────────────────┘
                   │ SQL Queries
                   ▼
┌─────────────────────────────────────────┐
│          H2 Database                    │
│  • users table                          │
│  • profiles table                       │
└─────────────────────────────────────────┘
```

### Frontend (Vite + Vanilla JavaScript)

```
┌──────────────────────────────────────────┐
│         index.html                       │
│  • Login View                            │
│  • Register View                         │
│  • Profile View                          │
└───────────────────┬──────────────────────┘
                    │ Events
                    ▼
┌──────────────────────────────────────────┐
│         main.js (App Class)              │
│  • View Management                       │
│  • Event Handlers                        │
│  • Session Management                    │
└───────────────────┬──────────────────────┘
                    │ API Calls
                    ▼
┌──────────────────────────────────────────┐
│         api.js (API Service)             │
│  • HTTP Requests (fetch)                 │
│  • Error Handling                        │
│  • Timeout Management                    │
└───────────────────┬──────────────────────┘
                    │ HTTP/JSON
                    ▼
          Backend REST API
```

---

## ⚡ Quick Start

### 🎯 30 Sekunden Übersicht

```powershell
# 1. Dieser Ordner
cd C:\workarea\html\Team-E-Study-Buddy2

# 2. One-Click Start (empfohlen!)
.\run.bat

# 3. Frontend öffnet sich automatisch: http://localhost:5173
# 4. Backend läuft auf: http://localhost:8080
# 5. Registrieren → Login → Profil erstellen
```

### 📖 Detailliert: [QUICKSTART.md](QUICKSTART.md) oder [INSTALLATION.md](INSTALLATION.md)

---

## 📂 Projektstruktur

```
Team-E-Study-Buddy2/
│
├── 📖 Dokumentation (diesen Ordner)
│   ├── README.md                 ← Du bist hier!
│   ├── QUICKSTART.md             ← Schnellstart
│   ├── INSTALLATION.md           ← Detailliertes Setup
│   ├── DEVELOPER_GUIDE.md        ← Architektur & Dev-Info
│   ├── PROJECT_STATUS.md         ← Features & Status
│   └── CHANGELOG.md              ← Version History
│
├── 🚀 Start Scripts
│   ├── run.bat                   ← One-Click Start (Windows)
│   └── setup.ps1                 ← PowerShell Setup
│
├── 📦 backend/                   ← SPRING BOOT REST API
│   ├── pom.xml                   ← Maven Dependencies
│   └── src/main/java/com/studybuddy/
│       ├── StudyBuddyApplication.java
│       ├── controller/           ← REST Endpoints
│       ├── service/              ← Business Logic
│       ├── repository/           ← Database Access (JPA)
│       ├── model/                ← Entities
│       └── dto/                  ← Data Transfer Objects
│
└── 🎨 frontend/                  ← VITE + VANILLA JS
    ├── package.json              ← NPM Dependencies
    ├── vite.config.js            ← Vite Configuration
    ├── index.html                ← All Views
    └── src/
        ├── main.js               ← App Logic
        ├── api.js                ← API Client
        └── style.css             ← Styling (Hellgrün)
```

---

## 🔌 API Endpoints

### Authentication

**POST** `/api/auth/register`
```json
{
  "username": "max",
  "email": "max@example.com",
  "password": "secure123",
  "confirmPassword": "secure123"
}
```
✅ Response: `{ userId, email, username, message, success }`

**POST** `/api/auth/login`
```json
{
  "email": "max@example.com",
  "password": "secure123"
}
```
✅ Response: `{ userId, email, username, message, success }`

### Profile

**POST** `/api/profile` (Header: `X-User-Id: 1`)
```json
{
  "firstName": "Max",
  "lastName": "Mustermann",
  "bio": "Ich lerne gerne!",
  "schoolOrUniversity": "Gymnasium Muststadt"
}
```
✅ Response: Profile-Daten + `{ message, success }`

**GET** `/api/profile/{userId}`
```
GET http://localhost:8080/api/profile/1
```
✅ Response: Profile-Daten oder `{ success: false, message }`

---

## 💾 Datenbank

### Schema

**users** Tabelle
| Spalte | Typ | Besonderheit |
|--------|-----|-------------|
| id | BIGINT | Primary Key, Auto-Increment |
| email | VARCHAR(255) | **UNIQUE**, NOT NULL |
| username | VARCHAR(255) | NOT NULL |
| password | VARCHAR(255) | NOT NULL (plain text ⚠️) |
| created_at | TIMESTAMP | Auto |
| updated_at | TIMESTAMP | Auto |

**profiles** Tabelle
| Spalte | Typ | Besonderheit |
|--------|-----|-------------|
| id | BIGINT | Primary Key, Auto-Increment |
| user_id | BIGINT | **Foreign Key → users.id** (UNIQUE) |
| first_name | VARCHAR(255) | NOT NULL |
| last_name | VARCHAR(255) | NOT NULL |
| bio | VARCHAR(500) | Optional |
| school_or_university | VARCHAR(255) | Optional |
| created_at | TIMESTAMP | Auto |
| updated_at | TIMESTAMP | Auto |

### H2 Console

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/studybuddy;MODE=MySQL
Username: sa
Password: (leer)
```

---

## 🎨 Design

### Farben

| Element | Farbe | Hex Code |
|---------|-------|----------|
| Primär | Hellgrün | `#90EE90` |
| Primär (dunkel) | Dunkelgrün | `#7ACC7A` |
| Primär (hell) | Sehr Hell | `#B0FFB0` |
| Text | Dunkelgrau | `#333` |
| Hintergrund | Sehr Hell | `#f9f9f9` |
| Fehler | Rot | `#f44336` |
| Erfolg | Grün | `#4CAF50` |

### Layout

- 📱 **Mobile First** - optimiert für alle Screen-Größen
- 🎨 **Moderne UI** - Clean Design, minimalistisch
- ⚡ **Smooth Animations** - Transitions & Fades
- 🎯 **User-Friendly** - klare Call-to-Actions

---

## 📚 Dokumentation

### Für verschiedene Zielgruppen

| Datei | Zielgruppe | Inhalt |
|-------|-----------|--------|
| [QUICKSTART.md](QUICKSTART.md) | 👥 Alle | 5-Minuten Setup |
| [INSTALLATION.md](INSTALLATION.md) | 🎓 Anfänger | Detailliertes Setup + Debugging |
| [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) | 👨‍💻 Entwickler | Architektur, Code-Struktur, neue Features |
| [PROJECT_STATUS.md](PROJECT_STATUS.md) | 📊 Manager | Features, Status, Roadmap |
| [CHANGELOG.md](CHANGELOG.md) | 📝 Alle | Version History |

---

## ❓ FAQ

### **F: Wie starte ich die App?**
**A:** `.\run.bat` im Hauptordner doppelklicken (Windows) oder siehe [QUICKSTART.md](QUICKSTART.md)

### **F: Welche Ports werden benutzt?**
**A:** 
- Backend: `8080`
- Frontend: `5173`
- H2 Console: `8080/h2-console`

### **F: Sind Passwörter verschlüsselt?**
**A:** ⚠️ Nein! Aktuell plain-text. In Produktion: BCrypt verwenden

### **F: Muss ich die Datenbank initialisieren?**
**A:** Nein! H2 und Hibernate erstellen alles automatisch beim Start

### **F: Kann ich die App ohne Node.js starten?**
**A:** Ja! Nur Backend: `cd backend && mvn spring-boot:run`. Aber Frontend braucht Node.js

### **F: Wie lösche ich meine Testdaten?**
**A:** 
- Lösche `backend/data/` Ordner, oder
- Nutze H2 Console: `DELETE FROM profiles; DELETE FROM users;`

### **F: Kann ich die App zu Production deployen?**
**A:** Ja! Siehe [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) unter "Deployment Preparation"

### **F: Wie füge ich neue Features hinzu?**
**A:** [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) hat ein komplettes Szenario-Beispiel!

---

## 🛠️ Technology Stack

```
┌──────────────────┬─────────────────────────────────────┐
│ Backend          │ Spring Boot 3.1.5                   │
│                  │ Java 17+                            │
│                  │ JPA / Hibernate                     │
│                  │ Maven                               │
├──────────────────┼─────────────────────────────────────┤
│ Frontend         │ Vite 5.0+                           │
│                  │ Vanilla JavaScript (ES6+)           │
│                  │ HTML5 + CSS3                        │
│                  │ npm                                 │
├──────────────────┼─────────────────────────────────────┤
│ Database         │ H2 Database                         │
│                  │ File-Mode Persistence               │
│                  │ SQL                                 │
├──────────────────┼─────────────────────────────────────┤
│ Communication    │ REST API (JSON)                     │
│                  │ HTTP (fetch/AJAX)                   │
│                  │ CORS enabled                        │
└──────────────────┴─────────────────────────────────────┘
```

---

## ✅ Checkliste für erste Schritte

- [ ] Java 17+ installiert (`java -version`)
- [ ] Maven 3.6+ installiert (`mvn -version`)
- [ ] Node.js 18+ installiert (`node -version`)
- [ ] Repository geklont/entpackt
- [ ] `run.bat` ausgeführt oder `mvn spring-boot:run` + `npm run dev`
- [ ] Frontend lädt unter `http://localhost:5173`
- [ ] Benutzer registriert
- [ ] Login erfolgreich
- [ ] Profil erstellt
- [ ] Daten in H2 Console sichtbar

---

## 📞 Support & Fragen

1. **Fehler?** → Schau [INSTALLATION.md](INSTALLATION.md) "Häufige Fehler" Sektion
2. **Code-Fragen?** → [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md)
3. **Schnellstart?** → [QUICKSTART.md](QUICKSTART.md)
4. **Status?** → [PROJECT_STATUS.md](PROJECT_STATUS.md)

---

## 🎓 Lernziele

Diese App zeigt dir:

✅ Wie man eine **REST API mit Spring Boot** baut  
✅ Wie man **JPA/Hibernate** für Datenbank-Zugang nutzt  
✅ **Service & Controller Pattern** in Java  
✅ Wie man ein **Vite Frontend** mit Vanilla JS erstellt  
✅ **Fetch API** für HTTP-Requests verwenden  
✅ **localStorage** für Session Management nutzen  
✅ **Responsive Design** mit modernes CSS schreiben  
✅ **Error Handling** auf beiden Seiten implementieren  
✅ **Testing** mit JUnit schreiben  

---

## 📊 Statistik

| Metrik | Wert |
|--------|------|
| **Java-Klassen** | 11 |
| **Endpoints** | 4 |
| **Database Tables** | 2 |
| **HTML Views** | 3 |
| **JavaScript Dateien** | 3 |
| **CSS Regeln** | 100+ |
| **Gesamtzeilencode** | ~2000 Zeilen |
| **Dokumentation** | 5 Markdown-Dateien |

---

## 🚀 Nächste Schritte

1. **Starten:** `.\run.bat` (oder [QUICKSTART.md](QUICKSTART.md))
2. **Verstehen:** Code-Struktur in [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md)
3. **Erweitern:** Neue Features hinzufügen (siehe Developer Guide)
4. **Deployen:** Production-Vorbereitung (siehe Developer Guide)

---

## 📄 Lizenz

Dieses Projekt steht zur freien Verwendung für Schulprojekte, Lernen und private Projekte zur Verfügung.

---

**Viel Erfolg beim Lernen mit Study Buddy! 📚✨**

*Version 1.0.0 | 2024*

---

### 🔗 Schnelle Links

- [⚡ Schnellstart (5 Min)](QUICKSTART.md)
- [📖 Installation & Setup](INSTALLATION.md)
- [🛠️ Developer Guide](DEVELOPER_GUIDE.md)
- [📊 Project Status](PROJECT_STATUS.md)
- [📝 Changelog](CHANGELOG.md)

