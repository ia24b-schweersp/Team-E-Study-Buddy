package com.studybuddy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {

    @NotBlank(message = "Vorname ist erforderlich")
    private String firstName;

    @NotBlank(message = "Nachname ist erforderlich")
    private String lastName;

    private String bio;

    private String schoolOrUniversity;
}

