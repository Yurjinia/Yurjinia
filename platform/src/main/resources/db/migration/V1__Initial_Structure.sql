-- V1__Initial_Structure.sql

-- User table
CREATE TABLE IF NOT EXISTS "user"
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100)        NOT NULL,
    last_name  VARCHAR(100)        NOT NULL,
    avatar_id  VARCHAR(255)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(50)         NOT NULL
);

-- Project table
CREATE TABLE IF NOT EXISTS project
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Board table
CREATE TABLE IF NOT EXISTS board
(
    id         BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES project (id) ON DELETE CASCADE
);

-- Column table
CREATE TABLE IF NOT EXISTS "column"
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    board_id BIGINT REFERENCES board (id) ON DELETE CASCADE
);

-- Task table
CREATE TABLE IF NOT EXISTS task
(
    id        BIGSERIAL PRIMARY KEY,
    column_id BIGINT REFERENCES "column" (id) ON DELETE CASCADE
);

-- Join table for User and Project
CREATE TABLE IF NOT EXISTS project_user
(
    user_id    BIGINT REFERENCES "user" (id) ON DELETE CASCADE,
    project_id BIGINT REFERENCES project (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, project_id)
);

-- Join table for User and Board
CREATE TABLE IF NOT EXISTS board_user
(
    user_id  BIGINT REFERENCES "user" (id) ON DELETE CASCADE,
    board_id BIGINT REFERENCES board (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, board_id)
);
