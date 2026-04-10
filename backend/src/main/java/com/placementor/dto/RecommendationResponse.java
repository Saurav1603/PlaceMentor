package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecommendationResponse {
    private List<RecommendedQuestion> recommendations;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendedQuestion {
        private Long questionId;
        private String topic;
        private String difficulty;
        private String questionText;
        private String reason;
    }
}
