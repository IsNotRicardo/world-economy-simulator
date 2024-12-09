package dao;

import entity.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	public void persist(Resource resource) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			Resource existingResource = findByName(resource.getName());
			if (existingResource != null) {
				if (existingResource.getPriority() != resource.getPriority()) {
					existingResource.setPriority(resource.getPriority());
				}
				if (existingResource.getBaseCapacity() != resource.getBaseCapacity()) {
					existingResource.setBaseCapacity(resource.getBaseCapacity());
				}
				if (existingResource.getProductionCost() != resource.getProductionCost()) {
					existingResource.setProductionCost(resource.getProductionCost());
				}
				em.merge(existingResource);
				logger.debug("Updated existing resource: {}", resource.getName());
			} else {
				em.persist(resource);
				logger.debug("Persisted new resource: {}", resource.getName());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource: {}", resource.getName(), e);
			throw e;
		}
	}

	public Resource findByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM Resource r WHERE r.name = :name", Resource.class)
			         .setParameter("name", name)
			         .getSingleResult();
		} catch (NoResultException e) {
			logger.debug("Resource not found: {}", name);
			return null;
		} catch (Exception e) {
			logger.error("Error finding resource by name: {}", name, e);
			throw e;
		}
	}

	public void deleteByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			em.createQuery("DELETE FROM Resource r WHERE r.name = :name")
			  .setParameter("name", name)
			  .executeUpdate();
			em.getTransaction().commit();
			logger.debug("Deleted resource by name: {}", name);
			} catch (PersistenceException e) {
			em.getTransaction().rollback();
			logger.error("Error deleting resource by name: {}", name, e);
			throw e;
		}
	}
}