package com.studybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO für Match-Akzeptierung/Ablehnung
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchActionDTO {
    private Long matchId;
    private String action; // "accept" oder "reject"
    private String message;
    private Boolean success;
}

