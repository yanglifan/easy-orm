package org.easyorm.groovy

import javax.persistence.Table
import javax.persistence.Column

class JPAEntityManager extends EntityManager {
	protected JPAEntityManager() {}

	public JPAEntityManager(url, username, password, driverClassName) {
		super(url, username, password, driverClassName)
	}

	@Override
	String getTableName(Object entity) {
		Table tableAnnotation = entity.getClass().getAnnotation(Table.class)
		if (tableAnnotation != null) {
			return tableAnnotation.name()
		} else {
			return entity.getClass().getSimpleName()
		}
	}

	@Override
	Map getFieldValuePairs(Object entity) {
		Map fieldValuePairs = [:]

		entity.properties.each { key, value ->
			if (!(key in RESERVED_PROPS)) {
				String columnName = key

				Column columnAnnotation = entity.getClass().getDeclaredField((String) key).
						getAnnotation(Column.class)
				if (columnAnnotation != null) {
					columnName = columnAnnotation.name()
				}
				fieldValuePairs.put(columnName, value)
			}
		}

		return fieldValuePairs
	}
}
