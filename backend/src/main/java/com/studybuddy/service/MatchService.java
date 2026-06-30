package com.studybuddy.service;

import com.studybuddy.dto.MatchSuggestionDTO;
import com.studybuddy.model.Match;
import com.studybuddy.model.Profile;
import com.studybuddy.model.RejectedMatch;
import com.studybuddy.model.User;
import com.studybuddy.repository.MatchRepository;
import com.studybuddy.repository.ProfileRepository;
import com.studybuddy.repository.RejectedMatchRepository;
import com.studybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service für Match- und Lernpartner-Management
 */
@Service
@Transactional
public class MatchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RejectedMatchRepository rejectedMatchRepository;

    /**
     * Generiere Match-Vorschläge für einen Nutzer
     * basierend auf Kompatibilität mit anderen Nutzern
     */
    public List<MatchSuggestionDTO> generateMatchSuggestions(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nutzer nicht gefunden"));

        Profile currentProfile = profileRepository.findByUserId(userId)
                .orElse(null);

        if (currentProfile == null) {
            return new ArrayList<>(); // Leere Liste wenn kein Profil existiert
        }

        // Hole alle anderen Nutzer mit Profilen
        List<User> allUsers = userRepository.findAll();
        List<MatchSuggestionDTO> suggestions = new ArrayList<>();

        for (User otherUser : allUsers) {
            // Überspringe den aktuellen Nutzer
            if (otherUser.getId().equals(userId)) {
                continue;
            }

            // ✅ FIX: Prüfe ob der aktuelle Nutzer BEREITS AGIERT HAT
            // Wenn ja, zeige diesen Nutzer nicht mehr in Vorschlägen
            Optional<Match> userActedMatch = matchRepository.findMatchWhereCurrentUserActed(
                    userId, otherUser.getId());
            if (userActedMatch.isPresent()) {
                Match match = userActedMatch.get();
                // Zeige Nutzer nicht, wenn Status ACCEPTED oder REJECTED ist
                if (match.getStatus().equals("ACCEPTED") || match.getStatus().equals("REJECTED")) {
                    continue;
                }
            }

            // Prüfe ob dieser Nutzer bereits als abgelehnt markiert wurde
            Optional<RejectedMatch> rejected = rejectedMatchRepository.findRejection(userId, otherUser.getId());
            if (rejected.isPresent()) {
                continue;
            }

            // Hole das Profil des anderen Nutzers
            Profile otherProfile = profileRepository.findByUserId(otherUser.getId())
                    .orElse(null);

            // Erstelle Vorschlag nur wenn der andere auch ein Profil hat
            if (otherProfile != null) {
                int compatibilityScore = calculateCompatibility(currentProfile, otherProfile);

                suggestions.add(MatchSuggestionDTO.builder()
                        .userId(otherUser.getId())
                        .username(otherUser.getUsername())
                        .firstName(otherProfile.getFirstName())
                        .lastName(otherProfile.getLastName())
                        .subjects(otherProfile.getSubjects())
                        .schoolOrUniversity(otherProfile.getSchoolOrUniversity())
                        .compatibilityScore(compatibilityScore)
                        .build());
            }
        }

        // Sortiere nach Kompatibilität
        suggestions.sort((a, b) -> Integer.compare(b.getCompatibilityScore(), a.getCompatibilityScore()));

        // Limitiere auf top 5 Vorschläge
        return suggestions.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Berechne Kompatibilität zwischen zwei Profilen
     * Basiert auf gemeinsamen Schulen/Unis
     */
    private int calculateCompatibility(Profile profile1, Profile profile2) {
        int score = 50; // Basis-Score

        // Prüfe auf gleiche Schule/Uni
        if (profile1.getSchoolOrUniversity() != null &&
            profile2.getSchoolOrUniversity() != null &&
            profile1.getSchoolOrUniversity().equalsIgnoreCase(profile2.getSchoolOrUniversity())) {
            score += 30;
        }

        // Prüfe auf Bio-Ähnlichkeit
        if (profile1.getSubjects() != null && profile2.getSubjects() != null) {
            String bio1 = profile1.getSubjects().toLowerCase();
            String bio2 = profile2.getSubjects().toLowerCase();

            String[] keywords = {"mathe", "englisch", "deutsch", "physik", "chemie",
                               "biologie", "informatik", "spanisch", "wirtschaft", "finanz und rechnungswesen"};

            for (String keyword : keywords) {
                if (bio1.contains(keyword) && bio2.contains(keyword)) {
                    score += 5;
                }
            }
        }

        return Math.min(score, 100);
    }

    /**
     * Akzeptiere einen Match-Vorschlag
     */
    public Map<String, Object> acceptMatch(Long userId, Long suggestedUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nutzer nicht gefunden"));
        User suggestedUser = userRepository.findById(suggestedUserId)
                .orElseThrow(() -> new RuntimeException("Vorgeschlagener Nutzer nicht gefunden"));

        // Prüfe ob bereits ein Match existiert
        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(userId, suggestedUserId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();

            // Aktualisiere akzeptiert-Status basierend auf aktuellen Nutzer
            if (match.getUser1().getId().equals(userId)) {
                match.setUser1Accepted(true);
            } else {
                match.setUser2Accepted(true);
            }

            matchRepository.save(match);

            return Map.of(
                    "success", true,
                    "message", match.getStatus().equals("ACCEPTED") ?
                               "Match akzeptiert! Ihr seid jetzt Lernpartner!" :
                               "Anfrage gesendet. Warte auf Bestätigung...",
                    "matchId", match.getId(),
                    "status", match.getStatus()
            );
        } else {
            // Erstelle neuen Match
            Match newMatch = Match.builder()
                    .user1(user)
                    .user2(suggestedUser)
                    .user1Accepted(true)
                    .status("PENDING")
                    .build();

            Match savedMatch = matchRepository.save(newMatch);

            return Map.of(
                    "success", true,
                    "message", "Anfrage gesendet. Warte auf Bestätigung...",
                    "matchId", savedMatch.getId(),
                    "status", savedMatch.getStatus()
            );
        }
    }

    /**
     * Lehne einen Match-Vorschlag ab
     */
    public Map<String, Object> rejectMatch(Long userId, Long suggestedUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nutzer nicht gefunden"));
        User suggestedUser = userRepository.findById(suggestedUserId)
                .orElseThrow(() -> new RuntimeException("Vorgeschlagener Nutzer nicht gefunden"));

        // Prüfe ob bereits ein Match existiert
        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(userId, suggestedUserId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();

            // Aktualisiere rejection Status basierend auf aktuellem Nutzer
            if (match.getUser1().getId().equals(userId)) {
                match.setUser1Rejected(true);
            } else {
                match.setUser2Rejected(true);
            }

            matchRepository.save(match);
        } else {
            // Erstelle Ablehnung-Eintrag wenn kein Match existiert
            RejectedMatch rejection = RejectedMatch.builder()
                    .user(user)
                    .rejectedUser(suggestedUser)
                    .build();

            rejectedMatchRepository.save(rejection);
        }

        return Map.of(
                "success", true,
                "message", "Vorschlag abgelehnt"
        );
    }

    /**
     * Hole alle akzeptierten Matches für einen Nutzer
     */
    public List<Map<String, Object>> getAcceptedMatches(Long userId) {
        List<Match> matches = matchRepository.findAcceptedMatchesByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Match match : matches) {
            User otherUser = match.getUser1().getId().equals(userId) ?
                            match.getUser2() : match.getUser1();
            Profile otherProfile = profileRepository.findByUserId(otherUser.getId())
                    .orElse(null);

            if (otherProfile != null) {
                result.add(Map.of(
                        "userId", otherUser.getId(),
                        "username", otherUser.getUsername(),
                        "firstName", otherProfile.getFirstName(),
                        "lastName", otherProfile.getLastName(),
                        "bio", otherProfile.getSubjects() != null ? otherProfile.getSubjects() : "",
                        "schoolOrUniversity", otherProfile.getSchoolOrUniversity() != null ?
                                            otherProfile.getSchoolOrUniversity() : "",
                        "matchId", match.getId()
                ));
            }
        }

        return result;
    }

    /**
     * Zähle alle akzeptierten Matches für einen Nutzer
     */
    public long countAcceptedMatches(Long userId) {
        return matchRepository.countAcceptedMatchesByUserId(userId);
    }

    /**
     * ✅ NEUE METHODE: Erstelle Initial-Matches für einen neu erstellten Benutzer
     * Dies stellt sicher, dass alle existierenden Benutzer den neuen Benutzer sehen
     */
    public void createInitialMatchesForNewProfile(Long newUserId) {
        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        // ✅ WICHTIG: Hole NUR Benutzer die AUCH ein PROFIL haben!
        List<User> existingUsers = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(newUserId))
                .collect(Collectors.toList());

        for (User otherUser : existingUsers) {
            // Prüfe ob der andere Benutzer ein Profil hat
            Optional<Profile> otherUserProfile = profileRepository.findByUserId(otherUser.getId());
            if (otherUserProfile.isEmpty()) {
                continue; // ✅ Überspringe User ohne Profil
            }

            // Prüfe ob bereits Match existiert
            Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(newUserId, otherUser.getId());
            if (existingMatch.isPresent()) {
                continue;
            }

            // Erstelle neuen Match-Datensatz
            Match newMatch = Match.builder()
                    .user1(newUser)
                    .user2(otherUser)
                    .status("PENDING")
                    .build();

            matchRepository.save(newMatch);
            matchRepository.flush();
        }
    }
}

