package org.easyorm.groovy.util

class IgnoreCaseHashMap extends HashMap<String, Object> {
	@Override
	Object get(Object key) {
		return super.get(((String) key).toLowerCase())
	}

	@Override
	Object put(String key, Object value) {
		return super.put(((String) key).toLowerCase(), value)
	}

}
