package datasource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
				conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/simulation?user?=root&password=root");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return conn;
	}

	public static void terminate() {
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

