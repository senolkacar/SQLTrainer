package com.senolkacar.sqltrainer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Start date must be before end date when the quiz is a test.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
