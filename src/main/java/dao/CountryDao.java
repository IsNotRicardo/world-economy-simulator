package dao;

import datasource.MariaDbConnection;
import model.core.Country;
import model.core.Resource;
import model.core.ResourceCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {

	private static final Logger logger = LoggerFactory.getLogger(CountryDao.class);

	public List<Country> getAllCountries() throws SQLException {
		logger.debug("Fetching all countries from the database");
		Connection conn = MariaDbConnection.getConnection();
		String sql = "SELECT * FROM country";
		List<Country> countries = new ArrayList<>();

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String countryName = rs.getString("name");
				double money = rs.getDouble("money");
				int population = rs.getInt("population");
//				Country country = new Country(countryName, money, population);
//				countries.add(country);
			}
		} catch (SQLException e) {
			logger.error("Error fetching countries", e);
			throw new RuntimeException(e);
		}
		logger.debug("Successfully fetched {} countries", countries.size());
		return countries;
	}

	public Country getCountryByName(String name) throws SQLException {
		logger.debug("Fetching country by name: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("SELECT * FROM country WHERE name = '%s'", name);
		int count = 0;
		String countryName = null;
		double money = 0;
		int population = 0;

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				count++;
				countryName = rs.getString("name");
				money = rs.getDouble("money");
				population = rs.getInt("population");
			}
		} catch (SQLException e) {
			logger.error("Error fetching country by name", e);
			throw new RuntimeException(e);
		}

		if (count == 1) {
			logger.debug("Successfully fetched country: {}", name);
//			return new Country(countryName, money, population);
			return null;
		} else {
			logger.error("Country not found: {}", name);
			throw new RuntimeException("Country not found");
		}
	}

	public List<Resource> getResourcesByCountryName(String countryName) throws SQLException {
		logger.debug("Fetching resources for country: {}", countryName);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"SELECT r.name, r.category, r.base_capacity, r.production_cost, r.priority " + "FROM resource r " +
				"JOIN country_resource cr ON r.id = cr.resource_id " + "JOIN country c ON cr.country_id = c.id " +
				"WHERE c.name = '%s'", countryName);
		List<Resource> resources = new ArrayList<>();

		try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			while (rs.next()) {
				String name = rs.getString("name");
				String category = rs.getString("category").toUpperCase();
				double priority = rs.getDouble("priority");
				int baseCapacity = rs.getInt("base_capacity");
				double productionCost = rs.getDouble("production_cost");
				Resource resource =
						new Resource(name, ResourceCategory.valueOf(category), priority, baseCapacity, productionCost);
				resources.add(resource);
			}
		} catch (SQLException e) {
			logger.error("Error fetching resources for country: {}", countryName, e);
			throw new RuntimeException(e);
		}
		logger.debug("Successfully fetched {} resources for country: {}", resources.size(), countryName);
		return resources;
	}

	public void saveCountry(Country country) throws SQLException {
		logger.debug("Saving country: {}", country.getName());
		Connection conn = MariaDbConnection.getConnection();
		String sql =
				String.format("INSERT INTO country (name, money, population) VALUES ('%s', %f, %d)", country.getName(),
				              country.getMoney(), country.getPopulation());
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
			logger.debug("Successfully saved country: {}", country.getName());
		} catch (SQLException e) {
			logger.error("Error saving country: {}", country.getName(), e);
			throw new RuntimeException(e);
		}
	}

	public void updateCountry(Country country) throws SQLException {
		logger.debug("Updating country: {}", country.getName());
		Connection conn = MariaDbConnection.getConnection();
		String sql =
				String.format("UPDATE country SET money = %f, population = %d WHERE name = '%s'", country.getMoney(),
				              country.getPopulation(), country.getName());
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
			logger.debug("Successfully updated country: {}", country.getName());
		} catch (SQLException e) {
			logger.error("Error updating country: {}", country.getName(), e);
			throw new RuntimeException(e);
		}
	}

	public void deleteCountry(String name) throws SQLException {
		logger.debug("Deleting country: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("DELETE FROM country WHERE name = '%s'", name);
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
			logger.debug("Successfully deleted country: {}", name);
		} catch (SQLException e) {
			logger.error("Error deleting country: {}", name, e);
			throw new RuntimeException(e);
		}
	}

	public void deleteAllCountries() throws SQLException {
		logger.debug("Deleting all countries");
		Connection conn = MariaDbConnection.getConnection();
		String sql = "DELETE FROM country";
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
			logger.debug("Successfully deleted all countries");
		} catch (SQLException e) {
			logger.error("Error deleting all countries", e);
			throw new RuntimeException(e);
		}
	}
}