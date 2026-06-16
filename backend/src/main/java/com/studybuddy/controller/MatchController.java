package com.studybuddy.controller;

import com.studybuddy.dto.MatchSuggestionDTO;
import com.studybuddy.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller für Match- und Lernpartner-Management
 */
@RestController
@RequestMapping("/api/match")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * Hole Match-Vorschläge für einen Nutzer
     */
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<?> getMatchSuggestions(@PathVariable Long userId) {
        try {
            List<MatchSuggestionDTO> suggestions = matchService.generateMatchSuggestions(userId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "suggestions", suggestions,
                    "message", "Match-Vorschläge geladen"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Fehler beim Laden der Vorschläge: " + e.getMessage()
                    ));
        }
    }

    /**
     * Akzeptiere einen Match-Vorschlag
     */
    @PostMapping("/accept")
    public ResponseEntity<?> acceptMatch(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        try {
            Long suggestedUserId = request.get("suggestedUserId");
            Map<String, Object> result = matchService.acceptMatch(userId, suggestedUserId);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Fehler beim Akzeptieren: " + e.getMessage()
                    ));
        }
    }

    /**
     * Lehne einen Match-Vorschlag ab
     */
    @PostMapping("/reject")
    public ResponseEntity<?> rejectMatch(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        try {
            Long suggestedUserId = request.get("suggestedUserId");
            Map<String, Object> result = matchService.rejectMatch(userId, suggestedUserId);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Fehler beim Ablehnen: " + e.getMessage()
                    ));
        }
    }

    /**
     * Hole alle akzeptierten Matches für einen Nutzer
     */
    @GetMapping("/accepted/{userId}")
    public ResponseEntity<?> getAcceptedMatches(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> matches = matchService.getAcceptedMatches(userId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "matches", matches,
                    "count", matches.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Fehler beim Laden der Matches: " + e.getMessage()
                    ));
        }
    }

    /**
     * Zähle akzeptierte Matches
     */
    @GetMapping("/count/{userId}")
    public ResponseEntity<?> countMatches(@PathVariable Long userId) {
        try {
            long count = matchService.countAcceptedMatches(userId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Fehler beim Zählen: " + e.getMessage()
                    ));
        }
    }
}

