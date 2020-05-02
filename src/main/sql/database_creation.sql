CREATE TABLE Student (
	Number integer NOT NULL,
	Last_Name varchar(255) NOT NULL,
	First_Name varchar(255) NOT NULL,
	FK_Group_Student integer NOT NULL,
	CONSTRAINT "Student_pk" PRIMARY KEY (Number)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Account (
	PK serial NOT NULL,
	Username varchar(255) NOT NULL UNIQUE,
	Password varchar(255) NOT NULL,
	FK_Role_User integer NOT NULL,
	CONSTRAINT "Account_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Role_User (
	PK serial NOT NULL,
	Wording varchar(255) NOT NULL,
	CONSTRAINT "Role_User_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Right_User (
	PK serial NOT NULL,
	Key varchar(255) NOT NULL UNIQUE,
	Wording varchar(255) NOT NULL,
	CONSTRAINT "Right_User_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Role_Right (
	PK_FK_Role_User integer NOT NULL,
	PK_FK_Right_User integer NOT NULL,
	Allow BOOLEAN NOT NULL,
	CONSTRAINT "Role_Right_pk" PRIMARY KEY (PK_FK_Role_User,PK_FK_Right_User)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Group_Student (
	PK serial NOT NULL,
	Wording varchar(255) NOT NULL,
	FK_Promotion integer NOT NULL,
	CONSTRAINT "Group_Student_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Promotion (
	PK serial NOT NULL,
	Wording varchar(255) NOT NULL,
	CONSTRAINT "Promotion_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Photo (
	PK serial NOT NULL,
	FK_Student integer NOT NULL,
	CONSTRAINT "Photo_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Timesheet (
	PK serial NOT NULL,
	Date TIMESTAMP NOT NULL,
	FK_Account integer NOT NULL,
	CONSTRAINT "Timesheet_pk" PRIMARY KEY (PK)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Presence (
	PK_FK_Timesheet integer NOT NULL,
	PK_FK_Student integer NOT NULL,
	Present BOOLEAN NOT NULL,
	CONSTRAINT "Presence_pk" PRIMARY KEY (PK_FK_Timesheet,PK_FK_Student)
) WITH (
  OIDS=FALSE
);



ALTER TABLE Student ADD CONSTRAINT "Student_fk0" FOREIGN KEY (FK_Group_Student) REFERENCES Group_Student(PK);

ALTER TABLE Account ADD CONSTRAINT "Account_fk0" FOREIGN KEY (FK_Role_User) REFERENCES Role_User(PK);



ALTER TABLE Role_Right ADD CONSTRAINT "Role_Right_fk0" FOREIGN KEY (PK_FK_Role_User) REFERENCES Role_User(PK) ON DELETE CASCADE;
ALTER TABLE Role_Right ADD CONSTRAINT "Role_Right_fk1" FOREIGN KEY (PK_FK_Right_User) REFERENCES Right_User(PK) ON DELETE CASCADE;

ALTER TABLE Group_Student ADD CONSTRAINT "Group_Student_fk0" FOREIGN KEY (FK_Promotion) REFERENCES Promotion(PK);


ALTER TABLE Signature ADD CONSTRAINT "Photo_fk0" FOREIGN KEY (FK_Student) REFERENCES Student(Number) ON DELETE CASCADE;

ALTER TABLE Timesheet ADD CONSTRAINT "Timesheet_fk0" FOREIGN KEY (FK_Account) REFERENCES Account(PK);

ALTER TABLE Presence ADD CONSTRAINT "Presence_fk0" FOREIGN KEY (PK_FK_Timesheet) REFERENCES Timesheet(PK);
ALTER TABLE Presence ADD CONSTRAINT "Presence_fk1" FOREIGN KEY (PK_FK_Student) REFERENCES Student(Number);

