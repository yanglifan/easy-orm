package org.easyorm.groovy

import java.text.SimpleDateFormat

class ABean {
	String stringField = "string"
	int intField = 10
	Date dateField
	String nullField

	public ABean() {
		dateField = new SimpleDateFormat("yyyy-MM-dd").parse("2011-11-11")
	}
}
