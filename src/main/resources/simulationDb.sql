CREATE DATABASE IF NOT EXISTS simulation;
USE simulation;

-- Drop and recreate default tables
DROP TABLE IF EXISTS default_resource;
DROP TABLE IF EXISTS default_country;
DROP TABLE IF EXISTS default_country_resource;

CREATE TABLE default_resource
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL UNIQUE,
    priority        DOUBLE       NOT NULL,
    base_capacity   INT          NOT NULL,
    production_cost DOUBLE       NOT NULL
);

INSERT IGNORE INTO default_resource (name, priority, base_capacity, production_cost)
VALUES ('Wood', 0.8, 10, 10.0),
       ('Iron', 0.9, 10, 50.0),
       ('Gold', 0.7, 10, 100.0),
       ('Oil', 0.9, 10, 70.0),
       ('Coal', 0.8, 10, 30.0),
       ('Copper', 0.85, 10, 40.0),
       ('Aluminium', 0.8, 10, 60.0),
       ('Uranium', 0.6, 10, 200.0),
       ('Food', 1.0, 10, 20.0),
       ('Water', 1.0, 10, 5.0),
       ('Wheat', 0.95, 10, 15.0),
       ('Corn', 0.95, 10, 15.0),
       ('Rice', 0.95, 10, 15.0),
       ('Soybeans', 0.95, 10, 15.0),
       ('Barley', 0.95, 10, 15.0),
       ('Oats', 0.95, 10, 15.0),
       ('Rye', 0.95, 10, 15.0),
       ('Millet', 0.95, 10, 15.0),
       ('Sorghum', 0.95, 10, 15.0),
       ('Cotton', 0.9, 10, 25.0),
       ('Sugar', 0.9, 10, 20.0),
       ('Tobacco', 0.1, 10, 30.0);

CREATE TABLE default_country
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL UNIQUE,
    money      DOUBLE       NOT NULL,
    population LONG         NOT NULL
);

INSERT IGNORE INTO default_country (name, money, population)
VALUES ('Finland', 276600000000.00, 5527573),
       ('Sweden', 593300000000.00, 10327589),
       ('Norway', 485500000000.00, 5421241),
       ('Denmark', 404200000000.00, 5831404),
       ('Iceland', 31020000000.00, 388425);

CREATE TABLE default_country_resource
(
    country_id  INT,
    resource_id INT,
    PRIMARY KEY (country_id, resource_id),
    FOREIGN KEY (country_id) REFERENCES default_country (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES default_resource (id) ON DELETE CASCADE
);

INSERT IGNORE INTO default_country_resource (country_id, resource_id)
VALUES
-- Finland
((SELECT id FROM default_country WHERE name = 'Finland'), (SELECT id FROM default_resource WHERE name = 'Food')),
((SELECT id FROM default_country WHERE name = 'Finland'), (SELECT id FROM default_resource WHERE name = 'Water')),
((SELECT id FROM default_country WHERE name = 'Finland'), (SELECT id FROM default_resource WHERE name = 'Wood')),
((SELECT id FROM default_country WHERE name = 'Finland'), (SELECT id FROM default_resource WHERE name = 'Iron')),
((SELECT id FROM default_country WHERE name = 'Finland'), (SELECT id FROM default_resource WHERE name = 'Copper')),
-- Sweden
((SELECT id FROM default_country WHERE name = 'Sweden'), (SELECT id FROM default_resource WHERE name = 'Food')),
((SELECT id FROM default_country WHERE name = 'Sweden'), (SELECT id FROM default_resource WHERE name = 'Water')),
((SELECT id FROM default_country WHERE name = 'Sweden'), (SELECT id FROM default_resource WHERE name = 'Wood')),
((SELECT id FROM default_country WHERE name = 'Sweden'), (SELECT id FROM default_resource WHERE name = 'Iron')),
((SELECT id FROM default_country WHERE name = 'Sweden'), (SELECT id FROM default_resource WHERE name = 'Gold')),
-- Norway
((SELECT id FROM default_country WHERE name = 'Norway'), (SELECT id FROM default_resource WHERE name = 'Food')),
((SELECT id FROM default_country WHERE name = 'Norway'), (SELECT id FROM default_resource WHERE name = 'Water')),
((SELECT id FROM default_country WHERE name = 'Norway'), (SELECT id FROM default_resource WHERE name = 'Oil')),
((SELECT id FROM default_country WHERE name = 'Norway'), (SELECT id FROM default_resource WHERE name = 'Coal')),
((SELECT id FROM default_country WHERE name = 'Norway'), (SELECT id FROM default_resource WHERE name = 'Aluminium')),
-- Denmark
((SELECT id FROM default_country WHERE name = 'Denmark'), (SELECT id FROM default_resource WHERE name = 'Food')),
((SELECT id FROM default_country WHERE name = 'Denmark'), (SELECT id FROM default_resource WHERE name = 'Water')),
((SELECT id FROM default_country WHERE name = 'Denmark'), (SELECT id FROM default_resource WHERE name = 'Wood')),
((SELECT id FROM default_country WHERE name = 'Denmark'), (SELECT id FROM default_resource WHERE name = 'Iron')),
((SELECT id FROM default_country WHERE name = 'Denmark'), (SELECT id FROM default_resource WHERE name = 'Coal')),
-- Iceland
((SELECT id FROM default_country WHERE name = 'Iceland'), (SELECT id FROM default_resource WHERE name = 'Food')),
((SELECT id FROM default_country WHERE name = 'Iceland'), (SELECT id FROM default_resource WHERE name = 'Water')),
((SELECT id FROM default_country WHERE name = 'Iceland'), (SELECT id FROM default_resource WHERE name = 'Wood')),
((SELECT id FROM default_country WHERE name = 'Iceland'), (SELECT id FROM default_resource WHERE name = 'Iron')),
((SELECT id FROM default_country WHERE name = 'Iceland'), (SELECT id FROM default_resource WHERE name = 'Uranium'));

-- Create resource table if it doesn't exist and populate it if empty
CREATE TABLE IF NOT EXISTS resource LIKE default_resource;

INSERT IGNORE INTO resource
SELECT *
FROM default_resource
WHERE (SELECT COUNT(*) FROM resource) = 0;

-- Create country table if it doesn't exist and populate it if empty
CREATE TABLE IF NOT EXISTS country LIKE default_country;

INSERT IGNORE INTO country
SELECT *
FROM default_country
WHERE (SELECT COUNT(*) FROM country) = 0;

-- Create many-to-many relationship table if it doesn't exist
CREATE TABLE IF NOT EXISTS country_resource
(
    country_id  INT,
    resource_id INT,
    PRIMARY KEY (country_id, resource_id),
    FOREIGN KEY (country_id) REFERENCES country (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resource (id) ON DELETE CASCADE
);

-- Populate many-to-many relationship table only if country and resource tables match default tables and if it's empty
INSERT IGNORE INTO country_resource (country_id, resource_id)
SELECT dc.id, dr.id
FROM default_country_resource dcr
         JOIN default_country dc ON dcr.country_id = dc.id
         JOIN default_resource dr ON dcr.resource_id = dr.id
WHERE NOT EXISTS (SELECT 1
                  FROM country c
                           JOIN resource r ON c.id = r.id
                  WHERE c.name != dc.name
                     OR r.name != dr.name)
  AND (SELECT COUNT(*) FROM country_resource) = 0;