package dao;

import datasource.MariaDbConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultResourceDaoTest {

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		MariaDbConnection.getConnection();
		MariaDbConnection.executeSqlFile("scripts/SimulationDb.sql");
	}

	@AfterAll
	public static void tearDownDatabase() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		conn.createStatement().executeUpdate("DROP DATABASE IF EXISTS `simulation`");
		conn.close();
	}

	@Test
	public void testFindAll() throws SQLException {
		DefaultResourceDao resourceDao = new DefaultResourceDao();
		assertEquals(22, resourceDao.findAll().size());
	}

	@Test
	public void testFindByName() throws SQLException {
		DefaultResourceDao resourceDao = new DefaultResourceDao();
		assertEquals("Food", resourceDao.findByName("Food").getName());
	}

	@Test
	public void testFindByNameNoResult() throws SQLException {
		DefaultResourceDao resourceDao = new DefaultResourceDao();
		assertNull(resourceDao.findByName("TestResource"));
	}

	@Test
	public void testFindByNameException() {
		DefaultResourceDao resourceDao = new DefaultResourceDao();
		try {
			resourceDao.findByName("TestResource1");
		} catch (SQLException e) {
			assertEquals("Error finding resource by name", e.getMessage());
		}
	}

	@Test
	public void testFindAllException() {
		DefaultResourceDao resourceDao = new DefaultResourceDao();
		try {
			resourceDao.findAll();
		} catch (SQLException e) {
			assertEquals("Error finding all resources", e.getMessage());
		}
	}
}