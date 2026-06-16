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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service für Match- und Lernpartner-Management
 */
@Service
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

        // Hole alle anderen Nutzer mit Profilen
        List<User> allUsers = userRepository.findAll();
        List<MatchSuggestionDTO> suggestions = new ArrayList<>();

        for (User otherUser : allUsers) {
            // Überspringe den aktuellen Nutzer
            if (otherUser.getId().equals(userId)) {
                continue;
            }

            // Prüfe ob bereits ein Match existiert
            Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(userId, otherUser.getId());
            if (existingMatch.isPresent()) {
                continue;
            }

            // Prüfe ob dieser Nutzer bereits abgelehnt wurde
            Optional<RejectedMatch> rejected = rejectedMatchRepository.findRejection(userId, otherUser.getId());
            if (rejected.isPresent()) {
                continue;
            }

            // Hole das Profil des anderen Nutzers
            Profile otherProfile = profileRepository.findByUserId(otherUser.getId())
                    .orElse(null);

            // Erstelle Vorschlag nur wenn beide Profile haben
            if (otherProfile != null && currentProfile != null) {
                int compatibilityScore = calculateCompatibility(currentProfile, otherProfile);

                suggestions.add(MatchSuggestionDTO.builder()
                        .userId(otherUser.getId())
                        .username(otherUser.getUsername())
                        .firstName(otherProfile.getFirstName())
                        .lastName(otherProfile.getLastName())
                        .bio(otherProfile.getBio())
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
        if (profile1.getBio() != null && profile2.getBio() != null) {
            String bio1 = profile1.getBio().toLowerCase();
            String bio2 = profile2.getBio().toLowerCase();

            String[] keywords = {"mathe", "englisch", "deutsch", "physik", "chemie",
                               "biologie", "informatik", "sprache", "lernen", "studium"};

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

        // Erstelle Ablehnung-Eintrag
        RejectedMatch rejection = RejectedMatch.builder()
                .user(user)
                .rejectedUser(suggestedUser)
                .build();

        rejectedMatchRepository.save(rejection);

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
                        "bio", otherProfile.getBio() != null ? otherProfile.getBio() : "",
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
}

