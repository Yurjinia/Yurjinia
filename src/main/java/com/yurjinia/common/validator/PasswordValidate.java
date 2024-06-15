package com.yurjinia.common.validator;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidate.TelConstraintValidator.class)
public @interface PasswordValidate {
    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class TelConstraintValidator implements ConstraintValidator<PasswordValidate, String> {

        @Override
        public boolean isValid(String password,
                               ConstraintValidatorContext constraintValidatorContext) {

            if (StringUtils.isEmpty(password)) {
                throw new CommonException(ErrorCode.INVALID_PASSWORD, List.of("Password cannot be empty"));
            }

            String check = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(password);
            return matcher.matches();
        }
    }

}
