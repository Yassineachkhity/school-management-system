CREATE TABLE IF NOT EXISTS users (
    userId VARCHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    createdAt DATETIME,
    lastLoginAt DATETIME,
    PRIMARY KEY (userId),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
    roleId VARCHAR(36) NOT NULL,
    label VARCHAR(20) NOT NULL,
    PRIMARY KEY (roleId),
    UNIQUE KEY uk_roles_label (label)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    UNIQUE KEY uk_user_roles (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO roles (roleId, label) VALUES
    ('00000000-0000-0000-0000-000000000010', 'ADMIN'),
    ('00000000-0000-0000-0000-000000000020', 'TEACHER'),
    ('00000000-0000-0000-0000-000000000030', 'STUDENT');

INSERT INTO users (userId, email, password_hash, createdAt, lastLoginAt) VALUES
    ('00000000-0000-0000-0000-000000000001', 'admin@school.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoO.j6a8b8xY9n1pocOq94WQ8A8m5XnV2i',
     '2024-01-01 00:00:00', '2024-01-10 08:00:00'),
    ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'amina.haddad@school.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoO.j6a8b8xY9n1pocOq94WQ8A8m5XnV2i',
     '2024-01-05 09:00:00', NULL),
    ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'youssef.karim@school.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoO.j6a8b8xY9n1pocOq94WQ8A8m5XnV2i',
     '2024-01-06 09:00:00', NULL),
    ('ddddddd1-dddd-dddd-dddd-ddddddddddd1', 'sara.elamrani@student.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoO.j6a8b8xY9n1pocOq94WQ8A8m5XnV2i',
     '2024-01-07 09:00:00', NULL),
    ('ddddddd2-dddd-dddd-dddd-ddddddddddd2', 'omar.bennani@student.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoO.j6a8b8xY9n1pocOq94WQ8A8m5XnV2i',
     '2024-01-08 09:00:00', NULL);

INSERT INTO user_roles (user_id, role_id) VALUES
    ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000010'),
    ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '00000000-0000-0000-0000-000000000020'),
    ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', '00000000-0000-0000-0000-000000000020'),
    ('ddddddd1-dddd-dddd-dddd-ddddddddddd1', '00000000-0000-0000-0000-000000000030'),
    ('ddddddd2-dddd-dddd-dddd-ddddddddddd2', '00000000-0000-0000-0000-000000000030');
