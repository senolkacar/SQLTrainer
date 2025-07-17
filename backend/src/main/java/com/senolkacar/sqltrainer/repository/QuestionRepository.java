package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT q FROM Question q " +
           "JOIN FETCH q.quiz quiz " +
           "JOIN FETCH quiz.attempts " +
           "JOIN FETCH quiz.database " +
           "LEFT JOIN FETCH q.answers " +
           "LEFT JOIN FETCH q.solutions " +
           "WHERE q.id = :id " +
           "ORDER BY q.order")
    Optional<Question> findByIdWithDetails(@Param("id") Integer id);

    @Query("SELECT q.id FROM Question q " +
           "WHERE q.quiz.id = :quizId AND q.order < :order " +
           "ORDER BY q.order DESC")
    Optional<Integer> findPreviousQuestionId(@Param("quizId") Long quizId, @Param("order") Integer order);

    @Query("SELECT q.id FROM Question q " +
           "WHERE q.quiz.id = :quizId AND q.order > :order " +
           "ORDER BY q.order ASC")
    Optional<Integer> findNextQuestionId(@Param("quizId") Long quizId, @Param("order") Integer order);

    @Query("SELECT q FROM Question q " +
            "JOIN FETCH q.quiz quiz " +
            "JOIN FETCH quiz.database " +
            "LEFT JOIN FETCH q.solutions " +
            "WHERE q.id = :id")
    Optional<Question> findByIdWithQuizAndSolutions(@Param("id") Integer id);

    @Query("DELETE FROM Question q WHERE q.id = :id")
    void deleteById(@Param("id") Integer id);

    @Query("SELECT TOP 1 *\n" +
            "FROM Questions\n" +
            "WHERE QuizId = @id")
    Question findFirstByQuizId(@Param("id") Long quizId);

    List<Question> findAllByQuizId(Long quizId);

    boolean existsByQuizIdAndOrderAndIdNot(Integer quizId, Integer order, Integer id);
}
