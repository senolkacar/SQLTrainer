package com.senolkacar.sqltrainer.validation;

import com.senolkacar.sqltrainer.entity.Quiz;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Quiz> {

    @Override
    public boolean isValid(Quiz quiz, ConstraintValidatorContext context) {
        if (quiz == null) {
            return true;
        }

        // Only validate date range when quiz is a test and both dates are present
        if (quiz.isTest() && quiz.getStartDate() != null && quiz.getEndDate() != null) {
            if (!quiz.getStartDate().isBefore(quiz.getEndDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("The start date must be before the end date.")
                       .addPropertyNode("startDate")
                       .addConstraintViolation();
                context.buildConstraintViolationWithTemplate("The end date must be after the start date.")
                       .addPropertyNode("endDate")
                       .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
