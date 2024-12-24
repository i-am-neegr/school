CREATE TABLE Person (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        age INT NOT NULL CHECK (age >= 0),
                        has_license BOOLEAN NOT NULL
);

CREATE TABLE Car (
                     id SERIAL PRIMARY KEY,
                     brand VARCHAR(255) NOT NULL,
                     model VARCHAR(255) NOT NULL,
                     cost NUMERIC(10, 2) NOT NULL CHECK (cost >= 0)
);

create table person_car (
                            person_id INT REFERENCES Person(id) ON DELETE CASCADE,
                            car_id INT REFERENCES Car(id) ON DELETE CASCADE,
                            PRIMARY KEY (person_id, car_id)
);