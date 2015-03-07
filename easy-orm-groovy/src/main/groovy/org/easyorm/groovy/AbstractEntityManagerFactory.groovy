package org.easyorm.groovy

import org.slf4j.LoggerFactory
import org.slf4j.Logger

abstract class AbstractEntityManagerFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityManagerFactory.class)

	private static final CRUD_METHOD_NAMES = ['create', 'query', 'delete', 'update']

	private static final List DB_CONFIG_KEYS = ['db.url', 'db.driverClassName', 'db.username',
			'db.password']

	public EntityManager build(Object source) {
		Properties props = loadConfiguration(source)

		EntityManager entityManager = buildEntityManager(props)

		String entityClassNames = props['entityClasses']
		if (entityClassNames) {
			entityClassNames.split(',').each { className ->
				weaveEntityClass(entityManager, className)
			}
		}

		return entityManager
	}

	abstract Properties loadConfiguration(source)

	private EntityManager buildEntityManager(Properties props) {
		if (props['supportJPA']) {
			return new JPAEntityManager(props['db.url'], props['db.username'],
					props['db.password'], props['db.driverClassName'])
		} else {
			SimpleEntityManager simpleEntityManager = new SimpleEntityManager(props['db.url'],
					props['db.username'], props['db.password'], props['db.driverClassName'])
			weaveMapClass(simpleEntityManager)
			return simpleEntityManager
		}
	}

	void weaveEntityClass(EntityManager entityManager, String className) {
		Class entityClass = Class.forName(className)

		CRUD_METHOD_NAMES.each {methodName ->
			entityClass.metaClass."$methodName" = {->
				entityManager."$methodName"(delegate)
			}
		}
	}

	private void weaveMapClass(SimpleEntityManager simpleEntityManager) {
		CRUD_METHOD_NAMES.each {methodName ->
			Map.metaClass."$methodName" = {->
				simpleEntityManager."$methodName"(delegate)
			}
		}
	}
}
