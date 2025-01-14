--liquibase formatted sql
--changeset Dudochkin:20240829-02-create-table-users_roles

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles (id, name_role)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO roles (id, name_role)
VALUES (2, 'ROLE_USER');

INSERT INTO users (id, full_name, phone_number, email, password, is_deleted, password_needs_change)
VALUES (uuid_generate_v4(),
        'Admin',
        '79278415725',
        'admin@mail.ru',
        '$2a$10$MbYjjhFDzzbx7hZhEj60PeJ2521NjRvmWu9.RmL2ZjnKZ3SBIyJ.6',
        false,
        false);

INSERT INTO users_roles (user_entity_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.email = 'admin@mail.ru'
  AND r.name_role = 'ROLE_ADMIN';