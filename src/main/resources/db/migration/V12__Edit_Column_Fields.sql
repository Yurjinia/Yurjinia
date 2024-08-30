CREATE UNIQUE INDEX unique_name_board ON "column" (name, board_id);

ALTER TABLE "column"
    ADD COLUMN column_position BIGSERIAL NOT NULL;
ALTER TABLE "column"
    DROP COLUMN "status";