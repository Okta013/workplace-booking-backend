--liquibase formatted sql
--changeset Anikeeva:20240827-01-create-table-users

create table users(
                            id uuid,
                            full_name varchar not null,
                            phone_number varchar(12) not null unique,
                            email varchar not null unique,
                            password varchar not null,
                            available_minutes int not null default 2400,
                            is_deleted boolean default false,
                            password_needs_change boolean default true,
                            primary key(id)
)