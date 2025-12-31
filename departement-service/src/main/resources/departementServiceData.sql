CREATE TABLE IF NOT EXISTS departements (
    departementId VARCHAR(36) NOT NULL,
    departementCode VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    headTeacherId VARCHAR(36),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (departementId),
    UNIQUE KEY uk_departements_code (departementCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO departements (
    departementId, departementCode, name, description, headTeacherId, createdAt, updatedAt
) VALUES
    ('11111111-1111-1111-1111-111111111111', 'CS', 'Computer Science',
     'Computer Science Department', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
     '2024-01-02 08:00:00', '2024-01-02 08:00:00'),
    ('22222222-2222-2222-2222-222222222222', 'MATH', 'Mathematics',
     'Mathematics Department', 'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
     '2024-01-02 08:05:00', '2024-01-02 08:05:00');
