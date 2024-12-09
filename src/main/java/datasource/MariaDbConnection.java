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
import java.sql.*;

public class MariaDbConnection {

	private static Connection conn = null;
	private static EntityManagerFactory emf = null;
	private static final String USER = "appuser";
	private static final String PASSWORD = "password";
	private static final String BASE_URL = "jdbc:mariadb://localhost:3306/";
	private static final String URL = "jdbc:mariadb://localhost:3306/simulation";

	private static final Logger logger = LoggerFactory.getLogger(MariaDbConnection.class);

	public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			try {
				try (Connection baseConn = DriverManager.getConnection(BASE_URL);
				     Statement stmt = baseConn.createStatement()) {
					// Create the database if it doesn't exist
					stmt.executeUpdate("DROP DATABASE IF EXISTS `simulation`");
					stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `simulation`");

					// Check if the user exists
					ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM mysql.user WHERE user = 'appuser' AND host = 'localhost'");
					rs.next();
					boolean userExists = rs.getInt(1) > 0;

					// Throw an error if the user doesn't exist
					if (!userExists) {
						throw new SQLException("Database user 'appuser'@'localhost' does not exist. Please create the user and grant the necessary privileges.");
					}

					// Ensure the user has the necessary privileges
					stmt.executeUpdate("GRANT ALL PRIVILEGES ON `simulation`.* TO 'appuser'@'localhost'");
					stmt.executeUpdate("FLUSH PRIVILEGES");
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
		Connection conn = getConnection();
		try (InputStream input = MariaDbConnection.class.getClassLoader().getResourceAsStream(
				filePath); BufferedReader br = new BufferedReader(new InputStreamReader(input))
		) {
			StringBuilder sb = new StringBuilder();
			String line;
			boolean inBlockComment = false;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("/*")) {
					inBlockComment = true;
				}
				if (inBlockComment) {
					if (line.endsWith("*/")) {
						inBlockComment = false;
					}
					continue;
				}
				if (line.startsWith("--") || line.startsWith("//") || line.trim().isEmpty()) {
					continue;
				}
				sb.append(line).append("\n");
				if (line.trim().endsWith(";")) {
					try (Statement stmt = conn.createStatement()) {
						stmt.execute(sb.toString());
					}
					sb.setLength(0);
				}
			}
			logger.debug("Executed SQL file: {}", filePath);
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