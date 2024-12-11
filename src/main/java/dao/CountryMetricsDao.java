package dao;

import entity.CountryEntity;
import entity.CountryMetricsEntity;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountryMetricsDao {

	Logger logger = LoggerFactory.getLogger(CountryMetricsDao.class);

	public void persist(CountryMetricsEntity countryMetrics) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			em.persist(countryMetrics);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting country metrics", e);
			throw e;
		}
	}

	public List<CountryMetricsEntity> findByCountry(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();

		try {
			return em.createQuery("SELECT c FROM CountryMetricsEntity c WHERE c.country.id = :countryId",
			                      CountryMetricsEntity.class).setParameter("countryId", country.getId())
			         .getResultList();

		} catch (Exception e) {
			logger.error("Error finding country metrics by country", e);
			throw e;
		}
	}

	public CountryMetricsEntity findByCountryAndDay(CountryEntity country, int day) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();

		try {
			return em.createQuery(
					         "SELECT c FROM CountryMetricsEntity c WHERE c.country.id = :countryId AND c.day = :day",
					         CountryMetricsEntity.class).setParameter("countryId", country.getId())
			         .setParameter("day", day).getSingleResult();
		} catch (Exception e) {
			logger.error("Error finding country metrics by country and day", e);
			throw e;
		}
	}

	public List<CountryMetricsEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT c FROM CountryMetricsEntity c", CountryMetricsEntity.class)
			         .getResultList();
		} catch (Exception e) {
			logger.error("Error finding all country metrics", e);
			throw e;
		}
	}

}
