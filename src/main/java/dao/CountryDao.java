package dao;

import entity.CountryEntity;
import entity.ResourceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class CountryDao {

	private static final Logger logger = LoggerFactory.getLogger(CountryDao.class);

	public void persist(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			CountryEntity existingCountry = findByName(country.getName());
			if (existingCountry != null && Objects.equals(existingCountry.getName(), country.getName())) {
				if (existingCountry.getMoney() != country.getMoney()) {
					existingCountry.setMoney(country.getMoney());
				}
				if (existingCountry.getPopulation() != country.getPopulation()) {
					existingCountry.setPopulation(country.getPopulation());
				}
				em.merge(existingCountry);
				logger.debug("Updated existing country: {}", country.getName());
			} else {
				em.persist(country);
				logger.debug("Persisted new country: {}", country.getName());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting country: {}", country.getName(), e);
			throw e;
		}
	}

	public CountryEntity findByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT c FROM CountryEntity c WHERE c.name = :name", CountryEntity.class)
			         .setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			logger.debug("Country not found: {}", name);
			return null;
		} catch (Exception e) {
			logger.error("Error finding country by name: {}", name, e);
			throw e;
		}
	}

	public List<CountryEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT c FROM CountryEntity c", CountryEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all countries", e);
			throw e;
		}
	}

	public List<ResourceEntity> findResourcesByCountryName(String countryName) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery(
					         "SELECT cr.resource FROM CountryEntity c JOIN c.countryResources cr WHERE c.name = :name",
					         ResourceEntity.class)
			         .setParameter("name", countryName)
			         .getResultList();
		} catch (Exception e) {
			logger.error("Error finding resources by country name: {}", countryName, e);
			throw e;
		}
	}

	public void delete(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			if (!em.contains(country)) {
				country = em.merge(country);
			}
			em.remove(country);
			em.getTransaction().commit();
			logger.debug("Deleted country: {}", country.getName());
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting country: {}", country.getName(), e);
			throw e;
		}
	}

	public void deleteByName(String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.createQuery("DELETE FROM CountryEntity c WHERE c.name = :name").setParameter("name", name)
			  .executeUpdate();
			em.getTransaction().commit();
			logger.debug("Deleted country by name: {}", name);
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting country by name: {}", name, e);
			throw e;
		}
	}

	public void deleteAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			em.createQuery("DELETE FROM CountryEntity").executeUpdate();
			em.getTransaction().commit();
			logger.debug("Deleted all countries");
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting all countries", e);
			throw e;
		}
	}
}