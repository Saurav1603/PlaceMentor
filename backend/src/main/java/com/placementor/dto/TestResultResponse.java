package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestResultResponse {
    private Long testId;
    private Integer score;
    private Integer totalQuestions;
    private Double accuracy;
    private List<SubmissionResult> results;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SubmissionResult {
        private Long questionId;
        private String questionText;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
    }
}
