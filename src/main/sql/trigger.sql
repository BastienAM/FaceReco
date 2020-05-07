CREATE OR REPLACE FUNCTION f_new_role()
RETURNS TRIGGER AS $$
BEGIN
	INSERT INTO role_right (pk_fk_role_user, pk_fk_right_user, allow) 
	SELECT NEW.pk, right_user.pk, FALSE FROM right_user;
	RETURN NEW;
END; $$ LANGUAGE 'plpgsql';


CREATE TRIGGER new_role AFTER INSERT 
ON role_user 
FOR EACH ROW
EXECUTE PROCEDURE f_new_role();

CREATE OR REPLACE FUNCTION f_new_right()
RETURNS TRIGGER AS $$
BEGIN
	INSERT INTO role_right (pk_fk_role_user, pk_fk_right_user, allow) 
	SELECT role_user.pk, NEW.pk, FALSE FROM role_user;
	RETURN NEW;
END; $$ LANGUAGE 'plpgsql';


CREATE TRIGGER new_right AFTER INSERT 
ON right_user 
FOR EACH ROW
EXECUTE PROCEDURE f_new_right();
