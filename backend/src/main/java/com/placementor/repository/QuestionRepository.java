package com.placementor.repository;

import com.placementor.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopic(String topic);
    List<Question> findByDifficulty(Question.Difficulty difficulty);
    List<Question> findByTopicAndDifficulty(String topic, Question.Difficulty difficulty);

    @Query("SELECT DISTINCT q.topic FROM Question q")
    List<String> findDistinctTopics();

    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestions(int limit);

    @Query(value = "SELECT * FROM questions WHERE topic = :topic AND difficulty = :difficulty ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomByTopicAndDifficulty(String topic, String difficulty, int limit);
}
