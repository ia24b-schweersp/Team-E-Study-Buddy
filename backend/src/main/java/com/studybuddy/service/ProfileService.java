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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

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
            profile.setBio(request.getBio());
            profile.setSchoolOrUniversity(request.getSchoolOrUniversity());
            Profile updatedProfile = profileRepository.save(profile);

            log.info("Profil aktualisiert für Benutzer: {}", userId);

            return ProfileResponse.builder()
                    .id(updatedProfile.getId())
                    .userId(updatedProfile.getUser().getId())
                    .firstName(updatedProfile.getFirstName())
                    .lastName(updatedProfile.getLastName())
                    .bio(updatedProfile.getBio())
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
                .bio(request.getBio())
                .schoolOrUniversity(request.getSchoolOrUniversity())
                .build();

        Profile savedProfile = profileRepository.save(profile);
        log.info("Profil erstellt für Benutzer: {}", userId);

        return ProfileResponse.builder()
                .id(savedProfile.getId())
                .userId(savedProfile.getUser().getId())
                .firstName(savedProfile.getFirstName())
                .lastName(savedProfile.getLastName())
                .bio(savedProfile.getBio())
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
                .bio(profile.getBio())
                .schoolOrUniversity(profile.getSchoolOrUniversity())
                .success(true)
                .build();
    }
}

