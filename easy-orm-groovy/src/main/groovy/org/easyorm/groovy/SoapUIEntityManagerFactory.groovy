package org.easyorm.groovy

class SoapUIEntityManagerFactory extends AbstractEntityManagerFactory {
	private static final Map SOAPUI_PROPS_MAPPING = [
			'db.url': '${#Project#DbUrl}',
			'db.driverClassName': '${#Project#JdbcDriver}',
			'db.username': '${#Project#DbUsername}',
			'db.password': '${#Project#DbPassword}'
	]

	private static final String DRIVER_CLASS_NAME = '${#Project#JdbcDriver}'
	private static final String DB_USERNAME = '${#Project#DbUsername}'
	private static final String DB_PASSWORD = '${#Project#DbPassword}'

	@Override
	Properties loadConfiguration(Object source) {
		Properties properties = new Properties()
		SOAPUI_PROPS_MAPPING.each {key, value ->
			properties.put(key, source.expand(value))
		}
		return properties
	}

}
