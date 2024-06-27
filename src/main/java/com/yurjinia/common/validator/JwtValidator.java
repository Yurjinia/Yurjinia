package com.yurjinia.common.validator;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class JwtValidator {

    private static final Pattern JWT_PATTERN;

    static {
        final String JWT_PATTERN_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
        JWT_PATTERN = Pattern.compile(JWT_PATTERN_REGEX);
    }

    public static boolean isValidateFormat(String token) {
        return JWT_PATTERN.matcher(token).matches();
    }
}
