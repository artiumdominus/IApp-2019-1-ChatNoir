DROP DATABASE IF EXISTS UselessChat;
CREATE DATABASE IF NOT EXISTS UselessChat;
USE UselessChat;
SET time_zone = '+3:00';

CREATE TABLE Perfil (
nome VARCHAR(70) NOT NULL,
username VARCHAR(70) NOT NULL,
senha VARCHAR(20) NOT NULL,
numeroDeTelefone VARCHAR(70) NOT NULL,
bio VARCHAR(70),
chatAddress VARCHAR(70) NOT NULL,
PRIMARY KEY (username)
);

CREATE TABLE Grupo (
nome VARCHAR(70) NOT NULL,
groupname VARCHAR(70) NOT NULL,
descricao VARCHAR(70),
tipo TINYINT,
criador VARCHAR(70) NOT NULL,
chatAddress VARCHAR(70) NOT NULL,
PRIMARY KEY (groupname)
);

CREATE TABLE Mensagem (
id INT NOT NULL AUTO_INCREMENT,
conteudo VARCHAR(1000) NOT NULL,
envio DATETIME NOT NULL,
status VARCHAR(10),
emissor VARCHAR(70),
receptor VARCHAR(70),
PRIMARY KEY (id)
);

CREATE TABLE Membro (
groupname VARCHAR(70) NOT NULL,
username VARCHAR(70) NOT NULL
);

CREATE TABLE Administrador (
groupname VARCHAR(70) NOT NULL,
username VARCHAR(70) NOT NULL
);