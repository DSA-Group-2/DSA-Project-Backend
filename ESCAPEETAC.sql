DROP DATABASE IF EXISTS ESCAPEEETAC;
CREATE DATABASE ESCAPEEETAC;
USE ESCAPEEETAC;

-- =========================
-- TABLA DE JUGADORES
-- =========================
CREATE TABLE jugadores (
    id_jugador INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    dinero INT DEFAULT 0
) AUTO_INCREMENT=1000;

-- =========================
-- TABLA DE PARTIDAS
-- =========================
CREATE TABLE partidas (
    id_partida INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME,
    duracion INT COMMENT 'Duración de la partida en minutos',
    partida_terminada TINYINT(1) DEFAULT 0
);

-- =========================
-- RELACIÓN PARTIDA - JUGADORES
-- =========================
CREATE TABLE partida_jugadores (
    id_partida INT,
    id_jugador INT,
    dinero_recogido INT DEFAULT 0,

    PRIMARY KEY (id_partida, id_jugador),

    FOREIGN KEY (id_partida) REFERENCES partidas(id_partida)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    FOREIGN KEY (id_jugador) REFERENCES jugadores(id_jugador)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================
-- DATOS DE PRUEBA
-- =========================

-- Jugadores
INSERT INTO jugadores (username, email, password) VALUES ('ElChavalPeca', 'chavalpeca@gmail.com', 'peca123');
INSERT INTO jugadores (username, email, password) VALUES ('Espinete34', 'espinete34@gmail.com', 'tqpepa');
INSERT INTO jugadores (username, email, password) VALUES ('ToniOllerElMejor', 'ollertoni@gmail.com', 'laianomola');
-- Partidas
INSERT INTO partidas (fecha_inicio, fecha_fin, duracion, partida_terminada)
VALUES
('2024-01-10 18:00:00', '2024-01-10 18:10:00', 10, 1),
('2024-02-15 19:00:00', '2024-02-15 19:20:00', 20, 1),
('2024-03-05 20:00:00', '2024-03-05 20:30:00', 30, 1);

-- Relación partidas - jugadores
INSERT INTO partida_jugadores VALUES (1, 1000, 500);

