package com.studybuddy.repository;

import com.studybuddy.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    /**
     * Finde alle akzeptierten Matches für einen Nutzer (egal ob user1 oder user2).
     */
    @Query("SELECT m FROM Match m WHERE (m.user1.id = :userId OR m.user2.id = :userId) AND m.status = 'ACCEPTED'")
    List<Match> findAcceptedMatchesByUserId(@Param("userId") Long userId);

    /**
     * Finde ein Match, bei dem der aktuelle Nutzer mit dem anderen Nutzer
     * bereits agiert hat (akzeptiert oder abgelehnt).
     */
    @Query("SELECT m FROM Match m WHERE " +
           "((m.user1.id = :userId AND (m.user1Accepted = true OR m.user1Rejected = true)) OR " +
           "(m.user2.id = :userId AND (m.user2Accepted = true OR m.user2Rejected = true))) AND " +
           "((m.user1.id = :otherUserId) OR (m.user2.id = :otherUserId))")
    Optional<Match> findMatchWhereCurrentUserActed(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);

    /**
     * Finde ein Match zwischen zwei Nutzern (unabhängig von der Reihenfolge).
     */
    @Query("SELECT m FROM Match m WHERE (m.user1.id = :userId1 AND m.user2.id = :userId2) OR (m.user1.id = :userId2 AND m.user2.id = :userId1)")
    Optional<Match> findMatchBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * Zähle alle akzeptierten Matches eines Nutzers.
     */
    @Query("SELECT COUNT(m) FROM Match m WHERE (m.user1.id = :userId OR m.user2.id = :userId) AND m.status = 'ACCEPTED'")
    long countAcceptedMatchesByUserId(@Param("userId") Long userId);
}
