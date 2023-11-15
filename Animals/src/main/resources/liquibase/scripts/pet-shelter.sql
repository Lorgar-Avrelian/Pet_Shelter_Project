-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE pet (
id INTEGER PRIMARY KEY,
name TEXT,
birthday DATE,
alive BOOLEAN,
pet_variety VARCHAR,
pet_code INTEGER UNIQUE
);

-- changeset tokovenko:2
CREATE TABLE client (
id INTEGER PRIMARY KEY,
fio TEXT,
address TEXT,
birthday DATE,
passport TEXT,
chat_id INTEGER,
beginningDay DATE,
pets INTEGER REFERENCES pet (pet_code)
);

-- changeset tokovenko:3
CREATE TABLE employee (
id INTEGER PRIMARY KEY,
fio TEXT,
address TEXT,
birthday DATE,
passport TEXT,
chat_id INTEGER,
work_position TEXT
);