package com.yurjinia.common.exception;

public enum ErrorCode {
    EMAIL_ALREADY_EXISTS,
    USERNAME_ALREADY_EXISTS,
    EMAIL_NOT_EXISTS,
    INVALID_EMAIL,
    INVALID_USERNAME,
    INVALID_PASSWORD,
    PASSWORD_IS_NOT_MATCHED,
    PASSWORD_MATCHES_OLD_PASSWORD,
    INVALID_SIGN_UP_REQUEST,
    INVALID_LOGIN_REQUEST,
    USER_NAME_ALREADY_EXISTS,
    USER_NOT_FOUND,
    USERNAME_CANNOT_BE_EMPTY,
    JWT_INVALID,
    JWT_EXPIRED,
    PROJECT_ALREADY_EXISTS,
    PROJECT_NOT_FOUND,
    USER_ALREADY_IN_PROJECT,
    TOKEN_NOT_FOUND,
    UPLOAD_FILE_ERROR,
    FILE_TYPE_ERROR,
    TOKEN_EXPIRED,
    DELETE_FILE_ERROR,
    BAD_REQUEST,
    BUILD_EMAIL_ERROR,
    TEMPLATE_FILE_NOT_FOUND,
    USER_ALREADY_EXISTS,
    USER_ALREADY_ACTIVE,
    FORBIDDEN,
    UNAUTHORIZED,
    OWNER_CANNOT_BE_REMOVED,
    OWNER_CANNOT_BE_A_USER,
    BOARD_ALREADY_EXISTS,
    BOARD_NOT_FOUND,
    COLUMN_ALREADY_EXISTS,
    COLUMN_NOT_FOUND,
    TICKET_NOT_FOUND,
    COMMENT_NOT_FOUND,
    USER_IS_NOT_AUTHOR;
}
