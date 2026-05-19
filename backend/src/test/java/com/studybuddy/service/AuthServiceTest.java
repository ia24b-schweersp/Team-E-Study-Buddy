package com.studybuddy.service;

import com.studybuddy.dto.LoginRequest;
import com.studybuddy.dto.RegisterRequest;
import com.studybuddy.model.User;
import com.studybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Datenbank vor jedem Test leeren
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        // Act
        var response = authService.register(request);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getUserId());
    }

    @Test
    void testRegisterPasswordMismatch() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("different123")
                .build();

        // Act
        var response = authService.register(request);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Passwörter stimmen nicht überein"));
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Arrange
        RegisterRequest request1 = RegisterRequest.builder()
                .username("user1")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        RegisterRequest request2 = RegisterRequest.builder()
                .username("user2")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        // Act
        authService.register(request1);
        var response2 = authService.register(request2);

        // Assert
        assertFalse(response2.isSuccess());
        assertTrue(response2.getMessage().contains("Email existiert bereits"));
    }

    @Test
    void testLoginSuccess() {
        // Arrange - Register first
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();
        authService.register(registerRequest);

        // Act
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();
        var response = authService.login(loginRequest);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void testLoginInvalidEmail() {
        // Act
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();
        var response = authService.login(request);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Email oder Passwort ist ungültig"));
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange - Register first
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();
        authService.register(registerRequest);

        // Act
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();
        var response = authService.login(loginRequest);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Email oder Passwort ist ungültig"));
    }
}

