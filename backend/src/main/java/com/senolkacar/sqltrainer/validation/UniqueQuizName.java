package com.senolkacar.sqltrainer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueQuizNameValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueQuizName {
    String message() default "Quiz name must be unique.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
