ALTER TABLE project
    ADD COLUMN owner_id BIGINT REFERENCES "user" (id) not null;

ALTER TABLE project
    ADD COLUMN code varchar(255) not null unique;
