--liquibase formatted sql

--changeset waalkman:1
CREATE TABLE IF NOT EXISTS acl_sid (
    id serial NOT NULL primary key,
    principal boolean NOT NULL,
    sid varchar(100) NOT NULL,
    UNIQUE (sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
    id serial NOT NULL primary key,
    class varchar(255) NOT NULL,
    UNIQUE (class)
);

CREATE TABLE IF NOT EXISTS acl_entry (
    id serial NOT NULL primary key,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting boolean NOT NULL,
    audit_success boolean NOT NULL,
    audit_failure boolean NOT NULL,
    UNIQUE (acl_object_identity,ace_order)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
    id serial NOT NULL primary key,
    object_id_class bigint NOT NULL,
    object_id_identity varchar(100) NOT NULL,
    parent_object bigint DEFAULT NULL,
    owner_sid bigint DEFAULT NULL,
    entries_inheriting boolean NOT NULL,
    UNIQUE (object_id_class,object_id_identity)
);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

ALTER SEQUENCE acl_sid_id_seq RESTART WITH 5;
ALTER SEQUENCE acl_class_id_seq RESTART WITH 6;
ALTER SEQUENCE acl_entry_id_seq RESTART WITH 17;
ALTER SEQUENCE acl_object_identity_id_seq RESTART WITH 17;