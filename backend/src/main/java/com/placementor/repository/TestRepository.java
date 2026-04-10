package com.placementor.repository;

import com.placementor.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByUserIdOrderByStartTimeDesc(Long userId);
    List<Test> findByUserIdAndCompleted(Long userId, Boolean completed);
}
