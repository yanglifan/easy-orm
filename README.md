About Easy ORM on Groovy
========================

Easy ORM on Groovy (EOG for short) is an easy-to-use ORM framework based on Groovy. Unlike Hibernate and GORM (based on Hibernate and Spring), EOG only provides the basic CRUD function.

How to use
-------------

Add a properties file, the content like following. The file name is easy-orm.properties for example.

	db.url=jdbc:hsqldb:file:testdb
	db.driverClassName=org.hsqldb.jdbcDriver
	db.username=sa
	db.password=

	entityClasses=org.easyorm.groovy.User,org.easyorm.groovy.Group

In your code

	EntityManagerFactory.build("/easy-orm.properties")

	// Create
	User user = new User(username: "John", age: 20)
	user.create()

	// Search
	Collection results = User.find(user)

User is your entity class. It is just a POJO. You do not need to implement CRUD methods in your entity classes. When invoke EntityManagerFactory.build(), CRUD method will be add into your entity classes automatically.