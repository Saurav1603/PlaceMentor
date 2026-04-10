package com.placementor.repository;

import com.placementor.model.TopicPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TopicPerformanceRepository extends JpaRepository<TopicPerformance, Long> {
    List<TopicPerformance> findByUserId(Long userId);
    Optional<TopicPerformance> findByUserIdAndTopic(Long userId, String topic);
}
