package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query("""
            SELECT\s
                q.*,\s
                d.*,\s
                a.*
            FROM\s
                Quizzes q
            LEFT JOIN\s
                Database d ON q.DatabaseId = d.Id
            LEFT JOIN\s
                Attempts a ON q.Id = a.QuizId""")
    List<Quiz> findAllWithDatabaseAndAttempts();

    @Query("""
            SELECT q.*, d.*, a.*
            FROM Quizzes q
            LEFT JOIN Database d ON q.DatabaseId = d.Id
            LEFT JOIN Attempts a ON q.Id = a.QuizId
            """)
    List<Quiz> findAllWithDatabaseAndAttemptsForQuizzes();
    @Query("""
            SELECT q.*, a.*
            FROM Quizzes q
            LEFT JOIN Attempts a ON q.Id = a.QuizId
            WHERE q.Id = @Id""")
    Quiz findByIdWithAttempts(int Id);

    @Query("""
            SELECT q FROM Quiz q
            WHERE q.name = :name
            """)
    boolean existsByName(String name);

    @Query("""
            SELECT q FROM Quiz q
            WHERE q.name = :name AND q.id <> :id
            """)
    boolean existsByNameAndIdNot(String name, Long id);
}
