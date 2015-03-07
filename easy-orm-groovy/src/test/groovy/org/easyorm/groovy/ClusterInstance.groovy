package org.easyorm.groovy

import javax.persistence.Column

public class ClusterInstance {
	String instanceId = "001"
	@Column(name = "isCrashed")
	boolean crashed
}
