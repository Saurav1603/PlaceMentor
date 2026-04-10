package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResponse {
    private String name;
    private String goal;
    private Double overallAccuracy;
    private String learningLevel;
    private Double placementReadinessScore;
    private Integer totalTestsTaken;
    private Integer totalQuestionsAttempted;
    private List<TopicPerformanceDTO> topicPerformances;
    private List<String> weakTopics;
    private List<String> strongTopics;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TopicPerformanceDTO {
        private String topic;
        private Double accuracy;
        private String level;
        private Integer totalAttempted;
        private Integer correctCount;
    }
}
