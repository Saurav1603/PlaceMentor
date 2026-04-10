package com.placementor.service;

import com.placementor.dto.QuestionDTO;
import com.placementor.exception.ResourceNotFoundException;
import com.placementor.model.Question;
import com.placementor.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(Long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return toDTO(q);
    }

    public QuestionDTO createQuestion(QuestionDTO dto) {
        Question question = Question.builder()
                .topic(dto.getTopic())
                .difficulty(Question.Difficulty.valueOf(dto.getDifficulty().toUpperCase()))
                .questionText(dto.getQuestionText())
                .optionA(dto.getOptionA())
                .optionB(dto.getOptionB())
                .optionC(dto.getOptionC())
                .optionD(dto.getOptionD())
                .correctAnswer(dto.getCorrectAnswer())
                .build();
        return toDTO(questionRepository.save(question));
    }

    public QuestionDTO updateQuestion(Long id, QuestionDTO dto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        question.setTopic(dto.getTopic());
        question.setDifficulty(Question.Difficulty.valueOf(dto.getDifficulty().toUpperCase()));
        question.setQuestionText(dto.getQuestionText());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setCorrectAnswer(dto.getCorrectAnswer());
        return toDTO(questionRepository.save(question));
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    public List<QuestionDTO> getQuestionsByTopic(String topic) {
        return questionRepository.findByTopic(topic).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private QuestionDTO toDTO(Question q) {
        return QuestionDTO.builder()
                .id(q.getId())
                .topic(q.getTopic())
                .difficulty(q.getDifficulty().name())
                .questionText(q.getQuestionText())
                .optionA(q.getOptionA())
                .optionB(q.getOptionB())
                .optionC(q.getOptionC())
                .optionD(q.getOptionD())
                .correctAnswer(q.getCorrectAnswer())
                .build();
    }
}
