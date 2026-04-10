package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestStartResponse {
    private Long testId;
    private Integer totalQuestions;
    private List<TestQuestion> questions;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TestQuestion {
        private Long questionId;
        private String topic;
        private String difficulty;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
    }
}
