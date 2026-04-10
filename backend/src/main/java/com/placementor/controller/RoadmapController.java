package com.placementor.controller;

import com.placementor.dto.RoadmapResponse;
import com.placementor.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @GetMapping("/{userId}")
    public ResponseEntity<RoadmapResponse> getRoadmap(@PathVariable Long userId) {
        return ResponseEntity.ok(roadmapService.getRoadmap(userId));
    }

    @PostMapping("/generate/{userId}")
    public ResponseEntity<String> generateRoadmap(@PathVariable Long userId) {
        roadmapService.generateRoadmap(userId);
        return ResponseEntity.ok("Roadmap generated successfully");
    }
}
