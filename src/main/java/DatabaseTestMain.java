import dao.CountryDao;
import dao.ResourceDao;
import datasource.MariaDbConnection;
import entity.CountryEntity;
import entity.CountryResource;
import entity.ResourceEntity;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DatabaseTestMain {

	public static void main(String[] args) throws SQLException {
		try {
			// Initialize database and run SQL file
			MariaDbConnection.executeSqlFile("simulationDb.sql");

			// Create DAOs
			CountryDao countryDao = new CountryDao();
			ResourceDao resourceDao = new ResourceDao();

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

			// Print all countries
			countryDao.findAll().forEach(country -> {
				System.out.println("Country: " + country.getName());
			});

			// Print all resources for each country
			countryDao.findAll().forEach(country -> {
				System.out.println("Country: " + country.getName());
				country.getCountryResources().forEach(countryResource -> {
					System.out.println("Resource: " + countryResource.getResource().getName());
				});
			});

			// Print all resources for a specific country
			countryDao.findResourcesByCountryName("Canada").forEach(resource -> {
				System.out.println("Resource: " + resource.getName());
			});

			// Delete a country
			CountryEntity country = countryDao.findByName("USA");
			countryDao.delete(country);
			System.out.println("Deleted country: " + country.getName());

			// Print all countries
			countryDao.findAll().forEach(c -> {
				System.out.println("Country: " + c.getName());
			});

			// Print all resources
			resourceDao.findAll().forEach(r -> {
				System.out.println("Resource: " + r.getName());
			});

			// Delete a resource
			ResourceEntity resource = resourceDao.findByName("Silver");
			resourceDao.deleteResourceByName(resource.getName());
			System.out.println("Deleted resource: " + resource.getName());

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