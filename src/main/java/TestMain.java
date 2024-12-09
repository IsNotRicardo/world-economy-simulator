import dao.CountryDao;
import dao.CountryResourceDao;
import dao.ResourceDao;
import datasource.MariaDbConnection;
import entity.Country;
import entity.CountryResource;
import entity.Resource;

public class TestMain {

	public static void main(String[] args) {
		try {
			// Initialize database and run SQL file
			MariaDbConnection.executeSqlFile("simulationDb.sql");

			// Create DAOs
			CountryDao countryDao = new CountryDao();
			ResourceDao resourceDao = new ResourceDao();
			CountryResourceDao countryResourceDao = new CountryResourceDao();

			// Create Resources
			Resource silver = new Resource("Silver", 1, 100, 10);
			Resource platinum = new Resource("Platinum", 2, 200, 20);

			// Create Countries
			Country usa = new Country("USA", 1000, 100000);
			Country canada = new Country("Canada", 2000, 200000);

			// Persist Resources and Countries
			resourceDao.persist(silver);
			resourceDao.persist(platinum);
			countryDao.persist(usa);
			countryDao.persist(canada);

			// Create CountryResource relationships
			CountryResource usaSilver = new CountryResource();
			usaSilver.setCountry(usa);
			usaSilver.setResource(silver);
			CountryResource usaPlatinum = new CountryResource();
			usaPlatinum.setCountry(usa);
			usaPlatinum.setResource(platinum);
			CountryResource canadaSilver = new CountryResource();
			canadaSilver.setCountry(canada);
			canadaSilver.setResource(silver);
			CountryResource canadaPlatinum = new CountryResource();
			canadaPlatinum.setCountry(canada);
			canadaPlatinum.setResource(platinum);

			// Persist CountryResource relationships
			countryResourceDao.persist(usaSilver);
			countryResourceDao.persist(usaPlatinum);
			countryResourceDao.persist(canadaSilver);
			countryResourceDao.persist(canadaPlatinum);

			// Close EntityManager
			MariaDbConnection.terminate();

			System.out.println("Test completed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}