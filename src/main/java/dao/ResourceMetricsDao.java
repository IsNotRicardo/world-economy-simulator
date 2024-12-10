package dao;

import entity.CountryEntity;
import entity.ResourceEntity;
import entity.ResourceMetricsEntity;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceMetricsDao {

	Logger logger = LoggerFactory.getLogger(ResourceMetricsDao.class);

	public void persist(ResourceMetricsEntity resourceMetrics) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(resourceMetrics);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource metrics", e);
			throw e;
		}
	}

	public List<ResourceMetricsEntity> findByResource(CountryEntity country, ResourceEntity resource) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery(
					         "SELECT r FROM ResourceMetricsEntity r WHERE r.country.id = :countryId AND r.resource.id = " +
					         ":resourceId", ResourceMetricsEntity.class).setParameter("countryId", country.getId())
			         .setParameter("resourceId", resource.getId()).getResultList();
		} catch (Exception e) {
			logger.error("Error finding resource metrics by resource", e);
			throw e;
		}
	}

	public List<ResourceMetricsEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT r FROM ResourceMetricsEntity r", ResourceMetricsEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all resource metrics", e);
			throw e;
		}
	}
}
