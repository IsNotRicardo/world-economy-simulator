package dao;

import entity.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceDao {

	private static final Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	public void persist(Resource resource) {
		EntityManager em = datasource.MariaDbConnection.getEntityManager();
		em.getTransaction().begin();
		try {
			Resource existingResource = em.createQuery("SELECT r FROM Resource r WHERE r.name = :name", Resource.class)
			                              .setParameter("name", resource.getName())
			                              .getSingleResult();
			existingResource.setPriority(resource.getPriority());
			existingResource.setBaseCapacity(resource.getBaseCapacity());
			existingResource.setProductionCost(resource.getProductionCost());
			em.merge(existingResource);
			logger.debug("Updated existing resource: {}", resource.getName());
		} catch (NoResultException e) {
			em.persist(resource);
			logger.debug("Persisted new resource: {}", resource.getName());
		} catch (SQLGrammarException e) {
			em.getTransaction().rollback();
			logger.error("SQL grammar error persisting resource: {}", resource.getName(), e);
			throw new PersistenceException("SQL grammar error", e);
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("Error persisting resource: {}", resource.getName(), e);
			throw e;
		}
		em.getTransaction().commit();
	}
}