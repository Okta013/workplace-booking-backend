--liquibase formatted sql
--changeset Dudochkin:20240829-02-create-table-roles

CREATE TABLE roles
(
    id        SERIAL PRIMARY KEY,
    name_role VARCHAR(50) NOT NULL
);