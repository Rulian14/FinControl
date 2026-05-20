CREATE DATABASE FinControl;
USE FinControl;

CREATE TABLE Usuario (
    ID_usuario INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Telefone_Usuario (
    ID_telefone INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(20) NOT NULL,
    ID_usuario INT NOT NULL,

    CONSTRAINT fk_usuario_telefone
        FOREIGN KEY (ID_usuario)
        REFERENCES Usuario(ID_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);