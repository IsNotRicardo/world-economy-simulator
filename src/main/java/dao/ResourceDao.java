package dao;

import entity.ResourceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	public void persist(ResourceEntity resourceEntity) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			ResourceEntity existingResourceEntity = findByName(resourceEntity.getName());
			if (existingResourceEntity != null) {
				if (existingResourceEntity.getPriority() != resourceEntity.getPriority()) {
					existingResourceEntity.setPriority(resourceEntity.getPriority());
				}
				if (existingResourceEntity.getBaseCapacity() != resourceEntity.getBaseCapacity()) {
					existingResourceEntity.setBaseCapacity(resourceEntity.getBaseCapacity());
				}
				if (existingResourceEntity.getProductionCost() != resourceEntity.getProductionCost()) {
					existingResourceEntity.setProductionCost(resourceEntity.getProductionCost());
				}
				em.merge(existingResourceEntity);
				logger.debug("Updated existing resource: {}", resourceEntity.getName());
			} else {
				em.persist(resourceEntity);
				logger.debug("Persisted new resource: {}", resourceEntity.getName());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource: {}", resourceEntity.getName(), e);
			throw e;
		}
	}

	public ResourceEntity findByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceEntity r WHERE r.name = :name", ResourceEntity.class)
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
			em.createQuery("DELETE FROM ResourceEntity r WHERE r.name = :name")
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