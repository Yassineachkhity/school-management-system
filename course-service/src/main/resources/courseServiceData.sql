CREATE TABLE IF NOT EXISTS courses (
    courseId VARCHAR(36) NOT NULL,
    courseCode VARCHAR(20) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    departementId VARCHAR(36) NOT NULL,
    creditHours INT NOT NULL,
    semester INT NOT NULL,
    status VARCHAR(20),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (courseId),
    UNIQUE KEY uk_courses_courseCode (courseCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO courses (
    courseId, courseCode, title, description, departementId,
    creditHours, semester, status, createdAt, updatedAt
) VALUES
    ('eeeeeee1-eeee-eeee-eeee-eeeeeeeeeee1', 'CS101', 'Intro to Programming',
     'Basics of programming and problem solving.', '11111111-1111-1111-1111-111111111111',
     4, 1, 'ACTIVE', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
    ('eeeeeee2-eeee-eeee-eeee-eeeeeeeeeee2', 'MATH101', 'Calculus I',
     'Differential calculus fundamentals.', '22222222-2222-2222-2222-222222222222',
     3, 1, 'ACTIVE', '2024-01-10 10:10:00', '2024-01-10 10:10:00');
