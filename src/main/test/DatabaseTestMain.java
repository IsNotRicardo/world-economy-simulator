import dao.*;
import datasource.MariaDbConnection;
import entity.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DatabaseTestMain {

	public static void main(String[] args) throws SQLException {
		try {
			// Initialize database and run SQL files
			MariaDbConnection.executeSqlFile("simulationDb.sql");
			MariaDbConnection.executeSqlFile("simulationMetrics.sql");

			// Create DAOs
			CountryDao countryDao = new CountryDao();
			ResourceDao resourceDao = new ResourceDao();
			CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
			ResourceMetricsDao resourceMetricsDao = new ResourceMetricsDao();
			ResourceNodeMetricsDao resourceNodeMetricsDao = new ResourceNodeMetricsDao();

			// Create Resources
			ResourceEntity silver = new ResourceEntity("Silver", 1, 100, 10);
			ResourceEntity platinum = new ResourceEntity("Platinum", 0.8, 200, 20);
			ResourceEntity maplesyrup = new ResourceEntity("Maple Syrup", 0.2, 300, 30);

			// Persist Resources
			resourceDao.persist(silver);
			resourceDao.persist(platinum);
			resourceDao.persist(maplesyrup);

			// Create Countries
			CountryEntity usa = new CountryEntity("USA", 1000, 100000);
			CountryEntity canada = new CountryEntity("Canada", 2000, 200000);

			// Create CountryResource relations
			CountryResource usaSilver = new CountryResource(usa, silver, 50);
			CountryResource usaPlatinum = new CountryResource(usa, platinum, 30);
			CountryResource canadaSilver = new CountryResource(canada, silver, 20);
			CountryResource canadaPlatinum = new CountryResource(canada, platinum, 10);
			CountryResource canadaMapleSyrup = new CountryResource(canada, maplesyrup, 5);

			// Add CountryResource to countries
			Set<CountryResource> usaResources = new HashSet<>();
			usaResources.add(usaSilver);
			usaResources.add(usaPlatinum);
			usa.setCountryResources(usaResources);

			Set<CountryResource> canadaResources = new HashSet<>();
			canadaResources.add(canadaSilver);
			canadaResources.add(canadaPlatinum);
			canadaResources.add(canadaMapleSyrup);
			canada.setCountryResources(canadaResources);

			// Persist Countries
			countryDao.persist(usa);
			countryDao.persist(canada);

			// Create and Persist Country Metrics
			CountryMetricsEntity usaMetrics = new CountryMetricsEntity(1, usa, 100000, 1000, 75.5, 500);
			CountryMetricsEntity canadaMetrics = new CountryMetricsEntity(1, canada, 200000, 2000, 80.0, 1000);
			countryMetricsDao.persist(usaMetrics);
			countryMetricsDao.persist(canadaMetrics);

			// Create and Persist Resource Metrics
			ResourceMetricsEntity usaSilverMetrics = new ResourceMetricsEntity(1, usa, silver, 50, 500);
			ResourceMetricsEntity canadaPlatinumMetrics = new ResourceMetricsEntity(1, canada, platinum, 10, 200);
			resourceMetricsDao.persist(usaSilverMetrics);
			resourceMetricsDao.persist(canadaPlatinumMetrics);

			// Create and Persist Resource Node Metrics
			ResourceNodeMetricsEntity usaSilverNodeMetrics = new ResourceNodeMetricsEntity(1, usa, silver, 10, 100, 1);
			ResourceNodeMetricsEntity canadaMapleSyrupNodeMetrics = new ResourceNodeMetricsEntity(1, canada, maplesyrup, 5, 50, 1);
			resourceNodeMetricsDao.persist(usaSilverNodeMetrics);
			resourceNodeMetricsDao.persist(canadaMapleSyrupNodeMetrics);

			// Print all country metrics
			countryMetricsDao.findAll().forEach(System.out::println);

			// Print all resource metrics
			resourceMetricsDao.findAll().forEach(System.out::println);

			// Print all resource node metrics
			resourceNodeMetricsDao.findAll().forEach(System.out::println);

			// Close EntityManager
			try {
				MariaDbConnection.terminate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			System.out.println("Test completed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}