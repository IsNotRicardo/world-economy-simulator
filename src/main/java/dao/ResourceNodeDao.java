package dao;

import datasource.MariaDbConnection;
import entity.CountryEntity;
import entity.ResourceNodeEntity;
import entity.ResourceNodeEntityId;
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
			// Use merge for both update and insert scenarios
			em.merge(resourceNode);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource node", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public List<ResourceNodeEntity> findByCountry(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery(
					"SELECT r FROM ResourceNodeEntity r WHERE r.country.id = :countryId",
					ResourceNodeEntity.class
			).setParameter("countryId", country.getId()).getResultList();
		} catch (Exception e) {
			logger.error("Error finding resource nodes by country", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public List<ResourceNodeEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceNodeEntity r", ResourceNodeEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all resource nodes", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public void delete(Long countryId, Long resourceId) {
		EntityManager em = MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			ResourceNodeEntityId id = new ResourceNodeEntityId();
			id.setCountryId(countryId);
			id.setResourceId(resourceId);
			ResourceNodeEntity resourceNodeEntity = em.find(ResourceNodeEntity.class, id);
			if (resourceNodeEntity != null) {
				em.remove(em.contains(resourceNodeEntity) ? resourceNodeEntity : em.merge(resourceNodeEntity));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting resource node by country and resource", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}

	}
}