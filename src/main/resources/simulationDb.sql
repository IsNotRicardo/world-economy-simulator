USE simulation;

DROP TABLE IF EXISTS default_country;
DROP TABLE IF EXISTS default_resource;

CREATE TABLE default_resource
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL UNIQUE,
    priority        DOUBLE       NOT NULL,
    base_capacity   INT          NOT NULL,
    production_cost DOUBLE       NOT NULL
);

CREATE TABLE default_country
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL UNIQUE,
    money      DOUBLE       NOT NULL,
    population BIGINT       NOT NULL
);

-- Insert data into default tables
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

INSERT IGNORE INTO default_country (name, money, population)
VALUES ('Finland', 276600000000.00, 5527573),
       ('Sweden', 593300000000.00, 10327589),
       ('Norway', 485500000000.00, 5421241),
       ('Denmark', 404200000000.00, 5831404),
       ('Iceland', 31020000000.00, 388425);

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

CREATE TABLE IF NOT EXISTS country_resource
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    country_id  BIGINT,
    resource_id BIGINT,
    quantity    INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resource (id) ON DELETE CASCADE
);

DELIMITER //

CREATE PROCEDURE insert_country_resource()
BEGIN
    IF (
        (SELECT COUNT(*) FROM country) = (SELECT COUNT(*) FROM default_country) AND
        (SELECT COUNT(*) FROM resource) = (SELECT COUNT(*) FROM default_resource) AND
        NOT EXISTS (SELECT 1
                    FROM country c
                             LEFT JOIN default_country dc ON c.id = dc.id
                    WHERE dc.id IS NULL) AND
        NOT EXISTS (SELECT 1
                    FROM resource r
                             LEFT JOIN default_resource dr ON r.id = dr.id
                    WHERE dr.id IS NULL)
        ) THEN
        INSERT IGNORE INTO country_resource (country_id, resource_id, quantity)
        VALUES
            -- Finland
            ((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Copper'), 100),
            ((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Iron'), 200),
            ((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Food'), 300),
            ((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Wood'), 400),
            ((SELECT id FROM country WHERE name = 'Finland'), (SELECT id FROM resource WHERE name = 'Water'), 500),
            -- Sweden
            ((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Gold'), 100),
            ((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Iron'), 200),
            ((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Wood'), 300),
            ((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Water'), 400),
            ((SELECT id FROM country WHERE name = 'Sweden'), (SELECT id FROM resource WHERE name = 'Food'), 500),
            -- Norway
            ((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Aluminium'), 100),
            ((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Coal'), 200),
            ((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Oil'), 300),
            ((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Water'), 400),
            ((SELECT id FROM country WHERE name = 'Norway'), (SELECT id FROM resource WHERE name = 'Food'), 500),
            -- Denmark
            ((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Coal'), 100),
            ((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Iron'), 200),
            ((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Wood'), 300),
            ((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Water'), 400),
            ((SELECT id FROM country WHERE name = 'Denmark'), (SELECT id FROM resource WHERE name = 'Food'), 500),
            -- Iceland
            ((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Uranium'), 100),
            ((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Iron'), 200),
            ((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Wood'), 300),
            ((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Water'), 400),
            ((SELECT id FROM country WHERE name = 'Iceland'), (SELECT id FROM resource WHERE name = 'Food'), 500);
    END IF;
END //

DELIMITER ;

CALL insert_country_resource();