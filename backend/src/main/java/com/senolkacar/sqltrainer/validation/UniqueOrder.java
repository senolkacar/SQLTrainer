package com.senolkacar.sqltrainer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueOrderValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueOrder {
    String message() default "Order must be unique within the quiz.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

