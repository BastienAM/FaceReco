INSERT INTO Role_User (Wording) VALUES ('Administrateur');

UPDATE Role_Right SET allow = TRUE WHERE pk_fk_role_user = 1;

INSERT INTO Account (username, password, fk_role_user) VALUES
('admin', '$2a$10$d7VCgG17hZgO/y1AW15AoeIf3lQxSIs80lYscKF2SKqqfRyLcEeQG', 1); -- mdp : admin