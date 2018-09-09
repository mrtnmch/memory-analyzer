package cz.mxmx.memoryanalyzer.model.raw;

import java.util.HashMap;
import java.util.Map;

public class RawInstanceDump {
	private final Long objectId;
	private final Long objectClassId;
	private final Map<String, Object> instanceValues = new HashMap<>();

	public RawInstanceDump(Long objectId, Long objectClassId) {
		this.objectId = objectId;
		this.objectClassId = objectClassId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public Long getObjectClassId() {
		return objectClassId;
	}

	public Map<String, Object> getInstanceValues() {
		return instanceValues;
	}

	public void addInstanceValue(String name, Object value) {
		this.instanceValues.put(name, value);
	}
}
