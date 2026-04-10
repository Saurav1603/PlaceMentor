package com.placementor.service;

import com.placementor.dto.RoadmapResponse;
import com.placementor.model.Roadmap;
import com.placementor.model.TopicPerformance;
import com.placementor.model.User;
import com.placementor.repository.RoadmapRepository;
import com.placementor.repository.TopicPerformanceRepository;
import com.placementor.repository.UserRepository;
import com.placementor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;
    private final UserRepository userRepository;

    // Activities per topic for daily plans
    private static final Map<String, List<String>> TOPIC_ACTIVITIES = Map.of(
            "Arrays", List.of("Two Pointer Technique", "Sliding Window", "Prefix Sum", "Kadane's Algorithm", "Binary Search on Arrays", "Matrix Traversal", "Array Rotation"),
            "Graphs", List.of("BFS Traversal", "DFS Traversal", "Dijkstra's Algorithm", "Topological Sort", "Union Find", "Cycle Detection", "Shortest Path"),
            "DP", List.of("Fibonacci Variants", "Knapsack Problems", "LCS / LIS", "Matrix Chain", "Coin Change", "Subset Sum", "Interval DP"),
            "Trees", List.of("Inorder Traversal", "Level Order Traversal", "BST Operations", "Lowest Common Ancestor", "Tree Diameter", "Balanced Tree Check", "Serialization"),
            "Sorting", List.of("Merge Sort Practice", "Quick Sort Practice", "Counting Sort", "Bucket Sort", "Sort Applications", "Comparator Design", "Stability Analysis")
    );

    @Transactional
    public void generateRoadmap(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Clear existing roadmap
        roadmapRepository.deleteByUserId(userId);

        List<TopicPerformance> performances = topicPerformanceRepository.findByUserId(userId);

        // Prioritize: WEAK first, then MEDIUM, then STRONG
        List<TopicPerformance> sorted = performances.stream()
                .sorted(Comparator.comparingInt(p -> {
                    switch (p.getLevel()) {
                        case WEAK: return 0;
                        case MEDIUM: return 1;
                        case STRONG: return 2;
                        default: return 3;
                    }
                }))
                .collect(Collectors.toList());

        // If no performance data, create default roadmap from all topics
        if (sorted.isEmpty()) {
            List<String> defaultTopics = List.of("Arrays", "Sorting", "Trees", "Graphs", "DP");
            int week = 1;
            for (String topic : defaultTopics) {
                createWeekPlan(user, week++, topic);
            }
            return;
        }

        int week = 1;
        for (TopicPerformance perf : sorted) {
            createWeekPlan(user, week++, perf.getTopic());
        }
    }

    private void createWeekPlan(User user, int weekNumber, String topic) {
        List<String> activities = TOPIC_ACTIVITIES.getOrDefault(topic,
                List.of("Theory Review", "Practice Easy", "Practice Medium", "Practice Hard", "Mock Test", "Revision", "Assessment"));

        int daysToCreate = Math.min(7, activities.size());
        for (int day = 1; day <= daysToCreate; day++) {
            Roadmap entry = Roadmap.builder()
                    .user(user)
                    .weekNumber(weekNumber)
                    .dayNumber(day)
                    .topic(topic)
                    .activity(activities.get(day - 1))
                    .completed(false)
                    .build();
            roadmapRepository.save(entry);
        }
    }

    public RoadmapResponse getRoadmap(Long userId) {
        List<Roadmap> entries = roadmapRepository.findByUserIdOrderByWeekNumberAscDayNumberAsc(userId);

        Map<Integer, List<Roadmap>> byWeek = entries.stream()
                .collect(Collectors.groupingBy(Roadmap::getWeekNumber, LinkedHashMap::new, Collectors.toList()));

        List<RoadmapResponse.WeekPlan> weeks = new ArrayList<>();
        for (Map.Entry<Integer, List<Roadmap>> entry : byWeek.entrySet()) {
            List<RoadmapResponse.DayPlan> days = entry.getValue().stream()
                    .map(r -> RoadmapResponse.DayPlan.builder()
                            .dayNumber(r.getDayNumber())
                            .activity(r.getActivity())
                            .completed(r.getCompleted())
                            .build())
                    .collect(Collectors.toList());

            String topic = entry.getValue().get(0).getTopic();

            weeks.add(RoadmapResponse.WeekPlan.builder()
                    .weekNumber(entry.getKey())
                    .topic(topic)
                    .days(days)
                    .build());
        }

        return RoadmapResponse.builder().weeks(weeks).build();
    }
}
