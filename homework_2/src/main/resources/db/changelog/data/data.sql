--liquibase formatted sql

--changeset waalkman:2
INSERT INTO acl_sid (id, principal, sid)
VALUES
       (1, 1, 'admin'),
       (2, 1, 'viewer'),
       (3, 1, 'manager');

INSERT INTO acl_class (id, class)
VALUES
       (1, 'com.study.spring.library.domain.Author'),
       (2, 'com.study.spring.library.domain.Book'),
       (3, 'com.study.spring.library.domain.Comment'),
       (4, 'com.study.spring.library.domain.Genre'),
       (5, 'com.study.spring.library.domain.User');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES
       (1, 1, '0000a0000b0000c0000d0000', NULL, 1, 0),
       (2, 1, '0000a0000b0000c0000d0001', NULL, 1, 0),
       (3, 1, '0000a0000b0000c0000d0002', NULL, 1, 0),
       (4, 1, '0000a0000b0000c0000d0003', NULL, 1, 0),
       (5, 1, '0000a0000b0000c0000d0004', NULL, 1, 0),

       (6, 4, '0000a0000b0000c0000d0005', NULL, 1, 0),
       (7, 4, '0000a0000b0000c0000d0006', NULL, 1, 0),
       (8, 4, '0000a0000b0000c0000d0007', NULL, 1, 0),
       (9, 4, '0000a0000b0000c0000d0008', NULL, 1, 0),

       (10, 2, '0000a0000b0000c0000d0008', NULL, 1, 0),
       (11, 2, '0000a0000b0000c0000d0010', NULL, 1, 0),

       (12, 3, '0000a0000b0000c0000d0011', NULL, 1, 0),
       (13, 3, '0000a0000b0000c0000d0012', NULL, 1, 0),

       (14, 5, '0000a0000b0000c0000d0013', NULL, 1, 0),
       (15, 5, '0000a0000b0000c0000d0014', NULL, 1, 0),
       (16, 5, '0000a0000b0000c0000d0015', NULL, 1, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure)
VALUES
       (1, 1, 1, 1, 16, 1, 1, 1),
       (2, 2, 1, 1, 16, 1, 1, 1),
       (3, 3, 1, 1, 16, 1, 1, 1),
       (4, 4, 1, 1, 16, 1, 1, 1),
       (5, 5, 1, 1, 16, 1, 1, 1),
       (6, 6, 1, 1, 16, 1, 1, 1),
       (7, 7, 1, 1, 16, 1, 1, 1),
       (8, 8, 1, 1, 16, 1, 1, 1),
       (9, 9, 1, 1, 16, 1, 1, 1),
       (10, 10, 1, 1, 16, 1, 1, 1),
       (11, 11, 1, 1, 16, 1, 1, 1),
       (12, 12, 1, 1, 16, 1, 1, 1),
       (13, 13, 1, 1, 16, 1, 1, 1),
       (14, 14, 1, 1, 16, 1, 1, 1),
       (15, 15, 1, 1, 16, 1, 1, 1),
       (16, 16, 1, 1, 16, 1, 1, 1);