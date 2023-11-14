-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE pet (
id DECIMAL PRIMARY KEY,
name TEXT,
birthday DATE,
alive BOOLEAN
);

-- changeset tokovenko:2
CREATE TABLE client (
id DECIMAL PRIMARY KEY,
fio TEXT,
address TEXT,
birthday DATE,
passport TEXT,
chatId INTEGER,
beginningDay DATE,
petId INTEGER REFERENCES pet (id)
);

-- changeset tokovenko:3
CREATE TABLE employee (
id DECIMAL PRIMARY KEY,
fio TEXT,
address TEXT,
birthday DATE,
passport TEXT,
chatId INTEGER,
work_position TEXT
);

-- changeset tokovenko:4
ALTER TABLE pet ADD COLUMN pet_variety VARCHAR;