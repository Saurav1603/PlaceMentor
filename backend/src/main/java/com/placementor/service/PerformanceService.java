package com.placementor.service;

import com.placementor.model.*;
import com.placementor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final TopicPerformanceRepository topicPerformanceRepository;
    private final SubmissionRepository submissionRepository;
    private final TestRepository testRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updatePerformanceAfterTest(Long userId, Long testId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Submission> submissions = submissionRepository.findByTestId(testId);

        for (Submission submission : submissions) {
            String topic = submission.getQuestion().getTopic();
            TopicPerformance perf = topicPerformanceRepository
                    .findByUserIdAndTopic(userId, topic)
                    .orElse(TopicPerformance.builder()
                            .user(user)
                            .topic(topic)
                            .totalAttempted(0)
                            .correctCount(0)
                            .accuracy(0.0)
                            .level(TopicPerformance.SkillLevel.WEAK)
                            .build());

            perf.setTotalAttempted(perf.getTotalAttempted() + 1);
            if (submission.getIsCorrect()) {
                perf.setCorrectCount(perf.getCorrectCount() + 1);
            }

            double accuracy = perf.getTotalAttempted() > 0
                    ? (double) perf.getCorrectCount() / perf.getTotalAttempted() * 100
                    : 0;
            perf.setAccuracy(Math.round(accuracy * 100.0) / 100.0);

            // Skill level classification
            if (accuracy < 60) {
                perf.setLevel(TopicPerformance.SkillLevel.WEAK);
            } else if (accuracy <= 80) {
                perf.setLevel(TopicPerformance.SkillLevel.MEDIUM);
            } else {
                perf.setLevel(TopicPerformance.SkillLevel.STRONG);
            }

            topicPerformanceRepository.save(perf);
        }
    }

    public List<TopicPerformance> getPerformanceByUserId(Long userId) {
        return topicPerformanceRepository.findByUserId(userId);
    }
}
