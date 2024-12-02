package dao;

import datasource.MariaDbConnection;
import model.core.Country;

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

	public List<Country> getAllCountries() {
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

	public Country getCountryByName(String name) {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "SELECT * FROM country WHERE name = '" + name + "'";
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

	public void saveCountry(Country country) {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "INSERT INTO country (name, money, population) VALUES ('" + country.getName() + "', " +
		             country.getMoney() + ", " + country.getPopulation() + ")";
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateCountry(Country country) {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "UPDATE country SET money = " + country.getMoney() + ", population = " + country.getPopulation() +
		             " WHERE name = '" + country.getName() + "'";
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteCountry(String name) {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "DELETE FROM country WHERE name = '" + name + "'";
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteAllCountries() {
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
