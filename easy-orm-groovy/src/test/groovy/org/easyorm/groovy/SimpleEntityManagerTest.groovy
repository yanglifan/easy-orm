package org.easyorm.groovy

import static org.junit.Assert.*

import java.sql.Timestamp

import org.junit.Test

public class SimpleEntityManagerTest {

	@Test
	void should_return_table_name_from_a_map() {
		assert "t_user" == new SimpleEntityManager().getTableName([Table: "t_user"])
	}

	@Test
	void should_return_column_map() {
		Map entity = [Table: 't_user', PrimaryKeys: ['username'], username: 'john',
				password: 'pass']
		Map fieldValuePairs = new SimpleEntityManager().getFieldValuePairs(entity)
		assertNull fieldValuePairs.Table
		assertNull fieldValuePairs.PrimaryKeys
		assertEquals 'john', fieldValuePairs.username
		assertEquals 'pass', fieldValuePairs.password
	}

	@Test
	void test_type_convert() {
		EntityManager entityManager = new SimpleEntityManager()
		assert "1000000" == entityManager.toSqlValue(new Timestamp(1000000))
		assert "1" == entityManager.toSqlValue(true)
		assert "0" == entityManager.toSqlValue(false)
	}
}
