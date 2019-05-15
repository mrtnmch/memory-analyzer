package cz.mxmx.memoryanalyzer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Instance dump
 */
public class InstanceDump extends GenericDump {
	private final ClassDump classDump;
	private final Map<InstanceFieldDump, Object> instanceFieldValues = new HashMap<>();

	/**
	 * Creates an instance dump.
	 * @param objectId Object ID.
	 * @param classDump Class of the instance.
	 */
	public InstanceDump(Long objectId, ClassDump classDump) {
		super(objectId);
		this.classDump = classDump;
	}

	public ClassDump getClassDump() {
		return classDump;
	}

	public Map<InstanceFieldDump, Object> getInstanceFieldValues() {
		return instanceFieldValues;
	}

	public void addInstanceField(InstanceFieldDump instanceFieldDump, Object value) {
		this.instanceFieldValues.put(instanceFieldDump, value);
	}

	@Override
	public String toString() {
		return "InstanceDump{" +
				"classDump=" + classDump.getName() +
				", instanceId=" + instanceId +
				'}';
	}
}
