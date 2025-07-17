package com.senolkacar.sqltrainer.validation;

import com.senolkacar.sqltrainer.entity.Question;
import com.senolkacar.sqltrainer.repository.QuestionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueOrderValidator implements ConstraintValidator<UniqueOrder, Question> {
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public boolean isValid(Question question, ConstraintValidatorContext context) {
        if (question == null || question.getOrder() == null || question.getQuizId() == null) {
            return true;
        }
        // Check if another question in the same quiz has the same order, excluding itself
        return !questionRepository.existsByQuizIdAndOrderAndIdNot(
            question.getQuizId(), question.getOrder(), question.getId() == null ? -1 : question.getId()
        );
    }
}

