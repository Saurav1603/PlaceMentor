package com.placementor.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer score;
    private Double accuracy;
    private Integer totalQuestions;

    @Column(nullable = false)
    private Boolean completed = false;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Submission> submissions;

    @PrePersist
    protected void onCreate() {
        this.startTime = LocalDateTime.now();
        this.completed = false;
    }
}
