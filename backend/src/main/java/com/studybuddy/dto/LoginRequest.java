package com.studybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email ist erforderlich")
    @Email(message = "Email sollte gültig sein")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich")
    private String password;
}

