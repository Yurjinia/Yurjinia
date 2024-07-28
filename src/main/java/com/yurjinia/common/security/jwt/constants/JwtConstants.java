package com.yurjinia.common.security.jwt.constants;

import org.springframework.beans.factory.annotation.Value;

public class JwtConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String USERNAME_KEY = "sub";

    @Value("${APP.AUTH.URL}")
    public static String AUTH_URL;

}
