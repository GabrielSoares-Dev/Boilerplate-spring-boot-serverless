INSERT INTO permissions (name, description, created_at, updated_at) 
VALUES
('CREATE_PERMISSION','create permission', CURRENT_TIMESTAMP, null),
('READ_ALL_PERMISSIONS','read all permissions', CURRENT_TIMESTAMP, null),
('DELETE_PERMISSION','delete permission', CURRENT_TIMESTAMP, null),
('READ_PERMISSION','read permission', CURRENT_TIMESTAMP, null),
('UPDATE_PERMISSION','update permission', CURRENT_TIMESTAMP, null),
('CREATE_ROLE','create role', CURRENT_TIMESTAMP, null),
('READ_ALL_ROLES','read all roles', CURRENT_TIMESTAMP, null),
('DELETE_ROLE','to delete role', CURRENT_TIMESTAMP, null),
('READ_ROLE','read role', CURRENT_TIMESTAMP, null),
('UPDATE_ROLE','update role', CURRENT_TIMESTAMP, null),
('SYNC_ROLE_WITH_PERMISSIONS','sync role with permissions', CURRENT_TIMESTAMP, null);