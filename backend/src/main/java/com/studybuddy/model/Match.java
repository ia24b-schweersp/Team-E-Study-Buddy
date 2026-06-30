package com.studybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Match-Modell für Lernpartner-Verbindungen
 */
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    /**
     * Status des Matches:
     * - PENDING: Eine Seite hat akzeptiert, die andere noch nicht
     * - ACCEPTED: Beide Seiten haben akzeptiert
     * - REJECTED: Mindestens eine Seite hat abgelehnt
     */
    @Column(nullable = false)
    @Builder.Default
    private String status = "PENDING";

    /**
     * Track ob user1 akzeptiert hat
     */
    @Builder.Default
    private Boolean user1Accepted = false;

    /**
     * Track ob user2 akzeptiert hat
     */
    @Builder.Default
    private Boolean user2Accepted = false;

    /**
     * Track ob user1 abgelehnt hat
     */
    @Builder.Default
    private Boolean user1Rejected = false;

    /**
     * Track ob user2 abgelehnt hat
     */
    @Builder.Default
    private Boolean user2Rejected = false;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Aktualisiere Status basierend auf Aktionen
        // Wenn eine Seite ablehnt, ist das Match abgelehnt
        if (user1Rejected || user2Rejected) {
            this.status = "REJECTED";
        } else if (user1Accepted && user2Accepted) {
            this.status = "ACCEPTED";
        } else {
            this.status = "PENDING";
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }

        // Setze Status basierend auf Aktionen beim Erstellen
        if (user1Rejected || user2Rejected) {
            this.status = "REJECTED";
        } else if (user1Accepted && user2Accepted) {
            this.status = "ACCEPTED";
        } else {
            this.status = "PENDING";
        }
    }
}
