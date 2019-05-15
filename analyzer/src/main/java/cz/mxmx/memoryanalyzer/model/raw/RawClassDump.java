package cz.mxmx.memoryanalyzer.model.raw;

import java.util.HashMap;
import java.util.Map;

/**
 * Class dump raw representation.
 */
public class RawClassDump {
	private final Long classObjectId;
	private final Long superClassObjectId;
	private final int instanceSize;
	private final Map<String, Object> staticFields = new HashMap<>();
	private final Map<String, String> instanceFields = new HashMap<>();
	private final Map<Integer, Object> constantFields = new HashMap<>();

	/**
	 * Creates a new raw class dump.
	 * @param classObjectId Object ID of the class object.
	 * @param superClassObjectId Super class object ID.
	 * @param instanceSize Number of instances of the class.
	 */
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

	/**
	 * Add a static field to the class.
	 * @param name Name of the field.
	 * @param value Value of the field.
	 */
	public void addStaticField(String name, Object value) {
		this.staticFields.put(name, value);
	}

	/**
	 * Add a instance field to the class.
	 * @param name Name of the field.
	 * @param type Type of the field.
	 */
	public void addInstanceField(String name, String type) {
		this.instanceFields.put(name, type);
	}

	/**
	 * Add a constant to the class (required by HPROF library).
	 * @param index Index of the constant.
	 * @param value Value of the constant.
	 */
	public void addConstantField(int index, Object value) {
		this.constantFields.put(index, value);
	}
}
