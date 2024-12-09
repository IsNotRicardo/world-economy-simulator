package dao;

import entity.CountryResource;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountryResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(CountryResourceDao.class);

	public void persist(CountryResource countryResource) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(countryResource);
			em.getTransaction().commit();
			logger.debug("Persisted new country resource: " + countryResource);
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting country resource: " + countryResource);
			throw e;
		}
	}

	public List<CountryResource> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT cr FROM CountryResource cr", CountryResource.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all country resources", e);
			throw e;
		}
	}

	public void delete(CountryResource countryResource) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.remove(countryResource);
			em.getTransaction().commit();
			logger.debug("Deleted country resource");
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting country resource", e);
			throw e;
		}
	}
}
