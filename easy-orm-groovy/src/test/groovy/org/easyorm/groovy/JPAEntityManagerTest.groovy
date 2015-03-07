package org.easyorm.groovy

import static org.junit.Assert.*

import org.junit.Test

class JPAEntityManagerTest {
	JPAEntityManager jpaEntityManager = new JPAEntityManager()

	@Test
	void getTableName_with_annotation() {
		assert "t_user" == jpaEntityManager.getTableName(new User())
	}

	@Test
	void getTableName_without_annotation() {
		assert "ABean" == jpaEntityManager.getTableName(new ABean())
	}

	@Test
	void getFieldValuePairs_test() {
		Map fieldValuePairs = jpaEntityManager.getFieldValuePairs(new ClusterInstance())
		assert "001" == fieldValuePairs['instanceId']
		assertFalse((Boolean) fieldValuePairs['isCrashed'])
	}
}
