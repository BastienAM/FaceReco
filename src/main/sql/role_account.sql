INSERT INTO Role_User (Wording) VALUES ('Administrateur');

UPDATE Role_Right SET allow = TRUE WHERE pk_fk_role_user = 1;

INSERT INTO Account (username, password, fk_role_user) VALUES
('admin', '$2a$10$d7VCgG17hZgO/y1AW15AoeIf3lQxSIs80lYscKF2SKqqfRyLcEeQG', 1); -- mdp : admin


INSERT INTO Role_User (Wording) VALUES ('Chargé d''appel');

UPDATE Role_Right AS RR SET RR.allow = TRUE FROM Right_User AS RU
WHERE RR.pk_fk_right_user = RU.pk AND RR.pk_fk_role_user = 2 AND Right.Key IN ('Recognition', 'TimesheetCreate', 'TimesheetUpdate', 'TimesheetDelete', 'TimesheetRead', 'StudentRead', 'GroupRead', 'PromotionRead');

INSERT INTO Account (username, password, fk_role_user) VALUES
('appel', '$2y$10$6I8hf21RJ37.6WBwSGS25.qtUxs8uodtAGUfjktyL5fjngUqXT/2C', 2); -- mdp : appel
