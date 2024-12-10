DROP USER IF EXISTS 'simulation_user'@'localhost';

CREATE USER IF NOT EXISTS 'simulation_user'@'localhost' IDENTIFIED BY 'password';

GRANT CREATE, SELECT, UPDATE, DELETE ON simulation.* TO 'simulation_user'@'localhost';
FLUSH PRIVILEGES;
