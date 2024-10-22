package com.yurjinia.platform.common.application.constants;

public class RequestMatchersConstants {

    public static final String AUTH_URL = "/api/v1/auth/**";
    public static final String[] SWAGGER_WHITELIST = {
            "swagger-ui/**",
            "v3/api-docs/**",
            "swagger-resources/**",
            "swagger-resources/",
    };

}
