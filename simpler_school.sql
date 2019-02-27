CREATE DATABASE simpler_school

GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost'
IDENTIFIED BY PASSWORD '*A8CA74B5D5245D3E86173E70B56406F9EFA6E72A' 
WITH GRANT OPTION;

CREATE TABLE utente (
	user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username varchar(50) NOT NULL,
	nome varchar(50) DEFAULT NULL,
	cognome varchar(50) DEFAULT NULL,
	pass_hash varchar(150) NOT NULL,
	scuola varchar(50) DEFAULT NULL,
	avatar_path varchar(150) DEFAULT NULL
)AUTO_INCREMENT=10000

CREATE TABLE PROFESSORE(
	prof_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) DEFAULT NULL,
	cognome varchar(50) DEFAULT NULL
)AUTO_INCREMENT=1000

CREATE TABLE MATERIA(
	materia_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL,
	color varchar(10) NOT NULL,
	user_id int NOT NULL,
	prof_id int DEFAULT NULL,
	prof2_id int DEFAULT NULL,
	FOREIGN KEY (user_id) REFERENCES utente(user_id),
	FOREIGN KEY (prof_id) REFERENCES PROFESSORE(prof_id),
	FOREIGN KEY (prof2_id) REFERENCES PROFESSORE(prof_id)
)AUTO_INCREMENT=100

CREATE TABLE ORARIOSETTIMANALE(
	os_id int not null AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) not null,
	file_path varchar(255) not null,
	user_id int not null,
	FOREIGN KEY (user_id) REFERENCES utente(user_id)
)AUTO_INCREMENT=100