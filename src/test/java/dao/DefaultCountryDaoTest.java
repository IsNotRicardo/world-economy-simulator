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

public class DefaultCountryDaoTest {

	private DefaultCountryDao defaultCountryDao;

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		MariaDbConnection.getConnection();
		MariaDbConnection.executeSqlFile("scripts/simulationDb.sql");
	}

	@AfterAll
	public static void tearDownDatabase() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		conn.createStatement().executeUpdate("DROP SCHEMA IF EXISTS `simulation`");
	}



	@BeforeEach
	public void setUp() {
		defaultCountryDao = new DefaultCountryDao();
	}

	@Test
	public void testFindAll() throws SQLException {
		assertEquals(5, defaultCountryDao.findAll().size());
	}

	@Test
	public void testFindByName() throws SQLException {
		assertEquals("Finland", defaultCountryDao.findByName("Finland").getName());
	}

	@Test
	public void testFindByNameNoResult() throws SQLException {
		assertNull(defaultCountryDao.findByName("TestCountry4"));
	}

	@Test
	public void testFindByNameException() {
		try {
			defaultCountryDao.findByName("TestCountry1");
		} catch (SQLException e) {
			assertEquals("Error finding country by name", e.getMessage());
		}
	}

	@Test
	public void testFindAllException() {
		try {
			defaultCountryDao.findAll();
		} catch (SQLException e) {
			assertEquals("Error finding all countries", e.getMessage());
		}
	}

	@Test
	public void testFindByNameNoResultException() throws SQLException {
		assertNull(defaultCountryDao.findByName("TestCountry4"));
	}
}