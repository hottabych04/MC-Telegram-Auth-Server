--liquibase formatted sql

--changeset hottabych04:1
CREATE TABLE IF NOT EXISTS account
(
    id SERIAL PRIMARY KEY ,
    username VARCHAR(16) NOT NULL ,
    uuid VARCHAR(36) UNIQUE NOT NULL ,
    telegram_id VARCHAR(32) NOT NULL
);