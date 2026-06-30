package com.studybuddy.controller;

import com.studybuddy.dto.MatchSuggestionDTO;
import com.studybuddy.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller für Match- und Lernpartner-Management.
 * Fehler werden zentral im {@link GlobalExceptionHandler} behandelt.
 */
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * Hole Match-Vorschläge für einen Nutzer.
     */
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<Map<String, Object>> getMatchSuggestions(@PathVariable Long userId) {
        List<MatchSuggestionDTO> suggestions = matchService.generateMatchSuggestions(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "suggestions", suggestions,
                "message", "Match-Vorschläge geladen"
        ));
    }

    /**
     * Akzeptiere einen Match-Vorschlag.
     */
    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> acceptMatch(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        Map<String, Object> result = matchService.acceptMatch(userId, request.get("suggestedUserId"));
        return ResponseEntity.ok(result);
    }

    /**
     * Lehne einen Match-Vorschlag ab.
     */
    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> rejectMatch(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        Map<String, Object> result = matchService.rejectMatch(userId, request.get("suggestedUserId"));
        return ResponseEntity.ok(result);
    }

    /**
     * Hole alle akzeptierten Matches für einen Nutzer.
     */
    @GetMapping("/accepted/{userId}")
    public ResponseEntity<Map<String, Object>> getAcceptedMatches(@PathVariable Long userId) {
        List<Map<String, Object>> matches = matchService.getAcceptedMatches(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "matches", matches,
                "count", matches.size()
        ));
    }

    /**
     * Zähle akzeptierte Matches.
     */
    @GetMapping("/count/{userId}")
    public ResponseEntity<Map<String, Object>> countMatches(@PathVariable Long userId) {
        long count = matchService.countAcceptedMatches(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", count
        ));
    }
}
