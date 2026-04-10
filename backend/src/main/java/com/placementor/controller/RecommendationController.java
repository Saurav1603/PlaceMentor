package com.placementor.controller;

import com.placementor.dto.RecommendationResponse;
import com.placementor.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }
}
