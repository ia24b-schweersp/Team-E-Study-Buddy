package com.studybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modell für abgelehnte Match-Vorschläge
 * Verhindert, dass bereits abgelehnte Nutzer erneut vorgeschlagen werden
 */
@Entity
@Table(name = "rejected_matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectedMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rejected_user_id", nullable = false)
    private User rejectedUser;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime rejectedAt = LocalDateTime.now();
}

