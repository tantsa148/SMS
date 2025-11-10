-- Supprimer la base (si elle existe)
DROP DATABASE IF EXISTS sms;

-- Créer la base
CREATE DATABASE sms;

-- Pour se connecter à la base sms
\c sms

CREATE TABLE users (
    id SERIAL PRIMARY KEY,        -- identifiant unique pour chaque utilisateur
    username VARCHAR(50) NOT NULL UNIQUE, -- nom d'utilisateur unique
    role VARCHAR(20) NOT NULL,    -- rôle de l'utilisateur (ex: ADMIN, USER)
    password VARCHAR(255) NOT NULL, -- mot de passe (hashé)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    texte TEXT NOT NULL
);
