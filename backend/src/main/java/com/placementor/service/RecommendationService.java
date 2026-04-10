package com.placementor.service;

import com.placementor.dto.RecommendationResponse;
import com.placementor.model.*;
import com.placementor.repository.*;
import com.placementor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void generateRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Clear old recommendations
        recommendationRepository.deleteByUserId(userId);

        List<TopicPerformance> performances = topicPerformanceRepository.findByUserId(userId);

        for (TopicPerformance perf : performances) {
            Question.Difficulty targetDifficulty;
            String reason;

            switch (perf.getLevel()) {
                case WEAK:
                    targetDifficulty = Question.Difficulty.EASY;
                    reason = "Your accuracy in " + perf.getTopic() + " is " + perf.getAccuracy() + "%. Start with easy questions to build foundations.";
                    break;
                case MEDIUM:
                    targetDifficulty = Question.Difficulty.MEDIUM;
                    reason = "You're progressing in " + perf.getTopic() + " (" + perf.getAccuracy() + "%). Try medium difficulty to improve.";
                    break;
                default:
                    targetDifficulty = Question.Difficulty.HARD;
                    reason = "You're strong in " + perf.getTopic() + " (" + perf.getAccuracy() + "%). Challenge yourself with hard questions.";
                    break;
            }

            List<Question> questions = questionRepository.findByTopicAndDifficulty(
                    perf.getTopic(), targetDifficulty);

            // Take up to 3 questions per topic
            int limit = Math.min(3, questions.size());
            for (int i = 0; i < limit; i++) {
                Recommendation rec = Recommendation.builder()
                        .user(user)
                        .question(questions.get(i))
                        .reason(reason)
                        .build();
                recommendationRepository.save(rec);
            }
        }
    }

    public RecommendationResponse getRecommendations(Long userId) {
        List<Recommendation> recs = recommendationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<RecommendationResponse.RecommendedQuestion> questions = recs.stream()
                .map(r -> RecommendationResponse.RecommendedQuestion.builder()
                        .questionId(r.getQuestion().getId())
                        .topic(r.getQuestion().getTopic())
                        .difficulty(r.getQuestion().getDifficulty().name())
                        .questionText(r.getQuestion().getQuestionText())
                        .reason(r.getReason())
                        .build())
                .collect(Collectors.toList());

        return RecommendationResponse.builder().recommendations(questions).build();
    }
}
