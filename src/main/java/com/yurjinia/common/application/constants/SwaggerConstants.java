package com.yurjinia.common.application.constants;

public class SwaggerConstants {
    public static final String PUBLIC_API = "public_api";
    public static final String PRIVATE_API = "private_api";
    public static final String PUBLIC_URL = "/api/v1/auth/**";
    public static final String[] PRIVATE_URL = new String[]{"/api/v1/users/**", "/api/v1/projects/**"};
}
