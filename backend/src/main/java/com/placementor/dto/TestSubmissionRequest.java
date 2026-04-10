package com.placementor.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestSubmissionRequest {
    private Long testId;
    private List<AnswerDTO> answers;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AnswerDTO {
        private Long questionId;
        private String selectedAnswer;
    }
}
