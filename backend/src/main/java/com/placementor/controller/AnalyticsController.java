package com.placementor.controller;

import com.placementor.dto.DashboardResponse;
import com.placementor.dto.SkillGapResponse;
import com.placementor.model.TopicPerformance;
import com.placementor.service.DashboardService;
import com.placementor.service.PerformanceService;
import com.placementor.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalyticsController {

    private final PerformanceService performanceService;
    private final SkillGapService skillGapService;
    private final DashboardService dashboardService;

    @GetMapping("/performance/{userId}")
    public ResponseEntity<List<Object>> getPerformance(@PathVariable Long userId) {
        List<TopicPerformance> performances = performanceService.getPerformanceByUserId(userId);
        List<Object> response = performances.stream()
                .map(p -> {
                    var map = new java.util.HashMap<String, Object>();
                    map.put("topic", p.getTopic());
                    map.put("totalAttempted", p.getTotalAttempted());
                    map.put("correctCount", p.getCorrectCount());
                    map.put("accuracy", p.getAccuracy());
                    map.put("level", p.getLevel().name());
                    return (Object) map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/skill-gap/{userId}")
    public ResponseEntity<SkillGapResponse> getSkillGaps(@PathVariable Long userId) {
        return ResponseEntity.ok(skillGapService.getSkillGaps(userId));
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboard(userId));
    }
}
