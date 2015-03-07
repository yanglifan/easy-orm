package org.easyorm.groovy

import javax.persistence.Table

@Table(name = "t_user")
class User {
	String username
	int age
	Date createAt = new Date()
}
