INSERT INTO users (name, email, phone_number, password, role_id, created_at, updated_at)
VALUES (
    'John Doe',
    'john.doe@example.com',
    '12345678901',
    '$2a$10$OpZ1FZhv2yKES9WienOmsO5lus79.4C5uQdcBOxObS0dkNem71PXG',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);