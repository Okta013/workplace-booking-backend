--liquibase formatted sql
--changeset Anikeeva:20240830-01-create-table-offices

create table offices(
    id UUID,
    address varchar not null,
    name varchar(30) not null,
    is_deleted boolean default false,
    primary key (id)
)