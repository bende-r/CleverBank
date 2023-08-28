CREATE DATABASE my_database;

\c my_database;

CREATE TABLE my_table
(
    id    serial PRIMARY KEY,
    name  VARCHAR(255),
    age   INT,
    email VARCHAR(255)
);

INSERT INTO my_table (name, age, email)
VALUES ('John Doe', 30, 'john@example.com');

INSERT INTO my_table (name, age, email)
VALUES ('Jane Smith', 25, 'jane@example.com');
