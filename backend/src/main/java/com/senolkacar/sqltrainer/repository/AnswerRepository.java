package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a " +
           "WHERE a.attempt.id = :attemptId AND a.question.id = :questionId " +
           "ORDER BY a.id DESC")
    Optional<Answer> findLatestByAttemptAndQuestion(@Param("attemptId") Long attemptId, @Param("questionId") Integer questionId);

    boolean existsByAttemptIdAndQuestionId(Long attemptId, Integer questionId);
}
