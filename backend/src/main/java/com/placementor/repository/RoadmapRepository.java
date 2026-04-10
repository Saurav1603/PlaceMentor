package com.placementor.repository;

import com.placementor.model.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByUserIdOrderByWeekNumberAscDayNumberAsc(Long userId);
    void deleteByUserId(Long userId);
}
