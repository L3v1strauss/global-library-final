CREATE TABLE `user_role`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_user FOREIGN KEY (user_id)
        REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT FK_role FOREIGN KEY (role_id)
        REFERENCES role (id) ON DELETE CASCADE
);

