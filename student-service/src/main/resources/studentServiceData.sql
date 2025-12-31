CREATE TABLE IF NOT EXISTS students (
    studentId VARCHAR(36) NOT NULL,
    userId VARCHAR(36) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    apogeCode VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    admissionDate DATE NOT NULL,
    gradeLevel INT NOT NULL,
    dateOfBirth DATE NOT NULL,
    departementId VARCHAR(36) NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (studentId),
    UNIQUE KEY uk_students_userId (userId),
    UNIQUE KEY uk_students_apogeCode (apogeCode),
    CHECK (gradeLevel BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO students (
    studentId, userId, firstName, lastName, apogeCode, email, phone,
    admissionDate, gradeLevel, dateOfBirth, departementId, createdAt, updatedAt
) VALUES
    ('ccccccc1-cccc-cccc-cccc-ccccccccccc1', 'ddddddd1-dddd-dddd-dddd-ddddddddddd1',
     'Sara', 'El Amrani', 'APG001', 'sara.elamrani@student.com', '0611111111',
     '2023-09-15', 2, '2005-06-20', '11111111-1111-1111-1111-111111111111',
     '2024-01-07 09:10:00', '2024-01-07 09:10:00'),
    ('ccccccc2-cccc-cccc-cccc-ccccccccccc2', 'ddddddd2-dddd-dddd-dddd-ddddddddddd2',
     'Omar', 'Bennani', 'APG002', 'omar.bennani@student.com', '0622222222',
     '2022-09-15', 3, '2004-03-12', '22222222-2222-2222-2222-222222222222',
     '2024-01-08 09:10:00', '2024-01-08 09:10:00');
