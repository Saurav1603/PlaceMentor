package com.placementor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String optionA;

    @Column(nullable = false)
    private String optionB;

    @Column(nullable = false)
    private String optionC;

    @Column(nullable = false)
    private String optionD;

    @Column(nullable = false)
    private String correctAnswer;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
