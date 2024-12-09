package dao;

import entity.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(DefaultResourceDao.class);

	public List<Resource> findAll() throws SQLException {
		Connection conn = datasource.MariaDbConnection.getConnection();
		String sql = "SELECT * from default_resource";
		String name = null;
		double priority = 0;
		int baseCapacity = 0;
		double productionCost = 0;
		List<Resource> resources = new ArrayList<>();

		try (Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql);
		) {
			while (rs.next()) {
				name = rs.getString("name");
				priority = rs.getDouble("priority");
				baseCapacity = rs.getInt("base_capacity");
				productionCost = rs.getDouble("production_cost");
				resources.add(new Resource(name, priority, baseCapacity, productionCost));
			}
		} catch (SQLException e) {
			logger.error("Error finding all resources", e);
			throw e;
		}
		return resources;
	}

	public Resource findByName(String name) throws SQLException {
		Connection conn = datasource.MariaDbConnection.getConnection();
		String sql = "SELECT * from default_resource where name = ?";
		String resourceName = null;
		double priority = 0;
		int baseCapacity = 0;
		double productionCost = 0;
		int count = 0;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				count++;
				resourceName = rs.getString("name");
				priority = rs.getDouble("priority");
				baseCapacity = rs.getInt("base_capacity");
				productionCost = rs.getDouble("production_cost");
			}
		} catch (SQLException e) {
			logger.error("Error finding resource by name: {}", name, e);
			throw e;
		} catch (NoResultException e) {
			logger.debug("Resource not found: {}", name);
			return null;
		}
		if (count == 1) {
			return new Resource(resourceName, priority, baseCapacity, productionCost);
		} else {
			return null;
		}
	}

}