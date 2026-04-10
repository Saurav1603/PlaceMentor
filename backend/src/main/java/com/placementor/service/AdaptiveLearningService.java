package com.placementor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdaptiveLearningService {

    private final PerformanceService performanceService;
    private final SkillGapService skillGapService;
    private final RoadmapService roadmapService;
    private final RecommendationService recommendationService;

    /**
     * Post-test adaptive learning pipeline:
     * 1. Update performance metrics
     * 2. Recalculate skill gaps (happens automatically since performance is updated)
     * 3. Regenerate roadmap based on new skill levels
     * 4. Regenerate recommendations based on new skill levels
     */
    @Transactional
    public void processPostTest(Long userId, Long testId) {
        // Step 1: Update topic-wise performance
        performanceService.updatePerformanceAfterTest(userId, testId);

        // Step 2 & 3: Regenerate roadmap (skill gaps recalculated internally)
        roadmapService.generateRoadmap(userId);

        // Step 4: Regenerate recommendations
        recommendationService.generateRecommendations(userId);
    }
}
