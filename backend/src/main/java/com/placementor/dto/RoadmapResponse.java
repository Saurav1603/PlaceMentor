package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoadmapResponse {
    private List<WeekPlan> weeks;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class WeekPlan {
        private Integer weekNumber;
        private String topic;
        private List<DayPlan> days;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DayPlan {
        private Integer dayNumber;
        private String activity;
        private Boolean completed;
    }
}
