CREATE TABLE `role`
(
    `id`   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NULL
);
CREATE TABLE `user_detail`
(
    `id`                      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `passport_number`         VARCHAR(45) NULL,
    `address`                 VARCHAR(45) NULL,
    `educational_institution` VARCHAR(100) NULL,
    `ei_address`              VARCHAR(100) NULL

);

CREATE TABLE `user`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name`     VARCHAR(45)  NOT NULL,
    `last_name`      VARCHAR(45)  NOT NULL,
    `email`          VARCHAR(45)  NOT NULL,
    `password`       VARCHAR(255) NOT NULL,
    `user_detail_id` BIGINT       NOT NULL UNIQUE,
    `date_creation`  TIMESTAMP NULL,
    `enabled`        INT          NOT NULL,
    FOREIGN KEY (user_detail_id) REFERENCES user_detail (id) ON DELETE CASCADE
);









