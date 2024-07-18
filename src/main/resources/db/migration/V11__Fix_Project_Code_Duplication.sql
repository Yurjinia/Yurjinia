-- Drop duplicate project_code column if it exists
ALTER TABLE project
    DROP COLUMN IF EXISTS project_code;

-- Ensure we have the correct project_code column
ALTER TABLE project
    ADD COLUMN IF NOT EXISTS project_code VARCHAR(100) UNIQUE NOT NULL;