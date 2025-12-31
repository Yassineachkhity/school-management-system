CREATE TABLE IF NOT EXISTS professeurs (
    teacherId VARCHAR(36) NOT NULL,
    userId VARCHAR(36) NOT NULL,
    employeeNumber VARCHAR(20) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    departementId VARCHAR(36) NOT NULL,
    level VARCHAR(50),
    status VARCHAR(20),
    hireDate DATE,
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (teacherId),
    UNIQUE KEY uk_teachers_userId (userId),
    UNIQUE KEY uk_teachers_employeeNumber (employeeNumber)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO professeurs (
    teacherId, userId, employeeNumber, firstName, lastName, email, phone, address,
    departementId, level, status, hireDate, createdAt, updatedAt
) VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
     'EMP-1001', 'Amina', 'Haddad', 'amina.haddad@school.com', '0612345678',
     'Campus A, Office 12', '11111111-1111-1111-1111-111111111111',
     'FULL_TIME_PROFESSOR', 'ACTIVE', '2020-09-01', '2024-01-05 09:15:00', '2024-01-05 09:15:00'),
    ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
     'EMP-1002', 'Youssef', 'Karim', 'youssef.karim@school.com', '0698765432',
     'Campus B, Office 8', '22222222-2222-2222-2222-222222222222',
     'ASSISTANT_PROFESSOR', 'ACTIVE', '2021-09-15', '2024-01-06 09:15:00', '2024-01-06 09:15:00');
