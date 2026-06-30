/**
 * Hilfsfunktionen, die in mehreren Modulen verwendet werden.
 */

/**
 * Escaped HTML-Sonderzeichen, um XSS beim Einfügen von benutzergenerierten
 * Inhalten via innerHTML zu verhindern.
 *
 * @param {*} value beliebiger Wert; wird zu String konvertiert
 * @returns {string} sicher einfügbarer String
 */
export function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
