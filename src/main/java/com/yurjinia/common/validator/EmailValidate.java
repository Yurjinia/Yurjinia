package com.yurjinia.common.validator;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidate.TelConstraintValidator.class)
public @interface EmailValidate {

    String message() default "Invalid email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class TelConstraintValidator implements ConstraintValidator<EmailValidate, String> {

        @Override
        public boolean isValid(String email,
                               ConstraintValidatorContext constraintValidatorContext) {

            if (StringUtils.isEmpty(email)) {
                throw new CommonException(ErrorCode.INVALID_EMAIL, HttpStatus.BAD_REQUEST, List.of("Email address cannot be empty"));
            }

            String check = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        }
    }

}
