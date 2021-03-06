CREATE DATABASE simpler_school;

GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost'
IDENTIFIED BY PASSWORD '*A8CA74B5D5245D3E86173E70B56406F9EFA6E72A' 
WITH GRANT OPTION;

USE SIMPLER_SCHOOL;

CREATE TABLE utente (
	user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username varchar(50) NOT NULL,
	nome varchar(50) DEFAULT NULL,
	cognome varchar(50) DEFAULT NULL,
	pass_hash varchar(150) NOT NULL,
	scuola varchar(50) DEFAULT NULL,
	avatar_path varchar(150) DEFAULT NULL
)AUTO_INCREMENT=100000;

CREATE TABLE DOCENTE(
	prof_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) DEFAULT NULL,
	cognome varchar(50) DEFAULT NULL,
	user_id int not null,
	FOREIGN KEY (user_id) REFERENCES utente(user_id) ON DELETE CASCADE
)AUTO_INCREMENT=10000;

CREATE TABLE MATERIA(
	materia_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL,
	color varchar(10) NOT NULL,
	user_id int NOT NULL,
	prof_id int DEFAULT NULL,
	prof2_id int DEFAULT NULL,
	FOREIGN KEY (user_id) REFERENCES utente(user_id) ON DELETE CASCADE,
	FOREIGN KEY (prof_id) REFERENCES DOCENTE(prof_id),
	FOREIGN KEY (prof2_id) REFERENCES DOCENTE(prof_id)
)AUTO_INCREMENT=1000;

CREATE TABLE INSEGNA(
	prof_id int not null,
	materia_id int not null,
	user_id int not null,
	FOREIGN KEY (prof_id) REFERENCES DOCENTE(prof_id) ON DELETE CASCADE,
	FOREIGN KEY (materia_id) REFERENCES MATERIA(materia_id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES utente(user_id) ON DELETE CASCADE,
	PRIMARY KEY(prof_id,materia_id)
)

CREATE TABLE ORARIOSETTIMANALE(
	os_id int not null AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) not null,
	file_path varchar(255) not null,
	user_id int not null,
	FOREIGN KEY (user_id) REFERENCES utente(user_id) ON DELETE CASCADE
)AUTO_INCREMENT=100;

CREATE TABLE TASK(
	TASK_ID int not null AUTO_INCREMENT PRIMARY KEY,
	TASK_DATA DATE NOT NULL,
	TIPO varchar(50) not null,
	COMMENTO varchar(255) not null,
	voto double,
	MATERIA_ID int NOT NULL,
	user_id int not null,
	os_id int not null,
	FOREIGN KEY (os_id) REFERENCES ORARIOSETTIMANALE(os_id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES utente(user_id) ON DELETE CASCADE,
	FOREIGN KEY (MATERIA_ID) REFERENCES materia(materia_id)
)AUTO_INCREMENT=100;

CREATE TABLE ALLEGATO(
	ALLEGATO_ID int not null AUTO_INCREMENT PRIMARY KEY,
	file_path varchar(255) not null,
	TASK_ID int not null,
	FOREIGN KEY (TASK_ID) REFERENCES TASK(TASK_ID) ON DELETE CASCADE
)AUTO_INCREMENT=100;

CREATE TRIGGER `delete_task` BEFORE DELETE ON `task`
FOR EACH ROW BEGIN
INSERT INTO history_task VALUES (OLD.TASK_ID, OLD.TASK_DATA, OLD.TIPO, OLD.COMMENTO, OLD.voto, OLD.MATERIA_ID, OLD.user_id, OLD.os_id, now());
END;

