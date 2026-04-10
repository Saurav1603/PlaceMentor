package com.placementor.service;

import com.placementor.dto.*;
import com.placementor.exception.BadRequestException;
import com.placementor.exception.ResourceNotFoundException;
import com.placementor.model.*;
import com.placementor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public TestStartResponse startTest(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get 10 random questions for the test
        List<Question> questions = questionRepository.findRandomQuestions(10);

        if (questions.isEmpty()) {
            throw new BadRequestException("No questions available. Please contact admin.");
        }

        // Create test record
        Test test = Test.builder()
                .user(user)
                .totalQuestions(questions.size())
                .completed(false)
                .build();
        test = testRepository.save(test);

        // Build response
        List<TestStartResponse.TestQuestion> testQuestions = questions.stream()
                .map(q -> TestStartResponse.TestQuestion.builder()
                        .questionId(q.getId())
                        .topic(q.getTopic())
                        .difficulty(q.getDifficulty().name())
                        .questionText(q.getQuestionText())
                        .optionA(q.getOptionA())
                        .optionB(q.getOptionB())
                        .optionC(q.getOptionC())
                        .optionD(q.getOptionD())
                        .build())
                .collect(Collectors.toList());

        return TestStartResponse.builder()
                .testId(test.getId())
                .totalQuestions(questions.size())
                .questions(testQuestions)
                .build();
    }

    @Transactional
    public TestResultResponse submitTest(Long userId, TestSubmissionRequest request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        if (test.getCompleted()) {
            throw new BadRequestException("Test already submitted");
        }

        if (!test.getUser().getId().equals(userId)) {
            throw new BadRequestException("This test does not belong to you");
        }

        int score = 0;
        List<TestResultResponse.SubmissionResult> results = new ArrayList<>();

        for (TestSubmissionRequest.AnswerDTO answer : request.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + answer.getQuestionId()));

            boolean correct = question.getCorrectAnswer().equalsIgnoreCase(answer.getSelectedAnswer());
            if (correct) score++;

            Submission submission = Submission.builder()
                    .test(test)
                    .question(question)
                    .selectedAnswer(answer.getSelectedAnswer())
                    .isCorrect(correct)
                    .build();
            submissionRepository.save(submission);

            results.add(TestResultResponse.SubmissionResult.builder()
                    .questionId(question.getId())
                    .questionText(question.getQuestionText())
                    .selectedAnswer(answer.getSelectedAnswer())
                    .correctAnswer(question.getCorrectAnswer())
                    .isCorrect(correct)
                    .build());
        }

        double accuracy = request.getAnswers().isEmpty() ? 0 :
                (double) score / request.getAnswers().size() * 100;

        test.setScore(score);
        test.setAccuracy(accuracy);
        test.setCompleted(true);
        test.setEndTime(LocalDateTime.now());
        testRepository.save(test);

        return TestResultResponse.builder()
                .testId(test.getId())
                .score(score)
                .totalQuestions(request.getAnswers().size())
                .accuracy(Math.round(accuracy * 100.0) / 100.0)
                .results(results)
                .build();
    }
}
