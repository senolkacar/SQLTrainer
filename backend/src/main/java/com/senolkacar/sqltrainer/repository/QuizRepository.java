package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Additional query methods can be defined here if needed
}
