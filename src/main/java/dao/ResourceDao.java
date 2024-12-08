package dao;

import datasource.MariaDbConnection;
import model.core.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	String name;
	String category;
	double priority;
	int baseCapacity;
	double productionCost;
	int count;

	public void createResource(String name, int category, double priority, int baseCapacity, double productionCost)
			throws SQLException {
		logger.debug("Creating resource: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"INSERT INTO resource (name, category, priority, base_capacity, production_cost) VALUES ('%s', %d, %f, %d, %f)",
				name, category, priority, baseCapacity, productionCost);
		try {
			conn.createStatement().executeUpdate(sql);
			logger.debug("Successfully created resource: {}", name);
		} catch (SQLException e) {
			logger.error("Failed to create resource: {}", name, e);
			throw new RuntimeException(e);
		}
	}

	public void updateResource(String name, int category, double priority, int baseCapacity, double productionCost)
			throws SQLException {
		logger.debug("Updating resource: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format(
				"UPDATE resource SET category = %d, priority = %f, base_capacity = %d, production_cost = %f WHERE name = '%s'",
				category, priority, baseCapacity, productionCost, name);
		try {
			conn.createStatement().executeUpdate(sql);
			logger.debug("Successfully updated resource: {}", name);
		} catch (SQLException e) {
			logger.error("Failed to update resource: {}", name, e);
			throw new RuntimeException(e);
		}
	}

	public void deleteResource(String name) throws SQLException {
		logger.debug("Deleting resource: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("DELETE FROM resource WHERE name = '%s'", name);
		try {
			conn.createStatement().executeUpdate(sql);
			logger.debug("Successfully deleted resource: {}", name);
		} catch (SQLException e) {
			logger.error("Failed to delete resource: {}", name, e);
			throw new RuntimeException(e);
		}
	}

	public Resource getResource(String name) throws SQLException {
		logger.debug("Fetching resource: {}", name);
		Connection conn = MariaDbConnection.getConnection();
		String sql = String.format("SELECT * FROM resource WHERE name = '%s'", name);
		int count = 0;
		String category = null;
		double priority = 0;
		int baseCapacity = 0;
		double productionCost = 0;

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				count++;
				category = rs.getString("category").toUpperCase();
				priority = rs.getDouble("priority");
				baseCapacity = rs.getInt("base_capacity");
				productionCost = rs.getDouble("production_cost");
			}
		} catch (SQLException e) {
			logger.error("Failed to get resource: {}", name, e);
			throw new RuntimeException(e);
		}

		if (count == 1) {
			logger.debug("Resource found: {}", name);
			return new Resource(name, priority, baseCapacity, productionCost);
		} else {
			logger.debug("Resource not found: {}", name);
			return null;
		}
	}

	public List<Resource> getAllResources() throws SQLException {
		logger.debug("Fetching all resources");
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
				Resource resource = new Resource(name, priority, baseCapacity,
				                                 productionCost);
				resources.add(resource);
			}
		} catch (SQLException e) {
			logger.error("Failed to get all resources", e);
			throw new RuntimeException(e);
		}
		logger.debug("Successfully fetched {} resources", resources.size());
		return resources;
	}
}
