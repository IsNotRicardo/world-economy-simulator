package dao;

import entity.CountryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountryDao {

	private static final Logger logger = LoggerFactory.getLogger(CountryDao.class);

	public void persist(CountryEntity country) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			CountryEntity existingCountry = findByName(country.getName());
			if (existingCountry != null) {
				// Update existing country
				existingCountry.setMoney(country.getMoney());
				existingCountry.setPopulation(country.getPopulation());
				em.merge(existingCountry);
			} else {
				em.persist(country);
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting country: {}", country.getName(), e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
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
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public List<CountryEntity> findAll() {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		try {
			return em.createQuery("SELECT c FROM CountryEntity c", CountryEntity.class).getResultList();
		} catch (Exception e) {
			logger.error("Error finding all countries", e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public void deleteByName (String name) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			CountryEntity countryEntity = findByName(name);
			if (countryEntity != null) {
				em.remove(em.contains(countryEntity) ? countryEntity : em.merge(countryEntity));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error deleting country by name: {}", name, e);
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}