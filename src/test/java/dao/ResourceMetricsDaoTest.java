package dao;

import datasource.MariaDbConnection;
import entity.CountryEntity;
import entity.ResourceEntity;
import entity.ResourceMetricsEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceMetricsDaoTest {

	private static final String USER = "simulation_user";
	private static final String PASSWORD = "password";
	private static final String BASE_URL = "jdbc:mariadb://localhost:3306/";

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		MariaDbConnection.getConnection();
		MariaDbConnection.executeSqlFile("scripts/SimulationDb.sql");
		MariaDbConnection.executeSqlFile("scripts/SimulationMetrics.sql");
	}

	@AfterAll
	public static void tearDownDatabase() throws SQLException {
		Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
		conn.createStatement().executeUpdate("DROP DATABASE IF EXISTS `simulation`");
	}

	@BeforeEach
	public void setUp() {
		MariaDbConnection.getEntityManager();

		EntityManager em = MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM ResourceMetricsEntity").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testPersist() {
		ResourceDao resourceDao = new ResourceDao();
		CountryDao countryDao = new CountryDao();
		ResourceMetricsDao resourceMetricsDao = new ResourceMetricsDao();

		EntityManager em = MariaDbConnection.getEntityManager();
		em.getTransaction().begin();

		ResourceEntity resource = new ResourceEntity("TestResource", 0.5, 100, 100.0);
		resourceDao.persist(resource);
		em.flush();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);
		em.flush();

		// Now persist ResourceMetricsEntity
		ResourceMetricsEntity resourceMetrics = new ResourceMetricsEntity(1, country, resource, 100, 100.0);
		resourceMetricsDao.persist(resourceMetrics);
		em.flush();

		ResourceMetricsEntity foundResourceMetrics =
				resourceMetricsDao.findByResourceAndDay(country, resource, 1);

		em.getTransaction().commit();
		em.close();

		assertEquals(1, foundResourceMetrics.getDay());
		assertEquals(country, foundResourceMetrics.getCountry());
		assertEquals(resource, foundResourceMetrics.getResource());
		assertEquals(100, foundResourceMetrics.getQuantity());
		assertEquals(100.0, foundResourceMetrics.getValue());
	}

	@Test
	public void testFindByResource() {
		ResourceDao resourceDao = new ResourceDao();
		CountryDao countryDao = new CountryDao();
		ResourceMetricsDao resourceMetricsDao = new ResourceMetricsDao();

		EntityManager em = MariaDbConnection.getEntityManager();
		em.getTransaction().begin();

		ResourceEntity resource = new ResourceEntity("TestResource", 0.5, 100, 100.0);
		resourceDao.persist(resource);
		em.flush();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);
		em.flush();

		// Now persist ResourceMetricsEntity
		ResourceMetricsEntity resourceMetrics = new ResourceMetricsEntity(1, country, resource, 100, 100.0);
		resourceMetricsDao.persist(resourceMetrics);
		em.flush();

		List<ResourceMetricsEntity> foundResourceMetrics = resourceMetricsDao.findByResource(country, resource);

		em.getTransaction().commit();
		em.close();

		assertEquals(1, foundResourceMetrics.size());
		assertEquals(1, foundResourceMetrics.getFirst().getDay());
		assertEquals(country, foundResourceMetrics.getFirst().getCountry());
		assertEquals(resource, foundResourceMetrics.getFirst().getResource());
		assertEquals(100, foundResourceMetrics.getFirst().getQuantity());
		assertEquals(100.0, foundResourceMetrics.getFirst().getValue());
	}

}