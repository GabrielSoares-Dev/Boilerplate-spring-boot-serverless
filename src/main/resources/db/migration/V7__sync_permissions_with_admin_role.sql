INSERT INTO role_has_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.name = 'ADMIN'
WHERE p.name IN (
    'CREATE_PERMISSION',
    'READ_ALL_PERMISSIONS',
    'DELETE_PERMISSION',
    'READ_PERMISSION',
    'UPDATE_PERMISSION',
    'CREATE_ROLE',
    'READ_ALL_ROLES',
    'DELETE_ROLE',
    'READ_ROLE',
    'UPDATE_ROLE',
    'SYNC_ROLE_WITH_PERMISSIONS'
);