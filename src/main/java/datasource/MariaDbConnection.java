package datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDbConnection {

	private static Connection conn = null;
	private static EntityManagerFactory emf = null;
	private static final String USER = "simulation_user";
	private static final String PASSWORD = "password";
	private static final String BASE_URL = "jdbc:mariadb://localhost:3306/";
	private static final String URL = "jdbc:mariadb://localhost:3306/simulation";

	private static final Logger logger = LoggerFactory.getLogger(MariaDbConnection.class);

	public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			try {
				try (Connection baseConn = DriverManager.getConnection(BASE_URL, USER, PASSWORD)) {
					baseConn.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS `simulation`");
				}

				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				logger.debug("Connected to MariaDB");
			} catch (SQLException e) {
				logger.error("Failed to connect to MariaDB!", e);
				throw new SQLException(e);
			}
		}
		return conn;
	}

	public static EntityManager getEntityManager() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("Simulation");
		}
		return emf.createEntityManager();
	}

	public static void executeSqlFile(String filePath) throws SQLException {
		try (InputStream input = MariaDbConnection.class.getClassLoader().getResourceAsStream(filePath)
		) {
			assert input != null;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(input))
			) {
				StringBuilder sb = new StringBuilder();
				String line;
				boolean inProcedure = false;
				while ((line = br.readLine()) != null) {
					if (line.trim().equalsIgnoreCase("DELIMITER //")) {
						inProcedure = true;
						continue;
					} else if (line.trim().equalsIgnoreCase("DELIMITER ;")) {
						inProcedure = false;
						continue;
					}
					if (inProcedure) {
						if (line.trim().endsWith("//")) {
							sb.append(line, 0, line.length() - 2).append("\n");
							try (Statement stmt = conn.createStatement()) {
								stmt.execute(sb.toString());
							}
							sb.setLength(0);
						} else {
							sb.append(line).append("\n");
						}
					} else {
						sb.append(line).append("\n");
						if (line.trim().endsWith(";")) {
							try (Statement stmt = conn.createStatement()) {
								stmt.execute(sb.toString());
							}
							sb.setLength(0);
						}
					}
				}
				logger.debug("Executed SQL file: {}", filePath);
			}
		} catch (SQLException | IOException ex) {
			logger.error("Failed to execute SQL file!", ex);
			throw new SQLException(ex);
		}
	}

	public static void terminate() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				logger.debug("Connection closed");
			}
			if (emf != null) {
				emf.close();
				logger.debug("EntityManagerFactory closed");
			}
		} catch (SQLException e) {
			logger.error("Failed to close connection!", e);
			throw new SQLException(e);
		}
	}
}