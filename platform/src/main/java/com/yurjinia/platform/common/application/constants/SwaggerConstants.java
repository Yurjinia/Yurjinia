package com.yurjinia.platform.common.application.constants;

public class SwaggerConstants {
    public static final String SWAGGER_PUBLIC_API_GROUP = "public_api";
    public static final String SWAGGER_PRIVATE_API_GROUP = "private_api";
    public static final String SWAGGER_PUBLIC_URL_PATH_MATCHERS = "/api/v1/auth/**";
    public static final String[] SWAGGER_PRIVATE_URL_PATH_MATCHERS = new String[]{"/api/v1/users/**", "/api/v1/projects/**"};
}
