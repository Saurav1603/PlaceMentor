package com.placementor.service;

import com.placementor.dto.SkillGapResponse;
import com.placementor.model.TopicPerformance;
import com.placementor.repository.TopicPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillGapService {

    private final TopicPerformanceRepository topicPerformanceRepository;

    public SkillGapResponse getSkillGaps(Long userId) {
        List<TopicPerformance> performances = topicPerformanceRepository.findByUserId(userId);

        List<SkillGapResponse.TopicGap> gaps = performances.stream()
                .map(p -> SkillGapResponse.TopicGap.builder()
                        .topic(p.getTopic())
                        .accuracy(p.getAccuracy())
                        .level(p.getLevel().name())
                        .build())
                .collect(Collectors.toList());

        return SkillGapResponse.builder().gaps(gaps).build();
    }

    public List<TopicPerformance> getWeakTopics(Long userId) {
        return topicPerformanceRepository.findByUserId(userId).stream()
                .filter(p -> p.getLevel() == TopicPerformance.SkillLevel.WEAK)
                .collect(Collectors.toList());
    }
}
