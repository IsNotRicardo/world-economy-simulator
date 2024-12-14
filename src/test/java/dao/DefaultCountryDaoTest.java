package dao;

import datasource.MariaDbConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static datasource.MariaDbConnection.executeSqlFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultCountryDaoTest {

	private DefaultCountryDao countryDao;

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		executeSqlFile("scripts/SimulationDb.sql");
	}

	@AfterAll
	public static void tearDownDatabase() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		conn.createStatement().executeUpdate("DROP DATABASE IF EXISTS `simulation`");
		conn.close();
	}

	@BeforeEach
	public void setUp() {
		countryDao = new DefaultCountryDao();
	}

	@Test
	public void testFindAll() throws SQLException {
		assertEquals(5, countryDao.findAll().size());
	}

	@Test
	public void testFindByName() throws SQLException {
		assertEquals("Finland", countryDao.findByName("Finland").getName());
	}

	@Test
	public void testFindByNameNoResult() throws SQLException {
		assertNull(countryDao.findByName("TestCountry4"));
	}

	@Test
	public void testFindByNameException() {
		try {
			countryDao.findByName("TestCountry1");
		} catch (SQLException e) {
			assertEquals("Error finding country by name", e.getMessage());
		}
	}

	@Test
	public void testFindAllException() {
		try {
			countryDao.findAll();
		} catch (SQLException e) {
			assertEquals("Error finding all countries", e.getMessage());
		}
	}

	@Test
	public void testFindByNameNoResultException() throws SQLException {
		assertNull(countryDao.findByName("TestCountry4"));
	}
}