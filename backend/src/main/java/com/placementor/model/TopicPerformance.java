package com.placementor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic_performance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TopicPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String topic;

    private Integer totalAttempted = 0;
    private Integer correctCount = 0;
    private Double accuracy = 0.0;

    @Enumerated(EnumType.STRING)
    private SkillLevel level = SkillLevel.WEAK;

    public enum SkillLevel {
        WEAK, MEDIUM, STRONG
    }
}
