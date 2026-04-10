package com.placementor.service;

import com.placementor.dto.DashboardResponse;
import com.placementor.exception.ResourceNotFoundException;
import com.placementor.model.TopicPerformance;
import com.placementor.model.Test;
import com.placementor.model.User;
import com.placementor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;

    public DashboardResponse getDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Test> completedTests = testRepository.findByUserIdAndCompleted(userId, true);
        List<TopicPerformance> performances = topicPerformanceRepository.findByUserId(userId);

        // Calculate overall accuracy
        double overallAccuracy = completedTests.stream()
                .filter(t -> t.getAccuracy() != null)
                .mapToDouble(Test::getAccuracy)
                .average()
                .orElse(0.0);

        // Total questions attempted
        int totalQuestions = completedTests.stream()
                .filter(t -> t.getTotalQuestions() != null)
                .mapToInt(Test::getTotalQuestions)
                .sum();

        // Determine learning level
        String learningLevel;
        if (completedTests.size() < 3) {
            learningLevel = "Beginner";
        } else if (overallAccuracy < 50) {
            learningLevel = "Beginner";
        } else if (overallAccuracy < 75) {
            learningLevel = "Intermediate";
        } else {
            learningLevel = "Advanced";
        }

        // Placement readiness score (0-100)
        double readiness = calculateReadinessScore(overallAccuracy, completedTests.size(), performances);

        // Weak and strong topics
        List<String> weakTopics = performances.stream()
                .filter(p -> p.getLevel() == TopicPerformance.SkillLevel.WEAK)
                .map(TopicPerformance::getTopic)
                .collect(Collectors.toList());

        List<String> strongTopics = performances.stream()
                .filter(p -> p.getLevel() == TopicPerformance.SkillLevel.STRONG)
                .map(TopicPerformance::getTopic)
                .collect(Collectors.toList());

        // Topic performance DTOs
        List<DashboardResponse.TopicPerformanceDTO> topicDTOs = performances.stream()
                .map(p -> DashboardResponse.TopicPerformanceDTO.builder()
                        .topic(p.getTopic())
                        .accuracy(p.getAccuracy())
                        .level(p.getLevel().name())
                        .totalAttempted(p.getTotalAttempted())
                        .correctCount(p.getCorrectCount())
                        .build())
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .name(user.getName())
                .goal(user.getGoal() != null ? user.getGoal().name() : "PLACEMENT")
                .overallAccuracy(Math.round(overallAccuracy * 100.0) / 100.0)
                .learningLevel(learningLevel)
                .placementReadinessScore(Math.round(readiness * 100.0) / 100.0)
                .totalTestsTaken(completedTests.size())
                .totalQuestionsAttempted(totalQuestions)
                .topicPerformances(topicDTOs)
                .weakTopics(weakTopics)
                .strongTopics(strongTopics)
                .build();
    }

    private double calculateReadinessScore(double accuracy, int testsTaken, List<TopicPerformance> performances) {
        // Readiness is weighted: 50% accuracy + 25% test coverage + 25% topic strength
        double accuracyScore = accuracy;

        // Test coverage: how many tests taken (max 10 for full score)
        double coverageScore = Math.min(testsTaken / 10.0, 1.0) * 100;

        // Topic strength: percentage of topics at MEDIUM or STRONG
        double topicScore = 0;
        if (!performances.isEmpty()) {
            long strong = performances.stream()
                    .filter(p -> p.getLevel() != TopicPerformance.SkillLevel.WEAK)
                    .count();
            topicScore = (double) strong / performances.size() * 100;
        }

        return accuracyScore * 0.5 + coverageScore * 0.25 + topicScore * 0.25;
    }
}
