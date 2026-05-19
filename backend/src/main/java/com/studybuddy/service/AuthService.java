package com.studybuddy.service;

import com.studybuddy.dto.AuthResponse;
import com.studybuddy.dto.LoginRequest;
import com.studybuddy.dto.RegisterRequest;
import com.studybuddy.model.User;
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
public class AuthService {

    private final UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registrierungsanfrage für Email: {}", request.getEmail());

        // Validierung
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Passwörter stimmen nicht überein")
                    .build();
        }

        // Prüfe, ob Email bereits existiert
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email existiert bereits")
                    .build();
        }

        // Benutzer erstellen
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword()) // In Produktion: Hashing verwenden!
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
        log.info("Login-Anfrage für Email: {}", request.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email oder Passwort ist ungültig")
                    .build();
        }

        User user = userOptional.get();

        // Einfache Passwort-Verifikation (In Produktion: BCrypt verwenden!)
        if (!user.getPassword().equals(request.getPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email oder Passwort ist ungültig")
                    .build();
        }

        log.info("Login erfolgreich für Benutzer: {}", user.getId());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .message("Login erfolgreich")
                .success(true)
                .build();
    }
}

