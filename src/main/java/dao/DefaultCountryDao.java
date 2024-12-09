package dao;

import entity.CountryEntity;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultCountryDao {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCountryDao.class);

	public List<CountryEntity> findAll() throws SQLException {
		Connection conn = datasource.MariaDbConnection.getConnection();
		String sql = "SELECT * from default_country";
		String countryName = null;
		double money = 0;
		long population = 0;
		List<CountryEntity> countries = new ArrayList<>();

		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)
		) {
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