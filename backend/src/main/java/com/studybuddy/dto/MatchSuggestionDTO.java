package com.studybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO für Match-Vorschläge
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchSuggestionDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String subjects;
    private String schoolOrUniversity;
    private int compatibilityScore; // Kompatibilitätsscore 0-100
}

