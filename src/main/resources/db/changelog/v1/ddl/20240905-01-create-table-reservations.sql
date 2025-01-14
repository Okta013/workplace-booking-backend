--liquibase formatted sql
--changeset Anikeeva:20240905-01-create-table-reservations
create table reservations(
    id uuid,
    booking_date timestamp not null,
    booking_start timestamp not null,
    booking_end timestamp not null,
    user_id uuid not null,
    workplace_id uuid not null,
    is_confirmed boolean default false,
    cancellation_date timestamp default null,
    cancellation_comment varchar default null,
    primary key (id)
)