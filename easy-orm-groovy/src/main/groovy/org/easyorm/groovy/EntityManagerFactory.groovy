package org.easyorm.groovy

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class EntityManagerFactory extends AbstractEntityManagerFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerFactory.class)

	@Override
	Properties loadConfiguration(source) {
		File file = (File) source

		Properties props = new Properties()

		LOGGER.info "Load the configuration file from ${file.canonicalPath}"

		if (!file.exists())
			throw new FileNotFoundException("The configuration file cannot be found.")

		props.load(new FileReader(file))

		return props
	}
}
