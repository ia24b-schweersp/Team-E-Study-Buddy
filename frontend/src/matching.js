/**
 * Matching Module - Verwaltet Match-Vorschläge und Lernpartner-Verbindungen
 */

class MatchingModule {
    constructor(app) {
        this.app = app;
        this.currentSuggestionIndex = 0;
        this.suggestions = [];
        this.acceptedMatches = [];
    }

    /**
     * Lade Match-Vorschläge
     */
    async loadSuggestions() {
        if (!this.app.currentUser) return;

        try {
            const response = await this.app.ApiService.getMatchSuggestions(this.app.currentUser.userId);

            if (response.success) {
                this.suggestions = response.suggestions || [];
                this.currentSuggestionIndex = 0;
                this.renderSuggestion();
            } else {
                this.app.showMessage('Fehler beim Laden der Vorschläge', 'error');
            }
        } catch (error) {
            this.app.showMessage('Fehler beim Laden der Vorschläge: ' + error.message, 'error');
        }
    }

    /**
     * Rendere aktuellen Match-Vorschlag
     */
    renderSuggestion() {
        const container = document.getElementById('matchSuggestionCard');
        if (!container) return;

        if (this.suggestions.length === 0) {
            container.innerHTML = `
                <div class="suggestion-empty">
                    <p>😊 Keine Vorschläge verfügbar</p>
                    <small>Aktualisiere später, um neue Lernpartner zu finden</small>
                </div>
            `;
            return;
        }

        if (this.currentSuggestionIndex >= this.suggestions.length) {
            container.innerHTML = `
                <div class="suggestion-empty">
                    <p>🎉 Alle Vorschläge angesehen!</p>
                    <button class="btn btn-secondary" onclick="window.matchingModule.loadSuggestions()">Neu laden</button>
                </div>
            `;
            return;
        }

        const suggestion = this.suggestions[this.currentSuggestionIndex];
        const compatibilityEmoji = this.getCompatibilityEmoji(suggestion.compatibilityScore);

        container.innerHTML = `
            <div class="suggestion-card">
                <div class="suggestion-header">
                    <h3>${suggestion.firstName} ${suggestion.lastName}</h3>
                    <div class="compatibility-badge">
                        ${compatibilityEmoji} ${suggestion.compatibilityScore}%
                    </div>
                </div>
                <div class="suggestion-content">
                    <p><strong>👤 Benutzername:</strong> ${suggestion.username}</p>
                    ${suggestion.schoolOrUniversity ? 
                        `<p><strong>🏫 Schule:</strong> ${suggestion.schoolOrUniversity}</p>` : ''}
                    ${suggestion.bio ? 
                        `<p><strong>📝 Bio:</strong> ${suggestion.bio}</p>` : ''}
                </div>
                <div class="suggestion-actions">
                    <button class="btn btn-success" onclick="window.matchingModule.acceptSuggestion(${suggestion.userId})">
                        ✓ Akzeptieren
                    </button>
                    <button class="btn btn-danger" onclick="window.matchingModule.rejectSuggestion(${suggestion.userId})">
                        ✗ Ablehnen
                    </button>
                </div>
                <div class="suggestion-counter">
                    ${this.currentSuggestionIndex + 1} / ${this.suggestions.length}
                </div>
            </div>
        `;
    }

    /**
     * Bestimme Emoji basierend auf Kompatibilität
     */
    getCompatibilityEmoji(score) {
        if (score >= 80) return '💚';
        if (score >= 60) return '💛';
        if (score >= 40) return '🧡';
        return '💙';
    }

    /**
     * Akzeptiere einen Match-Vorschlag
     */
    async acceptSuggestion(suggestedUserId) {
        if (!this.app.currentUser) return;

        try {
            const response = await this.app.ApiService.acceptMatch(
                this.app.currentUser.userId,
                suggestedUserId
            );

            if (response.success) {
                this.app.showMessage(response.message, 'success');
                this.currentSuggestionIndex++;
                this.renderSuggestion();
            } else {
                this.app.showMessage(response.message || 'Fehler beim Akzeptieren', 'error');
            }
        } catch (error) {
            this.app.showMessage('Fehler: ' + error.message, 'error');
        }
    }

    /**
     * Lehne einen Match-Vorschlag ab
     */
    async rejectSuggestion(suggestedUserId) {
        if (!this.app.currentUser) return;

        try {
            const response = await this.app.ApiService.rejectMatch(
                this.app.currentUser.userId,
                suggestedUserId
            );

            if (response.success) {
                this.app.showMessage('Vorschlag abgelehnt', 'info');
                this.currentSuggestionIndex++;
                this.renderSuggestion();
            } else {
                this.app.showMessage(response.message || 'Fehler beim Ablehnen', 'error');
            }
        } catch (error) {
            this.app.showMessage('Fehler: ' + error.message, 'error');
        }
    }

    /**
     * Lade akzeptierte Matches
     */
    async loadAcceptedMatches() {
        if (!this.app.currentUser) return;

        try {
            const response = await this.app.ApiService.getAcceptedMatches(this.app.currentUser.userId);

            if (response.success) {
                this.acceptedMatches = response.matches || [];
                this.renderAcceptedMatches();
            }
        } catch (error) {
            console.error('Fehler beim Laden der Matches:', error);
        }
    }

    /**
     * Rendere akzeptierte Matches
     */
    renderAcceptedMatches() {
        const container = document.getElementById('acceptedMatchesList');
        if (!container) return;

        if (this.acceptedMatches.length === 0) {
            container.innerHTML = `
                <p class="empty-message">Noch keine Lernpartner gefunden. Akzeptiere Vorschläge um Lernpartner zu finden!</p>
            `;
            return;
        }

        let html = '';
        this.acceptedMatches.forEach(match => {
            html += `
                <div class="match-card">
                    <h4>${match.firstName} ${match.lastName}</h4>
                    <p><strong>👤:</strong> ${match.username}</p>
                    ${match.schoolOrUniversity ? `<p><strong>🏫:</strong> ${match.schoolOrUniversity}</p>` : ''}
                    ${match.bio ? `<p><strong>📝:</strong> ${match.bio}</p>` : ''}
                </div>
            `;
        });

        container.innerHTML = html;
    }

    /**
     * Zeige Anzahl der Matches an
     */
    async updateMatchCount() {
        if (!this.app.currentUser) return;

        try {
            const response = await this.app.ApiService.countMatches(this.app.currentUser.userId);

            if (response.success) {
                const badge = document.getElementById('matchCountBadge');
                if (badge && response.count > 0) {
                    badge.textContent = response.count;
                    badge.style.display = 'inline-block';
                }
            }
        } catch (error) {
            console.error('Fehler beim Zählen der Matches:', error);
        }
    }
}

export default MatchingModule;

