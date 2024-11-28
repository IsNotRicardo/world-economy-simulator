package dao;

import datasource.MariaDbConnection;
import model.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {

	public List<Country> getAllCountries() {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "SELECT * FROM country";
		List<Country> countries = new ArrayList<Country>();

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return countries;
	}

}
