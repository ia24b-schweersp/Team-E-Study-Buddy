// API Service für Backend-Kommunikation

const API_BASE_URL = 'http://localhost:8080/api';
const REQUEST_TIMEOUT = 10000; // 10 seconds

class ApiService {
    /**
     * Generic fetch method with timeout
     */
    static async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        try {
            // Implementiere Timeout
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), REQUEST_TIMEOUT);

            const response = await fetch(url, {
                ...options,
                headers,
                signal: controller.signal
            });

            clearTimeout(timeoutId);

            // Prüfe ob Response JSON ist
            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                throw new Error('Server antwortete mit ungültigem Format');
            }

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || `HTTP Error: ${response.status}`);
            }

            return data;
        } catch (error) {
            if (error.name === 'AbortError') {
                console.error(`API Timeout [${endpoint}]`);
                throw new Error('Request Timeout - Backend antwortet nicht');
            }
            console.error(`API Error [${endpoint}]:`, error);
            throw error;
        }
    }

    /**
     * Registrierung
     */
    static async register(username, email, password, confirmPassword) {
        return this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify({
                username,
                email,
                password,
                confirmPassword
            })
        });
    }

    /**
     * Login
     */
    static async login(email, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({
                email,
                password
            })
        });
    }

    /**
     * Profil erstellen oder aktualisieren
     */
    static async createProfile(userId, profileData) {
        return this.request('/profile', {
            method: 'POST',
            headers: {
                'X-User-Id': userId
            },
            body: JSON.stringify(profileData)
        });
    }

    /**
     * Profil abrufen
     */
    static async getProfile(userId) {
        return this.request(`/profile/${userId}`, {
            method: 'GET'
        });
    }

    /**
     * Hole Match-Vorschläge
     */
    static async getMatchSuggestions(userId) {
        return this.request(`/match/suggestions/${userId}`, {
            method: 'GET'
        });
    }

    /**
     * Akzeptiere einen Match
     */
    static async acceptMatch(userId, suggestedUserId) {
        return this.request('/match/accept', {
            method: 'POST',
            headers: {
                'X-User-Id': userId
            },
            body: JSON.stringify({
                suggestedUserId: suggestedUserId
            })
        });
    }

    /**
     * Lehne einen Match ab
     */
    static async rejectMatch(userId, suggestedUserId) {
        return this.request('/match/reject', {
            method: 'POST',
            headers: {
                'X-User-Id': userId
            },
            body: JSON.stringify({
                suggestedUserId: suggestedUserId
            })
        });
    }

    /**
     * Hole akzeptierte Matches
     */
    static async getAcceptedMatches(userId) {
        return this.request(`/match/accepted/${userId}`, {
            method: 'GET'
        });
    }

    /**
     * Zähle Matches
     */
    static async countMatches(userId) {
        return this.request(`/match/count/${userId}`, {
            method: 'GET'
        });
    }
}

export default ApiService;

