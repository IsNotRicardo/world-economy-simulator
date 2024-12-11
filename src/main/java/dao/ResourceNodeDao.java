package dao;

import entity.CountryEntity;
import entity.ResourceNodeEntity;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceNodeDao {

	Logger logger = LoggerFactory.getLogger(ResourceNodeDao.class);

	public void persist(ResourceNodeEntity resourceNode) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(resourceNode);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource node", e);
			throw e;
		}
	}

	public List<ResourceNodeEntity> findByCountry(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceNodeEntity r WHERE r.country.id = :countryId",
			                      ResourceNodeEntity.class)
			         .setParameter("countryId", country.getId()).getResultList();
		} catch (Exception e) {
			logger.error("Error finding resource nodes by country", e);
			throw e;
		}
	}

	public void deleteAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.createQuery("DELETE FROM ResourceNodeEntity").executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting all resource nodes", e);
			throw e;
		}
	}

	public List<ResourceNodeEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceNodeEntity r", ResourceNodeEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all resource nodes", e);
			throw e;
		}
	}
}
