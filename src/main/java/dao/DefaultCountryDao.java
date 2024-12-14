package dao;

import entity.CountryEntity;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing operations on the "default_country" database table.
 * Provides methods for retrieving information about countries using raw SQL queries.
 */
public class DefaultCountryDao {

	/**
	 * Logger for logging debug and error messages.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DefaultCountryDao.class);

	/**
	 * Retrieves all countries from the "default_country" table.
	 *
	 * @return A list of {@link CountryEntity} objects representing all the rows in the "default_country" table.
	 * @throws SQLException If an SQL error occurs during the query execution.
	 */
	public List<CountryEntity> findAll() throws SQLException {
		Connection conn = datasource.MariaDbConnection.getConnection();
		String sql = "SELECT * from default_country";

		String countryName;
		double money;
		long population;
		List<CountryEntity> countries = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				countryName = rs.getString("name");
				money = rs.getDouble("money");
				population = rs.getLong("population");
				countries.add(new CountryEntity(countryName, money, population));
			}
		} catch (SQLException e) {
			logger.error("Error finding all countries", e);
			throw e;
		}
		return countries;
	}

	/**
	 * Finds a country in the "default_country" table by its name.
	 *
	 * @param name The name of the country to search for.
	 * @return A {@link CountryEntity} object representing the country with the specified name, or {@code null} if no match is found.
	 * @throws SQLException If an SQL error occurs during the query execution.
	 */
	public CountryEntity findByName(String name) throws SQLException {
		Connection conn = datasource.MariaDbConnection.getConnection();
		String sql = "SELECT * from default_country where name = ?";

		String countryName = null;
		double money = 0;
		int population = 0;
		int count = 0;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				count++;
				countryName = rs.getString("name");
				money = rs.getDouble("money");
				population = rs.getInt("population");
			}
		} catch (SQLException e) {
			logger.error("Error finding country by name: {}", name, e);
			throw e;

		} catch (NoResultException e) {
			logger.debug("Country not found: {}", name);
			return null;
		}
		if (count == 1) {
			return new CountryEntity(countryName, money, population);
		} else {
			return null;
		}
	}
}