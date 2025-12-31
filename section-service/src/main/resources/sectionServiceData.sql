CREATE TABLE IF NOT EXISTS sections (
    sectionId VARCHAR(36) NOT NULL,
    courseId VARCHAR(36) NOT NULL,
    teacherId VARCHAR(36) NOT NULL,
    academicYear VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,
    room VARCHAR(50) NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (sectionId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO sections (
    sectionId, courseId, teacherId, academicYear, capacity, room, createdAt, updatedAt
) VALUES
    ('fffffff1-ffff-ffff-ffff-fffffffffff1', 'eeeeeee1-eeee-eeee-eeee-eeeeeeeeeee1',
     'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '2024-2025', 35, 'A101',
     '2024-01-12 11:00:00', '2024-01-12 11:00:00'),
    ('fffffff2-ffff-ffff-ffff-fffffffffff2', 'eeeeeee2-eeee-eeee-eeee-eeeeeeeeeee2',
     'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '2024-2025', 40, 'B202',
     '2024-01-12 11:05:00', '2024-01-12 11:05:00');
