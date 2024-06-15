package com.yurjinia.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("Email already exists"),
    EMAIL_NOT_EXISTS("Email not exists"),
    INVALID_EMAIL("Invalid email"),
    INVALID_PASSWORD("Invalid password"),
    USER_NAME_ALREADY_EXISTS("Username already exists"),
    USER_NOT_FOUND("User not found"),
    JWT_INVALID("Invalid JWT token"),
    JWT_EXPIRED("JWT token expired"),
    PROJECT_ALREADY_EXISTS("Project already exists"),
    PROJECT_NOT_FOUND("Project not found"),
    TOKEN_NOT_FOUND("Token not found");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

}
