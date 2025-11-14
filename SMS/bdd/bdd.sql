-- Supprimer la base (si elle existe)
DROP DATABASE IF EXISTS sms;

-- Créer la base
CREATE DATABASE sms;

-- Pour se connecter à la base sms
\c sms

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    texte TEXT NOT NULL
);
-- Table Utilisateur (exemple existant)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Numero
CREATE TABLE numero (
    id_numero SERIAL PRIMARY KEY,
    valeur_numero VARCHAR(20) NOT NULL UNIQUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE numero
ADD CONSTRAINT valeur_numero_non_vide CHECK (valeur_numero <> '');


-- Table Possede (relation N:N entre users et Numero)
CREATE TABLE possede (
    id_utilisateur INT NOT NULL,
    id_numero INT NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utilisateur, id_numero),
    FOREIGN KEY (id_utilisateur) REFERENCES users(id),
    FOREIGN KEY (id_numero) REFERENCES Numero(id_numero)
); 


-- Table Plateforme
CREATE TABLE plateforme (
    id SERIAL PRIMARY KEY,
    nom_plateforme VARCHAR(50) UNIQUE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Table DisponibleSur (relation N:N entre Numero et Plateforme)
CREATE TABLE disponible_sur (
    id_numero INT NOT NULL,
    id_plateforme INT NOT NULL,
    PRIMARY KEY (id_numero, id_plateforme),
    FOREIGN KEY (id_numero) REFERENCES Numero(id_numero),
    FOREIGN KEY (id_plateforme) REFERENCES plateforme(id)
);



-----------------------
-------scrip a modifier
-----------------------
CREATE TABLE Numero (
    id_numero INT PRIMARY KEY,
    valeur_numero VARCHAR(20)
);

CREATE TABLE Possede (
    id_utilisateur INT,
    id_numero INT,
    PRIMARY KEY (id_utilisateur, id_numero),
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur),
    FOREIGN KEY (id_numero) REFERENCES Numero(id_numero)
);

CREATE TABLE Plateforme (
    id_plateforme INT PRIMARY KEY,
    nom_plateforme VARCHAR(50)
);

CREATE TABLE DisponibleSur (
    id_numero INT,
    id_plateforme INT,
    PRIMARY KEY (id_numero, id_plateforme),
    FOREIGN KEY (id_numero) REFERENCES Numero(id_numero),
    FOREIGN KEY (id_plateforme) REFERENCES Plateforme(id_plateforme)
);
