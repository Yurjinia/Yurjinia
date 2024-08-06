package com.yurjinia.project_structure.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpperCaseValidator implements ConstraintValidator<UpperCase, String> {

    private static final String UPPER_CASE_PATTERN = "^[A-Z]+$";

    @Override
    public void initialize(UpperCase constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(UPPER_CASE_PATTERN);
    }

}

