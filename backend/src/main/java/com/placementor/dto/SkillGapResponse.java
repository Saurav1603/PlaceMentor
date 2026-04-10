package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SkillGapResponse {
    private List<TopicGap> gaps;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TopicGap {
        private String topic;
        private Double accuracy;
        private String level;
    }
}
