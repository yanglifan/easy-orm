package org.easyorm.groovy

public class SimpleEntityManager extends EntityManager {

	private static final String[] NON_COLUMN_KEYS = ['Table', 'PrimaryKeys']

	public SimpleEntityManager() {}

	public SimpleEntityManager(url, username, password, driverClassName) {
		super(url, username, password, driverClassName)
	}

	String getTableName(Object entity) {
		if (entity instanceof Map)
			return getTableNameFromMap((Map) entity)
		else
			return entity.class.getSimpleName()
	}

	Map getFieldValuePairs(Object entity) {
		if (entity instanceof Map) {
			return getFieldValuePairsFromMap((Map) entity)
		} else {
			return getFieldValuePairsFromObj(entity)
		}
	}

	private TreeMap getFieldValuePairsFromObj(entity) {
		SortedMap fieldValues = new TreeMap()
		entity.properties.each {key, value ->
			if (!(key in RESERVED_PROPS)) {
				fieldValues.put(key, value)
			}
		}
		return fieldValues
	}

	private String getTableNameFromMap(Map mapEntity) {
		String tableName = mapEntity.Table
		if (tableName == null) {
			throw new IllegalArgumentException("Please provide the table name.")
		}
		return tableName
	}

	private Map getFieldValuePairsFromMap(Map mapEntity) {
		return mapEntity - [Table: mapEntity.Table, PrimaryKeys: mapEntity.PrimaryKeys]
	}
}
