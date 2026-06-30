package com.studybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Allgemeine Anwendungs-Beans.
 */
@Configuration
public class AppConfig {

    /**
     * BCrypt-basierter PasswordEncoder zum sicheren Hashen von Passwörtern.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
