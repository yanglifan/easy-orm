package org.easyorm.groovy

import org.junit.Test

class MySQLIntegrationTest {
	@Test
	void test() {
		EntityManager entityManager = new SimpleEntityManager("jdbc:mysql://localhost/my_db",
				"root", "", "com.mysql.jdbc.Driver")
		entityManager.create(new ABean())

		println entityManager.query(new ABean())
	}
}
