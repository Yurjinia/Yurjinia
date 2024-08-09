ALTER TABLE board
    ADD COLUMN code varchar(255) not null;
ALTER TABLE board
    ADD COLUMN name varchar(255) not null;

CREATE UNIQUE INDEX unique_code_project ON board(code, project_id);

DROP TABLE IF EXISTS board_user;