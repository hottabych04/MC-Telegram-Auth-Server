--liquibase formatted sql

--changeset hottabych04:1
INSERT INTO account(username, uuid, telegram_id)
      VALUES('testUser1', 'gfds8gusdug5342kjn', '4231412'),
            ('testUser2', 'nsdfaigjasidjt4535', '4353245345'),
            ('testUser3', 'gfd8g48t3929n4k45n', '7645634'),
            ('testUser4', 'sd8998435ntkjenmfd', '134123565');