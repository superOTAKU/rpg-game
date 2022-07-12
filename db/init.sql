DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`(
    `account_id` VARCHAR(255) PRIMARY KEY
);

DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`(
    `id` BIGINT PRIMARY KEY,
    `account_id` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(255),
    `vocation` VARCHAR(50) NOT NULL,
    `state` VARCHAR(50) NOT NULL
);
