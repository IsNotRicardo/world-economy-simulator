package dao;

import datasource.MariaDbConnection;
import model.core.Resource;
import model.core.ResourceCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class ResourceDao {

	public void createResource(String name, int category, double priority, int baseCapacity, double productionCost)
			throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"INSERT INTO resource (name, category, priority, base_capacity, production_cost) VALUES ('%s', %d, %f, %d, %f)",
				name, category, priority, baseCapacity, productionCost);
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateResource(String name, int category, double priority, int baseCapacity, double productionCost)
			throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"UPDATE resource SET category = %d, priority = %f, base_capacity = %d, production_cost = %f WHERE name = '%s'",
				category, priority, baseCapacity, productionCost, name);
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteResource(String name) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("DELETE FROM resource WHERE name = '%s'", name);
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void getResource(String name) throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("SELECT * FROM resource WHERE name = '%s'", name);
		try {
			conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Resource> getAllResources() throws SQLException {
		Connection conn = MariaDbConnection.getConnection();
		String sql = "SELECT * FROM resource";
		List<Resource> resources = new ArrayList<>();

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
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
}
