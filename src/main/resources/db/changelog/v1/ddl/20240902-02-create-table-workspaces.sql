--liquibase formatted sql
--changeset Anikeeva:20240830-02-create-table-workspaces

create table workspaces(
    id UUID,
    name varchar(30) not null,
    floor_number smallint not null,
    room_number smallint,
    is_deleted boolean default false,
    office_id UUID not null,
    primary key(id)
)