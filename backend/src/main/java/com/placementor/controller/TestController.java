package com.placementor.controller;

import com.placementor.dto.*;
import com.placementor.service.AdaptiveLearningService;
import com.placementor.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final AdaptiveLearningService adaptiveLearningService;

    @GetMapping("/start")
    public ResponseEntity<TestStartResponse> startTest(@RequestParam Long userId) {
        return ResponseEntity.ok(testService.startTest(userId));
    }

    @PostMapping("/submit")
    public ResponseEntity<TestResultResponse> submitTest(@RequestParam Long userId,
                                                          @RequestBody TestSubmissionRequest request) {
        TestResultResponse result = testService.submitTest(userId, request);

        // Trigger adaptive learning pipeline
        adaptiveLearningService.processPostTest(userId, request.getTestId());

        return ResponseEntity.ok(result);
    }
}
