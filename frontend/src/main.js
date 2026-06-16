import ApiService from './api.js';
import MatchingModule from './matching.js';
import DashboardModule from './dashboard.js';

class App {
    constructor() {
        this.currentUser = null;
        this.isLoading = false;
        this.ApiService = ApiService; // Mache ApiService verfügbar
        this.matchingModule = new MatchingModule(this);
        this.dashboardModule = new DashboardModule(this);
        this.init();
    }

    init() {
        // Event Listeners für Forms
        document.getElementById('loginForm').addEventListener('submit', (e) => this.handleLogin(e));
        document.getElementById('registerForm').addEventListener('submit', (e) => this.handleRegister(e));
        document.getElementById('profileForm').addEventListener('submit', (e) => this.handleProfileCreate(e));

        // Session aus localStorage laden
        this.loadSession();

        // Prüfe Backend-Verbindung
        this.checkBackendConnection();

        // Standardmäßig Login-View anzeigen
        if (!this.currentUser) {
            this.showLogin();
        }
    }

    /**
     * Prüfe Backend-Verbindung
     */
    async checkBackendConnection() {
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                timeout: 5000
            });
        } catch (error) {
            console.warn('Backend möglicherweise nicht erreichbar:', error);
            this.showMessage('⚠️ Backend nicht erreichbar. Bitte stelle sicher, dass der Server läuft.', 'error');
        }
    }

    /**
     * Session-Management
     */
    loadSession() {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            this.currentUser = JSON.parse(savedUser);
            this.showDashboard();
            this.updateNavBar();
        }
    }

    saveSession() {
        if (this.currentUser) {
            localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
        }
    }

    logout() {
        localStorage.removeItem('currentUser');
        this.currentUser = null;
        this.showMessage('Du hast dich abgemeldet', 'info');
        this.showLogin();
        this.updateNavBar();
    }

    /**
     * View Management
     */
    showView(viewId) {
        // Verstecke alle Views
        document.querySelectorAll('.view').forEach(view => {
            view.classList.remove('active');
        });

        // Zeige die ausgewählte View
        const view = document.getElementById(viewId);
        if (view) {
            view.classList.add('active');
        }
    }

    showLogin() {
        this.showView('loginView');
    }

    showRegister() {
        this.showView('registerView');
    }

    showProfile() {
        this.showView('profileView');
        if (this.currentUser) {
            this.loadProfileData();
        }
    }

    showDashboard() {
        this.showView('dashboardView');
        if (this.currentUser) {
            this.dashboardModule.init();
        }
    }

    showMatching() {
        // Zeige einen zentralen Matching-View (optional)
        if (this.currentUser) {
            this.matchingModule.loadSuggestions();
            this.matchingModule.loadAcceptedMatches();
        }
    }

    /**
     * Navigation Bar Update
     */
    updateNavBar() {
        const navMenu = document.getElementById('navMenu');
        navMenu.innerHTML = '';

        if (this.currentUser) {
            navMenu.innerHTML = `
                <li><a href="#" onclick="app.showDashboard(); return false;">Dashboard</a></li>
                <li>
                    <a href="#" onclick="app.showMatching(); return false;">
                        🤝 Matches
                        <span id="matchCountBadge" class="nav-badge" style="display: none;">0</span>
                    </a>
                </li>
                <li><span class="username">👤 ${this.currentUser.username}</span></li>
                <li><a href="#" onclick="app.logout(); return false;">Logout</a></li>
            `;

            // Aktualisiere Match-Count Badge
            if (this.matchingModule) {
                this.matchingModule.updateMatchCount();
            }
        } else {
            navMenu.innerHTML = `
                <li><a href="#" onclick="app.showLogin(); return false;">Login</a></li>
                <li><a href="#" onclick="app.showRegister(); return false;">Registrierung</a></li>
            `;
        }
    }

    /**
     * Message Management
     */
    showMessage(message, type = 'info') {
        const container = document.getElementById('messageContainer');
        const messageEl = document.createElement('div');
        messageEl.className = `message ${type}`;
        messageEl.textContent = message;

        container.appendChild(messageEl);

        // Auto-remove nach 5 Sekunden
        setTimeout(() => {
            messageEl.classList.add('hide');
            setTimeout(() => messageEl.remove(), 300);
        }, 5000);
    }

    /**
     * Login Handler
     */
    async handleLogin(e) {
        e.preventDefault();
        const form = e.target;
        const email = form.elements['email'].value;
        const password = form.elements['password'].value;

        try {
            const response = await ApiService.login(email, password);

            if (response.success) {
                this.currentUser = {
                    userId: response.userId,
                    email: response.email,
                    username: response.username
                };
                this.saveSession();
                this.showMessage(response.message, 'success');
                this.showDashboard();
                this.updateNavBar();
                form.reset();
            } else {
                this.showMessage(response.message || 'Login fehlgeschlagen', 'error');
            }
        } catch (error) {
            this.showMessage('Fehler beim Login: ' + error.message, 'error');
        }
    }

    /**
     * Register Handler
     */
    async handleRegister(e) {
        e.preventDefault();
        const form = e.target;
        const username = form.elements['username'].value;
        const email = form.elements['email'].value;
        const password = form.elements['password'].value;
        const confirmPassword = form.elements['confirmPassword'].value;

        try {
            const response = await ApiService.register(username, email, password, confirmPassword);

            if (response.success) {
                this.currentUser = {
                    userId: response.userId,
                    email: response.email,
                    username: response.username
                };
                this.saveSession();
                this.showMessage(response.message, 'success');
                this.showProfile();
                this.updateNavBar();
                form.reset();
            } else {
                this.showMessage(response.message || 'Registrierung fehlgeschlagen', 'error');
            }
        } catch (error) {
            this.showMessage('Fehler bei der Registrierung: ' + error.message, 'error');
        }
    }

    /**
     * Profile Create/Update Handler
     */
    async handleProfileCreate(e) {
        e.preventDefault();

        if (!this.currentUser) {
            this.showMessage('Du musst angemeldet sein', 'error');
            return;
        }

        const form = e.target;
        const profileData = {
            firstName: form.elements['firstName'].value,
            lastName: form.elements['lastName'].value,
            bio: form.elements['bio'].value,
            schoolOrUniversity: form.elements['schoolOrUniversity'].value
        };

        try {
            const response = await ApiService.createProfile(this.currentUser.userId, profileData);

            if (response.success) {
                this.showMessage(response.message, 'success');
            } else {
                this.showMessage(response.message || 'Fehler beim Speichern des Profils', 'error');
            }
        } catch (error) {
            this.showMessage('Fehler beim Speichern: ' + error.message, 'error');
        }
    }

    /**
     * Load Profile Data
     */
    async loadProfileData() {
        if (!this.currentUser) return;

        try {
            const response = await ApiService.getProfile(this.currentUser.userId);

            if (response.success) {
                document.getElementById('profileFirstName').value = response.firstName || '';
                document.getElementById('profileLastName').value = response.lastName || '';
                document.getElementById('profileBio').value = response.bio || '';
                document.getElementById('profileSchool').value = response.schoolOrUniversity || '';
            }
        } catch (error) {
            // Profil existiert möglicherweise noch nicht, was okay ist
            console.log('Profil noch nicht erstellt');
        }
    }
}

// Initialisiere App
const app = new App();

// Mache app global verfügbar für onclick-Handler in HTML
window.app = app;

