--liquibase formatted sql
--changeset Dudochkin:20240829-02-create-table-users_roles

CREATE TABLE users_roles
(
    user_entity_id UUID,
    role_id        INTEGER,
    PRIMARY KEY (user_entity_id, role_id),
    FOREIGN KEY (user_entity_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);