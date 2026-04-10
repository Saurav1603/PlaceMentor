package com.placementor.controller;

import com.placementor.dto.QuestionDTO;
import com.placementor.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO dto) {
        return ResponseEntity.ok(questionService.createQuestion(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<QuestionDTO>> getByTopic(@PathVariable String topic) {
        return ResponseEntity.ok(questionService.getQuestionsByTopic(topic));
    }
}
