INSERT INTO genre (name) VALUES  ('FICTION'), ('SCIENCE'), ('MATH'), ('ECONOMY'), ('COMPUTER_SCIENCE');
INSERT INTO user_detail (passport_number, address, educational_institution, ei_address)
values ('KH7777777', 'Bogushevicha 16', 'IT Academy', 'M.Gorkogo 1');
INSERT INTO user (first_name, last_name, email, password, enabled)
values ('Levi', 'Strauss', 'L3v1s@rambler.ru', '$2a$10$IDpP0FWrQBI0btt90udzW./.R/jBMt2QPk8oDj1yMI6D/1yRVIJpq', '1');
INSERT into user_role(user_id, role_id) values ('1', '1');