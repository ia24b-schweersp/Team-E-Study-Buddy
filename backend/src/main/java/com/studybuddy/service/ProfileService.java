package com.studybuddy.service;

import com.studybuddy.dto.ProfileRequest;
import com.studybuddy.dto.ProfileResponse;
import com.studybuddy.model.Profile;
import com.studybuddy.model.User;
import com.studybuddy.repository.ProfileRepository;
import com.studybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final MatchService matchService;

    private String normalizeSubjects(String subjects) {
        if (subjects == null || subjects.isBlank()) {
            return "";
        }

        return Arrays.stream(subjects.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @Transactional
    public ProfileResponse createProfile(Long userId, ProfileRequest request) {
        log.info("Profil erstellen für Benutzer: {}", userId);

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ProfileResponse.builder()
                    .success(false)
                    .message("Benutzer nicht gefunden")
                    .build();
        }

        User user = userOptional.get();

        // Prüfe, ob bereits ein Profil existiert
        Optional<Profile> existingProfile = profileRepository.findByUser(user);
        if (existingProfile.isPresent()) {
            Profile profile = existingProfile.get();
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            profile.setSubjects(normalizeSubjects(request.getSubjects()));
            profile.setSchoolOrUniversity(request.getSchoolOrUniversity());
            Profile updatedProfile = profileRepository.save(profile);

            log.info("Profil aktualisiert für Benutzer: {}", userId);

            return ProfileResponse.builder()
                    .id(updatedProfile.getId())
                    .userId(updatedProfile.getUser().getId())
                    .firstName(updatedProfile.getFirstName())
                    .lastName(updatedProfile.getLastName())
                    .subjects(updatedProfile.getSubjects())
                    .schoolOrUniversity(updatedProfile.getSchoolOrUniversity())
                    .message("Profil aktualisiert")
                    .success(true)
                    .build();
        }

        // Neues Profil erstellen
        Profile profile = Profile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .subjects(normalizeSubjects(request.getSubjects()))
                .schoolOrUniversity(request.getSchoolOrUniversity())
                .build();

        Profile savedProfile = profileRepository.save(profile);
        profileRepository.flush(); // ✅ Stelle sicher dass das Profil in die DB geschrieben wird

        log.info("Profil gespeichert für Benutzer: {}, jetzt erstelle Matches", userId);

        // ✅ Erstelle Initial-Matches mit allen anderen Benutzern
        try {
            matchService.createInitialMatchesForNewProfile(userId);
            log.info("Initial-Matches erfolgreich erstellt für neuen Benutzer: {}", userId);
        } catch (Exception e) {
            log.error("Fehler beim Erstellen von Initial-Matches für Benutzer: {}", userId, e);
            throw new RuntimeException("Fehler beim Erstellen von Initial-Matches", e);
        }

        log.info("Profil erstellt und Matches initialisiert für Benutzer: {}", userId);

        return ProfileResponse.builder()
                .id(savedProfile.getId())
                .userId(savedProfile.getUser().getId())
                .firstName(savedProfile.getFirstName())
                .lastName(savedProfile.getLastName())
                .subjects(savedProfile.getSubjects())
                .schoolOrUniversity(savedProfile.getSchoolOrUniversity())
                .message("Profil erfolgreich erstellt")
                .success(true)
                .build();
    }

    public ProfileResponse getProfile(Long userId) {
        log.info("Profil abrufen für Benutzer: {}", userId);

        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);

        if (profileOptional.isEmpty()) {
            return ProfileResponse.builder()
                    .success(false)
                    .message("Profil nicht gefunden")
                    .build();
        }

        Profile profile = profileOptional.get();

        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .subjects(profile.getSubjects())
                .schoolOrUniversity(profile.getSchoolOrUniversity())
                .success(true)
                .build();
    }
}

