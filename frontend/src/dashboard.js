/**
 * Dashboard Module - Hauptseite nach dem Login
 */

class DashboardModule {
    constructor(app) {
        this.app = app;
    }

    /**
     * Initialisiere das Dashboard
     */
    async init() {
        this.renderDashboard();
        await this.updateStats();

        // Lade auch Vorschläge nach kurzer Verzögerung
        setTimeout(() => {
            if (this.app.matchingModule) {
                this.app.matchingModule.loadSuggestions();
                this.app.matchingModule.loadAcceptedMatches();
            }
        }, 500);
    }

    /**
     * Rendere das Dashboard
     */
    renderDashboard() {
        const container = document.getElementById('dashboardView');
        if (!container) return;

        const user = this.app.currentUser;

        container.innerHTML = `
            <div class="dashboard-container">
                <!-- Header Section -->
                <div class="dashboard-header">
                    <div class="welcome-section">
                        <h2>Willkommen, ${user.username}! 👋</h2>
                        <p class="subtitle">Finde Lernpartner und verbessere dich zusammen</p>
                    </div>
                    <div class="header-actions">
                        <button class="btn btn-secondary" onclick="app.showProfile()" title="Profil bearbeiten">
                            ⚙️ Profil
                        </button>
                        <button class="btn btn-secondary" onclick="app.logout()" title="Abmelden">
                            🚪 Logout
                        </button>
                    </div>
                </div>

                <!-- Quick Stats -->
                <div class="stats-section">
                    <div class="stat-card">
                        <div class="stat-icon">🤝</div>
                        <div class="stat-content">
                            <div class="stat-number" id="statMatches">0</div>
                            <div class="stat-label">Lernpartner</div>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">👤</div>
                        <div class="stat-content">
                            <div class="stat-number" id="statProfile">✓</div>
                            <div class="stat-label">Profil-Status</div>
                        </div>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="dashboard-content">
                    <!-- Matching Section -->
                    <div class="section matching-section">
                        <h3>🎯 Finde Lernpartner</h3>
                        <p class="section-subtitle">Entdecke passende Lernpartner basierend auf deinen Interessen</p>
                        <div id="matchSuggestionCard" class="match-suggestion-container">
                            <div class="loading">Laden...</div>
                        </div>
                    </div>

                    <!-- Accepted Matches Section -->
                    <div class="section matches-section">
                        <h3>🤝 Deine Lernpartner</h3>
                        <p class="section-subtitle">Deine bestätigten Lernpartner-Verbindungen</p>
                        <div id="acceptedMatchesList" class="matches-list">
                            <p class="loading">Laden...</p>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Aktualisiere Statistiken
     */
    async updateStats() {
        if (!this.app.currentUser) return;

        try {
            // Aktualisiere Match-Zähler
            const matchResponse = await this.app.ApiService.countMatches(this.app.currentUser.userId);
            if (matchResponse.success) {
                document.getElementById('statMatches').textContent = matchResponse.count;
            }

            // Prüfe Profil-Status
            const profileResponse = await this.app.ApiService.getProfile(this.app.currentUser.userId);
            if (profileResponse.success) {
                document.getElementById('statProfile').textContent = '✓';
            }
        } catch (error) {
            console.error('Fehler beim Aktualisieren der Statistiken:', error);
        }
    }

    /**
     * Aktualisiere Dashboard-Daten
     */
    async refresh() {
        await this.updateStats();
        if (this.app.matchingModule) {
            await this.app.matchingModule.loadSuggestions();
            await this.app.matchingModule.loadAcceptedMatches();
        }
    }
}

export default DashboardModule;

