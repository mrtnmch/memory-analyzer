package cz.mxmx.memoryanalyzer.model.raw;

import java.util.HashMap;
import java.util.Map;

/**
 * Raw instance dump representing an instance.
 */
public class RawInstanceDump {
	private final Long objectId;
	private final Long objectClassId;
	private final Map<String, Object> instanceValues = new HashMap<>();

	/**
	 * Creates a new instance dump representation.
	 * @param objectId ID of the object.
	 * @param objectClassId ID of the class.
	 */
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

	/**
	 * Return field values.
	 * @return A map with format fieldName=value
	 */
	public Map<String, Object> getInstanceValues() {
		return instanceValues;
	}

	/**
	 * Add a new field value.
	 * @param name Name of the field.
	 * @param value Value of the field.
	 */
	public void addInstanceValue(String name, Object value) {
		this.instanceValues.put(name, value);
	}
}
