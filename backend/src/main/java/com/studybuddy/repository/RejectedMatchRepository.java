package com.studybuddy.repository;

import com.studybuddy.model.RejectedMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RejectedMatchRepository extends JpaRepository<RejectedMatch, Long> {

    /**
     * Prüfe ob ein Nutzer bereits von einem anderen Nutzer abgelehnt wurde.
     */
    @Query("SELECT r FROM RejectedMatch r WHERE r.user.id = :userId AND r.rejectedUser.id = :rejectedUserId")
    Optional<RejectedMatch> findRejection(@Param("userId") Long userId, @Param("rejectedUserId") Long rejectedUserId);
}

