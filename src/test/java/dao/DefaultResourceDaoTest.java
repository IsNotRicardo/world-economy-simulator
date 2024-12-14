package dao;

import datasource.MariaDbConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultResourceDaoTest {

	private DefaultResourceDao defaultResourceDao;

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		try (Connection conn = MariaDbConnection.getConnection()) {
			MariaDbConnection.executeSqlFile("scripts/simulationDb.sql");
		}
	}

	@AfterAll
	public static void tearDownDatabase() throws SQLException {
		try (Connection conn = MariaDbConnection.getConnection()) {
			conn.createStatement().executeUpdate("DROP SCHEMA IF EXISTS `simulation`");
		}
	}

	@BeforeEach
	public void setUp() {
		defaultResourceDao = new DefaultResourceDao();
	}

	@Test
	public void testFindAll() throws SQLException {
		assertEquals(22, defaultResourceDao.findAll().size());
	}

	@Test
	public void testFindByName() throws SQLException {
		assertEquals("Food", defaultResourceDao.findByName("Food").getName());
	}

	@Test
	public void testFindByNameNoResult() throws SQLException {
		assertNull(defaultResourceDao.findByName("TestResource"));
	}

	@Test
	public void testFindByNameException() {
		try {
			defaultResourceDao.findByName("TestResource1");
		} catch (SQLException e) {
			assertEquals("Error finding resource by name", e.getMessage());
		}
	}

	@Test
	public void testFindAllException() {
		try {
			defaultResourceDao.findAll();
		} catch (SQLException e) {
			assertEquals("Error finding all resources", e.getMessage());
		}
	}
}