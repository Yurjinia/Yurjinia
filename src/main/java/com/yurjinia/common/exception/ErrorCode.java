package com.yurjinia.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("Email already exists"),
    USERNAME_ALREADY_EXISTS("Username already exists"),
    EMAIL_NOT_EXISTS("Email not exists"),
    INVALID_EMAIL("Invalid email"),
    INVALID_USERNAME("Invalid username"),
    INVALID_PASSWORD("Invalid password"),
    PASSWORD_IS_NOT_MATCHED("Password is not matched"),
    PASSWORD_MATCHES_OLD_PASSWORD("Password matches old password"),
    INVALID_SIGN_UP_REQUEST("Invalid sign up request"),
    INVALID_LOGIN_REQUEST("Invalid login request"),
    USER_NAME_ALREADY_EXISTS("Username already exists"),
    USER_NOT_FOUND("User not found"),
    USERNAME_CANNOT_BE_EMPTY("Username cannot be empty"),
    JWT_INVALID("Invalid JWT token"),
    JWT_EXPIRED("JWT token expired"),
    PROJECT_ALREADY_EXISTS("Project already exists"),
    PROJECT_NOT_FOUND("Project not found"),
    USER_ALREADY_IN_PROJECT("User already in project"),
    TOKEN_NOT_FOUND("Token not found"),
    UPLOAD_FILE_ERROR("Failed to upload file"),
    FILE_TYPE_ERROR("File type error"),
    TOKEN_EXPIRED("Token expired"),
    DELETE_FILE_ERROR("Failed to delete file"),
    BAD_REQUEST("Bad request"),
    BUILD_EMAIL_ERROR("Failed to build email"),
    TEMPLATE_FILE_NOT_FOUND("Template file not found"),
    USER_ALREADY_EXISTS("User already exists"),
    USER_ALREADY_ACTIVE("User already active"),
    FORBIDDEN("Forbidden"),
    UNAUTHORIZED("Unauthorized"),
    OWNER_CANNOT_BE_REMOVED("Owner cannot be removed"),
    OWNER_CANNOT_BE_A_USER("Owner cannot be a user"),
    BOARD_ALREADY_EXISTS("Board already exists"),
    BOARD_NOT_FOUND("Board not found"),
    COLUMN_ALREADY_EXISTS("Column already exists"),
    COLUMN_NOT_FOUND("Column not found");


    //TODO: Add HttpStatus to the error code
    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

}
