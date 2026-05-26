package com.studybuddy.controller;

import com.studybuddy.dto.ProfileRequest;
import com.studybuddy.dto.ProfileResponse;
import com.studybuddy.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ProfileRequest request) {
        log.info("POST /api/profile - Profil erstellen für Benutzer: {}", userId);
        ProfileResponse response = profileService.createProfile(userId, request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        log.info("GET /api/profile/{} - Profil abrufen", userId);
        ProfileResponse response = profileService.getProfile(userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

