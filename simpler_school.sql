CREATE DATABASE simpler_school

CREATE TABLE utente (
  user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username varchar(50) NOT NULL,
  nome varchar(50) DEFAULT NULL,
  cognome varchar(50) DEFAULT NULL,
  pass_hash varchar(150) NOT NULL,
  scuola varchar(50) DEFAULT NULL,
  avatar_path varchar(150) DEFAULT NULL
)AUTO_INCREMENT=10000
