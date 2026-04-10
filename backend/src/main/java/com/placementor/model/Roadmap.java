package com.placementor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roadmap")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer weekNumber;

    @Column(nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String activity;

    @Column(nullable = false)
    private Boolean completed = false;
}
