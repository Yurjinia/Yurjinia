ALTER TABLE "task" RENAME TO "ticket";
ALTER TABLE "ticket" ALTER COLUMN "created" TYPE TIMESTAMP;
ALTER TABLE "ticket" ALTER COLUMN "updated" TYPE TIMESTAMP;
ALTER TABLE "ticket" RENAME COLUMN "name" TO "title";
ALTER TABLE "board" ADD COLUMN uniq_ticket_code SERIAL;
ALTER TABLE "ticket" ADD COLUMN position BIGSERIAL NOT NULL;
/*ALTER TABLE task_status RENAME TO ticket_status;
*//*ALTER TABLE "column" RENAME COLUMN tasks TO tickets;
*/



