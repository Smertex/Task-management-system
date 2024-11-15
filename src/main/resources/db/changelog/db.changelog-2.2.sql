--liquibase formatted sql

--changeset smertex:1
INSERT INTO comment(task_id, content, created_by)
VALUES ('a9099b32-e5b2-41aa-9ab6-d4d461549c70', 'This is test content 1', 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0');

--changeset smertex:2
INSERT INTO comment(task_id, content, created_by)
VALUES ('5f0288e7-301b-416b-af58-dd433667a607', 'This is test content 2', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');

--changeset smertex:3
INSERT INTO comment(task_id, content, created_by)
VALUES ('5f0288e7-301b-416b-af58-dd433667a607', 'This is test content 3', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');
