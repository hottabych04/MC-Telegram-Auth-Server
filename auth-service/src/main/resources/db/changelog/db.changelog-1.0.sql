--liquibase formatted sql

--changeset hottabych04:1
CREATE TABLE IF NOT EXISTS account
(
    uuid VARCHAR(36) PRIMARY KEY ,
    username VARCHAR(16) NOT NULL ,
    telegram_id VARCHAR(32)
);

--changeset hottabych04:2
CREATE TABLE IF NOT EXISTS registration
(
    registration_hash VARCHAR(64) PRIMARY KEY ,
    account_id VARCHAR(36) REFERENCES account(uuid)
);
