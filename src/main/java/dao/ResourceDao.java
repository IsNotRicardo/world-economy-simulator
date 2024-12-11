package dao;


import entity.ResourceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	public void persist(ResourceEntity resourceEntity) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.merge(resourceEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource: {}", resourceEntity.getName(), e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
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
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public void deleteByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			ResourceEntity resourceEntity = findByName(name);
			if (resourceEntity != null) {
				em.remove(em.contains(resourceEntity) ? resourceEntity : em.merge(resourceEntity));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting resource by name: {}", name, e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public List<ResourceEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceEntity r", ResourceEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all resources", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}