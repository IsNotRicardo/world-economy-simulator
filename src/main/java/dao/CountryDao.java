package dao;

import datasource.MariaDbConnection;
import model.core.Country;
import model.core.Resource;
import model.core.ResourceCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;

public class CountryDao {

	String countryName;
	double money;
	int population;
	int count;

	public List<Country> getAllCountries() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "SELECT * FROM country";
		List<Country> countries = new ArrayList<>();

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				countryName = rs.getString("name");
				money = rs.getDouble("money");
				population = rs.getInt("population");
				Country country = new Country(countryName, money, population);
				countries.add(country);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return countries;
	}

	public Country getCountryByName(String name) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("SELECT * FROM country WHERE name = '%s'", name);
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				count++;
				int id = rs.getInt("id");
				countryName = rs.getString("name");
				money = rs.getDouble("money");
				population = rs.getInt("population");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if (count == 1) {
			return new Country(countryName, money, population);
		} else {
			throw new RuntimeException("More than one country with the same name found");
		}
	}

	public List<Resource> getResourcesByCountryName(String countryName) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"SELECT r.name, r.category, r.base_capacity, r.production_cost, r.priority " +
				"FROM resource r " +
				"JOIN country_resource cr ON r.id = cr.resource_id " +
				"JOIN country c ON cr.country_id = c.id " +
				"WHERE c.name = '%s'", countryName);
		List<Resource> resources = new ArrayList<>();

		try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			while (rs.next()) {
				String name = rs.getString("name");
				String category = rs.getString("category").toUpperCase();
				double priority = rs.getDouble("priority");
				int baseCapacity = rs.getInt("base_capacity");
				double productionCost = rs.getDouble("production_cost");
				Resource resource = new Resource(name, ResourceCategory.valueOf(category), priority, baseCapacity,
				                                 productionCost
				);
				resources.add(resource);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resources;
	}

	public void saveCountry(Country country) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql =
				String.format("INSERT INTO country (name, money, population) VALUES ('%s', %f, %d)", country.getName(),
				              country.getMoney(), country.getPopulation());
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateCountry(Country country) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql =
				String.format("UPDATE country SET money = %f, population = %d WHERE name = '%s'", country.getMoney(),
				              country.getPopulation(), country.getName());
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteCountry(String name) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("DELETE FROM country WHERE name = '%s'", name);
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteAllCountries() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "DELETE FROM country";
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
