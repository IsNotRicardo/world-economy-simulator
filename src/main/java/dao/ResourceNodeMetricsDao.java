package dao;

import entity.CountryEntity;
import entity.ResourceEntity;
import entity.ResourceNodeMetricsEntity;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceNodeMetricsDao {

	Logger logger = LoggerFactory.getLogger(ResourceNodeMetricsDao.class);

	public void persist(ResourceNodeMetricsEntity resourceNodeMetrics) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(resourceNodeMetrics);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource node metrics", e);
			throw e;
		}
	}

	public List<ResourceNodeMetricsEntity> findByResourceNode(CountryEntity countryEntity,
	                                                          ResourceEntity resourceEntity) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceNodeMetricsEntity r WHERE r.country.id = :countryId AND r" +
			                      ".resource.id = :resourceId", ResourceNodeMetricsEntity.class)
			         .setParameter("countryId", countryEntity.getId())
			         .setParameter("resourceId", resourceEntity.getId()).getResultList();
		} catch (Exception e) {
			logger.error("Error finding resource node metrics by resource node", e);
			throw e;
		}
	}

	public List<ResourceNodeMetricsEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceNodeMetricsEntity r", ResourceNodeMetricsEntity.class)
			         .getResultList();
		} catch (Exception e) {
			logger.error("Error finding all resource node metrics", e);
			throw e;
		}
	}
}
