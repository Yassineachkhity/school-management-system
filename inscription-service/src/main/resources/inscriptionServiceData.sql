CREATE TABLE IF NOT EXISTS inscriptions (
    enrollmentId VARCHAR(36) NOT NULL,
    studentId VARCHAR(36) NOT NULL,
    sectionId VARCHAR(36) NOT NULL,
    enrolledAt DATETIME NOT NULL,
    grade DOUBLE,
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (enrollmentId),
    UNIQUE KEY uk_inscriptions_student_section (studentId, sectionId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO inscriptions (
    enrollmentId, studentId, sectionId, enrolledAt, grade, createdAt, updatedAt
) VALUES
    ('99999991-9999-9999-9999-999999999991', 'ccccccc1-cccc-cccc-cccc-ccccccccccc1',
     'fffffff1-ffff-ffff-ffff-fffffffffff1', '2024-09-10 10:00:00', 16.5,
     '2024-09-10 10:00:00', '2024-09-10 10:00:00'),
    ('99999992-9999-9999-9999-999999999992', 'ccccccc2-cccc-cccc-cccc-ccccccccccc2',
     'fffffff2-ffff-ffff-ffff-fffffffffff2', '2024-09-10 10:05:00', 14.0,
     '2024-09-10 10:05:00', '2024-09-10 10:05:00');
