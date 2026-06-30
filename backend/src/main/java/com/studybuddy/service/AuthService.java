package com.studybuddy.service;

import com.studybuddy.dto.AuthResponse;
import com.studybuddy.dto.LoginRequest;
import com.studybuddy.dto.RegisterRequest;
import com.studybuddy.model.User;
import com.studybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        log.info("Registrierungsanfrage für Email: {}", email);

        // Validierung
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Passwörter stimmen nicht überein")
                    .build();
        }

        // Prüfe, ob Email bereits existiert
        if (userRepository.existsByEmail(email)) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email existiert bereits")
                    .build();
        }

        // Benutzer erstellen (Passwort wird gehasht gespeichert)
        User user = User.builder()
                .email(email)
                .username(request.getUsername().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("Benutzer erfolgreich registriert: {}", savedUser.getId());

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .message("Registrierung erfolgreich")
                .success(true)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());
        log.info("Login-Anfrage für Email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        // Gleiche Fehlermeldung für unbekannte Email und falsches Passwort,
        // damit keine Rückschlüsse auf existierende Konten möglich sind.
        if (userOptional.isEmpty()
                || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email oder Passwort ist ungültig")
                    .build();
        }

        User user = userOptional.get();
        log.info("Login erfolgreich für Benutzer: {}", user.getId());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .message("Login erfolgreich")
                .success(true)
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}

