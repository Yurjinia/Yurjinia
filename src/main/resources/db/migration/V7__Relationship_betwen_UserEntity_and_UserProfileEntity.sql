CREATE TABLE IF NOT EXISTS "user_profile"
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    avatar_id  VARCHAR(255) NULL,
    username   VARCHAR(64)  NULL
);

ALTER TABLE "user"
    DROP COLUMN "first_name";
ALTER TABLE "user"
    DROP COLUMN "last_name";
ALTER TABLE "user"
    DROP COLUMN "avatar_id";
ALTER TABLE "user"
    DROP COLUMN "username";

ALTER TABLE "user"
    ADD COLUMN profile_id BIGINT;

ALTER TABLE "user"
    ADD CONSTRAINT fk_profile
        FOREIGN KEY (profile_id)
            REFERENCES user_profile (id);