package datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MariaDbConnection {

	private static Connection conn = null;
	private static String USER;
	private static String PASSWORD;
	private static final String BASE_URL = "jdbc:mariadb://localhost:3306/";
	private static final String URL = "jdbc:mariadb://localhost:3306/simulation";

	static {
		try (InputStream input = MariaDbConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
			Properties prop = new Properties();
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
			}
			prop.load(input);
			USER = prop.getProperty("db.user");
			PASSWORD = prop.getProperty("db.password");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			try {
				try (
						Connection baseConn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
						Statement stmt = baseConn.createStatement()
				) {
					stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `simulation`");
				}

				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (SQLException e) {
				System.out.println("Connection failed!");
				throw new SQLException(e);
			}
			return conn;
		}
		return conn;
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
		} catch (SQLException | IOException ex) {
			System.out.println("Failed to execute SQL file!");
			ex.printStackTrace();
		}
	}

	public static void terminate() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Failed to terminate connection!");
			e.printStackTrace();
		}
	}
}