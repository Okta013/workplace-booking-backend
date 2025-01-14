--liquibase formatted sql
--changeset Anikeeva:20240830-03-create-table-workplaces

create table workplaces(
    id UUID,
    number integer not null,
    description varchar,
    workspace_id UUID not null,
    is_deleted boolean default false,
    primary key(id)
)