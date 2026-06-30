package com.studybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String subjects;
    private String schoolOrUniversity;
    private String message;
    private boolean success;
}

