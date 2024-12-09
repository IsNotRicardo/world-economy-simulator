CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON simulation.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;