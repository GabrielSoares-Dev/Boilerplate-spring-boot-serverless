INSERT INTO users (name, email, phone_number, password, role_id, created_at, updated_at)
VALUES (
    'admin',
    'admin@gmail.com',
    '11942421224',
    '$2a$10$RMKQ0fJbQJFtd4z4aVIgQ.Fiq/X0eBRhJCT8oociPzxdnVequE58q',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);