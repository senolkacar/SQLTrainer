package com.senolkacar.sqltrainer.validation;

import com.senolkacar.sqltrainer.entity.Quiz;
import com.senolkacar.sqltrainer.repository.QuizRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueQuizNameValidator implements ConstraintValidator<UniqueQuizName, Quiz> {
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public boolean isValid(Quiz quiz, ConstraintValidatorContext context) {
        if (quiz == null || quiz.getName() == null || quiz.getName().trim().isEmpty()) {
            return true; // Let @NotEmpty handle empty names
        }

        // Check if another quiz has the same name, excluding itself
        return !quizRepository.existsByNameAndIdNot(
            quiz.getName(), quiz.getId() == null ? -1L : quiz.getId()
        );
    }
}
