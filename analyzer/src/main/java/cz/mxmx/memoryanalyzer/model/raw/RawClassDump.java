package cz.mxmx.memoryanalyzer.model.raw;

import java.util.HashMap;
import java.util.Map;

public class RawClassDump {
	private final Long classObjectId;
	private final Long superClassObjectId;
	private final int instanceSize;
	private final Map<String, Object> staticFields = new HashMap<>();
	private final Map<String, String> instanceFields = new HashMap<>();
	private final Map<Integer, Object> constantFields = new HashMap<>();

	public RawClassDump(Long classObjectId, Long superClassObjectId, int instanceSize) {
		this.classObjectId = classObjectId;
		this.superClassObjectId = superClassObjectId;
		this.instanceSize = instanceSize;
	}

	public Long getClassObjectId() {
		return classObjectId;
	}

	public Long getSuperClassObjectId() {
		return superClassObjectId;
	}

	public int getInstanceSize() {
		return instanceSize;
	}

	public Map<String, Object> getStaticFields() {
		return staticFields;
	}

	public Map<String, String> getInstanceFields() {
		return instanceFields;
	}

	public Map<Integer, Object> getConstantFields() {
		return constantFields;
	}

	public void addStaticField(String name, Object value) {
		this.staticFields.put(name, value);
	}

	public void addInstanceField(String name, String type) {
		this.instanceFields.put(name, type);
	}

	public void addConstantField(int index, Object value) {
		this.constantFields.put(index, value);
	}
}
