ALTER TABLE "task" RENAME TO "ticket";
ALTER TABLE "ticket" ALTER COLUMN "created" TYPE TIMESTAMP;
ALTER TABLE "ticket" ALTER COLUMN "updated" TYPE TIMESTAMP;
ALTER TABLE "ticket" RENAME COLUMN "name" TO "title";
ALTER TABLE "board" ADD COLUMN unique_ticket_code SERIAL UNIQUE;
ALTER TABLE "ticket" ADD COLUMN position BIGSERIAL NOT NULL;
