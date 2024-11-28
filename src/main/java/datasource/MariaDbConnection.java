package datasource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MariaDbConnection {

	private static Connection conn = null;
	private static String user;
	private static String password;

	static {
		try (InputStream input = MariaDbConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
			Properties prop = new Properties();
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
			}
			prop.load(input);
			user = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (conn == null) {
			try {
				createDatabaseIfNotExists();
				conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/simulation?user?=root&password=root");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return conn;
	}

	private static void createDatabaseIfNotExists() {
		try (Connection tempConn = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/?user=" + user + "&password=" +
				password); Statement stmt = tempConn.createStatement()
		) {
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS simulation");
			createTablesIfNotExists(tempConn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void createTablesIfNotExists(Connection conn) {
		try (Statement stmt = conn.createStatement()) {
			String createResourceTable = """
			                              CREATE TABLE IF NOT EXISTS resource (
			                              	id INT NOT NULL AUTO_INCREMENT,
			                              	name VARCHAR(255) NOT NULL,
			                              	priority DOUBLE NOT NULL,
			                              	baseCapacity INT NOT NULL,
			                              	productionCost DOUBLE NOT NULL,
			                              	PRIMARY KEY (id)
			                             );
			                             """;
			String createCountryTable = """
			                                		CREATE TABLE IF NOT EXISTS country (
			                                     	id INT NOT NULL AUTO_INCREMENT,
			                                     	name VARCHAR(255) NOT NULL,
			                                     	money DOUBLE NOT NULL,
			                                     	population LONG NOT NULL,
			                                     	PRIMARY KEY (id)
			                                         /* FOREIGN KEY that references the resource node table */
			                            );
			                            """;
			stmt.executeUpdate(createCountryTable);
			stmt.executeUpdate(createResourceTable);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void terminate() {
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

