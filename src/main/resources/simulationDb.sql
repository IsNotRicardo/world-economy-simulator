CREATE DATABASE IF NOT EXISTS simulation;

USE simulation;

DROP TABLE IF EXISTS default_resource;
DROP TABLE IF EXISTS default_country;

CREATE TABLE IF NOT EXISTS default_resource
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL UNIQUE,
    category        VARCHAR(255) NOT NULL,
    priority        DOUBLE       NOT NULL,
    base_capacity   INT          NOT NULL,
    production_cost DOUBLE       NOT NULL
);

INSERT IGNORE INTO default_resource (name, category, priority, base_capacity, production_cost)
VALUES ('Wood', 'NATURAL', 0.8, 10, 10.0),
       ('Iron', 'METAL', 0.9, 10, 50.0),
       ('Gold', 'METAL', 0.7, 10, 100.0),
       ('Oil', 'NATURAL', 0.9, 10, 70.0),
       ('Coal', 'NATURAL', 0.8, 10, 30.0),
       ('Copper', 'METAL', 0.85, 10, 40.0),
       ('Aluminium', 'METAL', 0.8, 10, 60.0),
       ('Uranium', 'METAL', 0.6, 10, 200.0),
       ('Food', 'AGRICULTURAL', 1.0, 10, 20.0),
       ('Water', 'AGRICULTURAL', 1.0, 10, 5.0),
       ('Wheat', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Corn', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Rice', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Soybeans', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Barley', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Oats', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Rye', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Millet', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Sorghum', 'AGRICULTURAL', 0.95, 10, 15.0),
       ('Cotton', 'AGRICULTURAL', 0.9, 10, 25.0),
       ('Sugar', 'AGRICULTURAL', 0.9, 10, 20.0),
       ('Tobacco', 'AGRICULTURAL', 0.1, 10, 30.0);

CREATE TABLE IF NOT EXISTS resource like default_resource;

INSERT IGNORE INTO resource
select *
from default_resource;

CREATE TABLE IF NOT EXISTS default_country
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL UNIQUE,
    money      DOUBLE       NOT NULL,
    population LONG         NOT NULL
);

INSERT INTO default_country (name, money, population)
VALUES ('Finland', 276600000000.00, 5527573),
       ('Sweden', 593300000000.00, 10327589),
       ('Norway', 485500000000.00, 5421241),
       ('Denmark', 404200000000.00, 5831404),
       ('Iceland', 31020000000.00, 388425);

CREATE TABLE IF NOT EXISTS country like default_country;

INSERT IGNORE INTO country
select *
from default_country;

CREATE TABLE IF NOT EXISTS country_resource
(
    country_id  INT,
    resource_id INT,
    PRIMARY KEY (country_id, resource_id),
    FOREIGN KEY (country_id) REFERENCES country (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resource (id) ON DELETE CASCADE
);

INSERT IGNORE INTO country_resource (country_id, resource_id)
VALUES
-- Finland
((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Food')),
((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Water')),
((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Wood')),
((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Iron')),
((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Copper')),
-- Sweden
((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Food')),
((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Water')),
((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Wood')),
((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Iron')),
((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Gold')),
-- Norway
((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Food')),
((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Water')),
((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Oil')),
((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Coal')),
((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Aluminium')),
-- Denmark
((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Food')),
((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Water')),
((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Wood')),
((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Iron')),
((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Coal')),
-- Iceland
((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Food')),
((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Water')),
((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Wood')),
((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Iron')),
((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Uranium'));

/*
select *
from default_resource;

select *
from resource;

select *
from default_country;

select *
from country;

SELECT country_id, c.name AS country_name, r.name AS resource_name
FROM country_resource cr
         JOIN country c ON cr.country_id = c.id
         JOIN resource r ON cr.resource_id = r.id;
 */