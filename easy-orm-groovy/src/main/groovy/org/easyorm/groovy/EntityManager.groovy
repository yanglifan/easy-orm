package org.easyorm.groovy

import static java.sql.Types.*

import java.sql.Timestamp

import groovy.sql.GroovyResultSet
import groovy.sql.Sql

import org.slf4j.LoggerFactory

import org.easyorm.groovy.util.IgnoreCaseHashMap

public abstract class EntityManager {

	protected static final String[] RESERVED_PROPS = ['metaClass', 'class'] as String[]

	private logger = LoggerFactory.getLogger(EntityManager.class)

	private Sql sql

	private boolean enablePrintSql = true

	protected EntityManager() {}

	public EntityManager(String url, String username, String password, String driverClassName) {
		sql = Sql.newInstance(url, username, password, driverClassName)
	}

	public void close() {
		sql.close()
	}

	public List query(entity) {
		Map props = getFieldValuePairs(entity)

		StringBuilder sqlBuilder = new StringBuilder("select * from ") << getTableName(entity)
		String stmt = buildSqlWithWhereClause(props, sqlBuilder)

		printSql stmt

		def results = []
		sql.eachRow(stmt) { GroovyResultSet row ->
			Map result = new IgnoreCaseHashMap()
			for (int i = 1; i <= row.getMetaData().columnCount; i++) {
				int columnType = row.getMetaData().getColumnType(i)
				String columnLabel = row.getMetaData().getColumnLabel(i)
				result."$columnLabel" = toJavaValue(row.getObject(i), columnType)
			}
			results << result
		}

		return results
	}

	public void create(entity) {
		StringBuilder stmtBuilder = new StringBuilder("insert into ")
		stmtBuilder << getTableName(entity) << ' ('

		Map fieldValuePairs = getFieldValuePairs(entity)

		fieldValuePairs.keySet().eachWithIndex { field, index ->
			if (fieldValuePairs.get(field) != null) {
				stmtBuilder << field
				addCommaIfNecessary(index, fieldValuePairs.size(), stmtBuilder)
			}
		}

		stmtBuilder << ') values ('

		fieldValuePairs.values().eachWithIndex { value, index ->
			if (value != null) {
				stmtBuilder << toSqlValue(value)
				addCommaIfNecessary(index, fieldValuePairs.size(), stmtBuilder)
			}
		}

		stmtBuilder << ')'

		String sqlStmt = stmtBuilder.toString()
		printSql sqlStmt

		sql.execute sqlStmt
	}

	public void update(entity) {

	}

	public void delete(entity) {
		StringBuilder sqlBuilder = new StringBuilder("delete from ") << getTableName(entity)
		String sqlStmt = buildSqlWithWhereClause(getFieldValuePairs(entity), sqlBuilder)
		printSql(sqlStmt)
		sql.execute(sqlStmt)
	}

	abstract String getTableName(Object entity)

	abstract Map getFieldValuePairs(Object entity)

	protected void addCommaIfNecessary(int index, int size, StringBuilder stringBuilder) {
		if (index < size - 1) stringBuilder << ', '
	}

	protected String toSqlValue(value) {
		switch (value.getClass().getName()) {
			case "java.lang.String":
				return "\'$value\'"
			case "java.util.Date":
				String dateString = ((Date) value).format("yyyy-MM-dd hh:mm:ss")
				return "\'$dateString\'"
			case "java.sql.Timestamp":
				return Long.toString(((Timestamp) value).time)
			case "java.lang.Boolean":
				// Currently only support MySQL
				return value ? 1 : 0
			default:

				return value.toString()
		}
	}

	Object toJavaValue(Object sqlValue, int sqlType) {
		switch (sqlType) {
			case TINYINT:
				if (sqlValue == 1) {
					return Boolean.TRUE
				} else if (sqlValue == 0) {
					return Boolean.FALSE
				}
			case DATE:
				return (Date) sqlValue
			default:
				return sqlValue
		}
	}

	private String buildSqlWithWhereClause(Map props, StringBuilder sqlBuilder) {
		sqlBuilder << ' where 1=1'

		props.each { key, value ->
			if (value != null && !(key in RESERVED_PROPS)) {
				sqlBuilder << ' and ' << key << ' = ' << toSqlValue(value)
			}
		}
		return sqlBuilder.toString()
	}

	private void printSql(String sql) {
		if (enablePrintSql && logger.isInfoEnabled()) {
			logger.info("SQL Statement: " + sql)
		}
	}

	public Sql getSql() {
		return sql
	}

	public void setLogger(logger) {
		this.logger = logger
	}
}
