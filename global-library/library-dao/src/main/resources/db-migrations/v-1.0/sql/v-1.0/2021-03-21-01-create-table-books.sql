CREATE TABLE `author`
(
    `id`   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NULL
);

CREATE TABLE `publisher`
(
    `id`   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NULL
);

CREATE TABLE `genre`
(
    `id`   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NULL
);

CREATE TABLE `book`
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `isbn`            VARCHAR(45) NULL,
    `name`            VARCHAR(200) NULL,
    `publishing_year` DATE NULL,
    `picture`         VARCHAR(100) NULL,
    `description`     TEXT NULL,
    `genre_id`        BIGINT NULL,
    `publisher_id`    BIGINT NULL,
    `quantity`        INT NULL,
    `date_creation`   TIMESTAMP NULL,
    FOREIGN KEY (genre_id) REFERENCES genre (id),
    FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);

CREATE TABLE `book_author`
(
    `book_id`   BIGINT NOT NULL,
    `author_id` BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT FK_book FOREIGN KEY (book_id)
        REFERENCES book (id) ON DELETE CASCADE,
    CONSTRAINT FK_author FOREIGN KEY (author_id)
        REFERENCES author (id) ON DELETE CASCADE
);

CREATE TABLE `rating`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      BIGINT    NOT NULL,
    `book_id`      BIGINT    NOT NULL,
    `date`         TIMESTAMP NOT NULL,
    `rating_value` DECIMAL   NOT NULL,
    `review`       VARCHAR(10000) NULL,
    FOREIGN KEY (user_id) references user (id),
    FOREIGN KEY (book_id) references book (id)

);

CREATE TABLE `request`
(
    `id`      BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT    NOT NULL,
    `book_id` BIGINT    NOT NULL UNIQUE,
    `date`    TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) references user (id),
    FOREIGN KEY (book_id) references book (id)

);

CREATE TABLE `extradition`
(
    `id`      BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT    NOT NULL,
    `book_id` BIGINT    NOT NULL UNIQUE,
    `date`    TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) references user (id),
    FOREIGN KEY (book_id) references book (id)
);

