-- Queries to add columns to the task and column tables
ALTER TABLE "column"
    ADD COLUMN status VARCHAR(255);
ALTER TABLE "column"
    ADD COLUMN created TIMESTAMP;
ALTER TABLE "column"
    ADD COLUMN updated TIMESTAMP;

ALTER TABLE task
    ADD COLUMN code VARCHAR(255);
ALTER TABLE task
    ADD COLUMN created DATE;
ALTER TABLE task
    ADD COLUMN description TEXT;
ALTER TABLE task
    ADD COLUMN end_date DATE;
ALTER TABLE task
    ADD COLUMN name VARCHAR(255);
ALTER TABLE task
    ADD COLUMN priority VARCHAR(255);
ALTER TABLE task
    ADD COLUMN start_date DATE;
ALTER TABLE task
    ADD COLUMN type VARCHAR(255);
ALTER TABLE task
    ADD COLUMN updated DATE;
ALTER TABLE task
    ADD COLUMN assignee_id BIGINT;
ALTER TABLE task
    ADD COLUMN board_id BIGINT;
ALTER TABLE task
    ADD COLUMN reporter_id BIGINT;
ALTER TABLE task
    ADD COLUMN status_id BIGINT;
