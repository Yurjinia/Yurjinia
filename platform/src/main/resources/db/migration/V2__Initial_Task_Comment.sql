-- Task status table
CREATE TABLE IF NOT EXISTS task_status
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Task table
CREATE TABLE IF NOT EXISTS task
(
    id          BIGSERIAL PRIMARY KEY,
    task_code   VARCHAR(255) UNIQUE NOT NULL,
    name        VARCHAR(255) NOT NULL,
    status_id   BIGINT NOT NULL,
    description TEXT NOT NULL,
    start_date  DATE,
    end_date    DATE,
    assignee_id BIGINT,
    reporter_id BIGINT,
    priority    VARCHAR(255),
    created     DATE NOT NULL,
    updated     DATE NOT NULL,
    column_id   BIGINT NOT NULL,
    board_id    BIGINT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES task_status (id),
    FOREIGN KEY (assignee_id) REFERENCES "user" (id),
    FOREIGN KEY (reporter_id) REFERENCES "user" (id),
    FOREIGN KEY (column_id) REFERENCES "column" (id),
    FOREIGN KEY (board_id) REFERENCES "board" (id)
);

-- Comment table
CREATE TABLE IF NOT EXISTS comment
(
    id        BIGSERIAL PRIMARY KEY,
    task_id   BIGINT,
    text      TEXT,
    created   TIMESTAMP,
    updated   TIMESTAMP,
    author_id BIGINT,
    FOREIGN KEY (task_id) REFERENCES task (id),
    FOREIGN KEY (author_id) REFERENCES "user" (id)
);
