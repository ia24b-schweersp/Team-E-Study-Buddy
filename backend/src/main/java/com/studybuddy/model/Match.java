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

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Aktualisiere Status basierend auf Akzeptanzen
        if (user1Accepted && user2Accepted) {
            this.status = "ACCEPTED";
        } else if (!user1Accepted || !user2Accepted) {
            this.status = "PENDING";
        }
    }
}

