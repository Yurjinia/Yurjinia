-- V4__Initial_Confirmation_Token_table.sql

CREATE TABLE confirmation_token
(
    id           BIGSERIAL PRIMARY KEY,
    token        VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    expires_at   TIMESTAMP    NOT NULL,
    user_email   VARCHAR(255) NOT NULL,
    project_name VARCHAR(255) NOT NULL
);