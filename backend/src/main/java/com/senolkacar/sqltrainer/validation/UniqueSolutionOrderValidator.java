package com.senolkacar.sqltrainer.validation;

import com.senolkacar.sqltrainer.entity.Solution;
import com.senolkacar.sqltrainer.repository.SolutionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueSolutionOrderValidator implements ConstraintValidator<UniqueSolutionOrder, Solution> {
    @Autowired
    private SolutionRepository solutionRepository;

    @Override
    public boolean isValid(Solution solution, ConstraintValidatorContext context) {
        if (solution == null || solution.getOrder() == null || solution.getQuestionId() == null) {
            return true;
        }

        // Check if another solution in the same question has the same order, excluding itself
        return !solutionRepository.existsByQuestionIdAndOrderAndIdNot(
            solution.getQuestionId(), solution.getOrder(), solution.getId() == null ? -1L : solution.getId()
        );
    }
}
