package com.placementor.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String goal;
    private Integer dailyStudyHours;
}
