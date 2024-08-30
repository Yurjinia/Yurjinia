package com.yurjinia.project_structure.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = UpperCaseValidator.class)
public @interface UpperCase {
    String message() default "Field must contain only uppercase letters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

