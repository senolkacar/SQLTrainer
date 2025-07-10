package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    @Query("SELECT a FROM Attempt a " +
           "WHERE a.student.id = :studentId AND a.quiz.id = :quizId " +
           "ORDER BY a.id DESC")
    Optional<Attempt> findLatestByStudentAndQuiz(@Param("studentId") Long studentId, @Param("quizId") Long quizId);
}
