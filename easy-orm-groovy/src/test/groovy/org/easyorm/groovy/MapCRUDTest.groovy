package org.easyorm.groovy

import static org.junit.Assert.*

import org.junit.Test
import org.junit.Before
import org.junit.After

public class MapCRUDTest {
	private static final String USER_TABLE = 'User'

	private EntityManager entityManager

	@Before
	void setUp() {
		String createTableStmt = '''
CREATE TABLE User
(
	username varchar(256) NOT NULL,
	age int,
	createAt date NOT NULL,
	login tinyint
);
'''
		File configuration = new File(getClass().getResource("/easy-orm.properties").file)
		entityManager = new EntityManagerFactory().build(configuration)
		entityManager.getSql().execute(createTableStmt)
	}

	@Test
	void should_nothing_when_table_empty() {
		assertEquals 0, [Table: USER_TABLE].query().size()
	}

	@Test
	void test_create_and_query() {
		[Table: USER_TABLE, username: 'tom', age: 30, createAt: new Date()].create()

		def map = [Table: USER_TABLE, username: 'john', age: 20, createAt: new Date()]
		map.create()

		assert 1 == map.query().size()
		assert 2 == [Table: USER_TABLE].query().size()
	}

	@Test
	void should_nothing_after_delete() {
		Map map = [Table: USER_TABLE, username: 'tom', age: 30, createAt: new Date(), login: 1]
		map.create()

		List results = map.query()
		assertEquals 1, results.size()
		println results[0]
		assertEquals 'tom', results[0].username
		assertEquals 30, results[0].age
		assertTrue results[0].login

		map.delete()
		assertEquals 0, map.query().size()
	}

	@After
	void tearDown() {
		entityManager.getSql().execute("DROP TABLE User")
	}
}
