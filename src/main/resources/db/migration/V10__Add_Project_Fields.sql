-- Add projectOwnerEmail and add owner_id column to project table
ALTER TABLE project
    ADD COLUMN project_owner_email VARCHAR(100) NOT NULL,
    ADD COLUMN owner_id BIGINT;