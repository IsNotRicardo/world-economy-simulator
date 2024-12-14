package dao;

import datasource.MariaDbConnection;
import entity.CountryEntity;
import entity.CountryMetricsEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryMetricsDaoTest {

	@BeforeAll
	public static void setUpDatabase() throws SQLException {
		Connection conn =
		MariaDbConnection.getConnection();
		MariaDbConnection.terminate();
	}

	@BeforeEach
	public void setUp() {
		MariaDbConnection.getEntityManager();

		EntityManager em = MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM CountryMetricsEntity").executeUpdate();
		em.createQuery("DELETE FROM CountryEntity").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testPersist() {
		CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
		CountryDao countryDao = new CountryDao();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);

		CountryMetricsEntity countryMetrics =
				new CountryMetricsEntity(1, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		countryMetricsDao.persist(countryMetrics);

		CountryMetricsEntity foundCountryMetrics = countryMetricsDao.findByCountryAndDay(country, 1);
		assertEquals(1, foundCountryMetrics.getDay());
		assertEquals(country, foundCountryMetrics.getCountry());
		assertEquals(1_000_000, foundCountryMetrics.getPopulation());
		assertEquals(100_000_000.0, foundCountryMetrics.getMoney());
		assertEquals(0.5, foundCountryMetrics.getAverageHappiness());
		assertEquals(100.0, foundCountryMetrics.getIndividualBudget());
	}

	@Test
	public void testFindByCountry() {
		CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
		CountryDao countryDao = new CountryDao();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);

		CountryMetricsEntity countryMetrics =
				new CountryMetricsEntity(1, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		countryMetricsDao.persist(countryMetrics);

		CountryMetricsEntity foundCountryMetrics = countryMetricsDao.findByCountry(country).get(0);
		assertEquals(1, foundCountryMetrics.getDay());
		assertEquals(country, foundCountryMetrics.getCountry());
		assertEquals(1_000_000, foundCountryMetrics.getPopulation());
		assertEquals(100_000_000.0, foundCountryMetrics.getMoney());
		assertEquals(0.5, foundCountryMetrics.getAverageHappiness());
		assertEquals(100.0, foundCountryMetrics.getIndividualBudget());
	}

	@Test
	public void testFindAll() {
		CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
		CountryDao countryDao = new CountryDao();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);

		CountryMetricsEntity countryMetrics1 =
				new CountryMetricsEntity(1, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		CountryMetricsEntity countryMetrics2 =
				new CountryMetricsEntity(2, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		CountryMetricsEntity countryMetrics3 =
				new CountryMetricsEntity(3, country, 1_000_000, 100_000_000.0, 0.5, 100.0);

		countryMetricsDao.persist(countryMetrics1);
		countryMetricsDao.persist(countryMetrics2);
		countryMetricsDao.persist(countryMetrics3);

		assertEquals(3, countryMetricsDao.findAll().size());
	}

	@Test
	public void testFindByCountryAndDay() {
		CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
		CountryDao countryDao = new CountryDao();

		CountryEntity country = new CountryEntity("TestCountry", 100_000_000.0, 1_000_000);
		countryDao.persist(country);

		CountryMetricsEntity countryMetrics1 =
				new CountryMetricsEntity(1, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		CountryMetricsEntity countryMetrics2 =
				new CountryMetricsEntity(2, country, 1_000_000, 100_000_000.0, 0.5, 100.0);
		CountryMetricsEntity countryMetrics3 =
				new CountryMetricsEntity(3, country, 1_000_000, 100_000_000.0, 0.5, 100.0);

		countryMetricsDao.persist(countryMetrics1);
		countryMetricsDao.persist(countryMetrics2);
		countryMetricsDao.persist(countryMetrics3);

		CountryMetricsEntity foundCountryMetrics = countryMetricsDao.findByCountryAndDay(country, 2);
		assertEquals(2, foundCountryMetrics.getDay());
		assertEquals(country, foundCountryMetrics.getCountry());
		assertEquals(1_000_000, foundCountryMetrics.getPopulation());
		assertEquals(100_000_000.0, foundCountryMetrics.getMoney());
		assertEquals(0.5, foundCountryMetrics.getAverageHappiness());
		assertEquals(100.0, foundCountryMetrics.getIndividualBudget());
	}
}