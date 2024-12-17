CREATE TABLE student (
                         age INTEGER CHECK (age > 15) DEFAULT 20,
                         name text NOT NULL UNIQUE,
                         id BIGINT PRIMARY KEY
);

CREATE TABLE faculty(
                        id BIGINT PRIMARY KEY,
                        color text,
                        name text
);

Alter TABLE faculty
    ADD CONSTRAINT color_name_uniq UNIQUE (color, name);