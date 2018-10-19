package cz.mxmx.memoryanalyzer.model;

import java.util.List;

public class InstanceArrayDump extends ArrayDump<Object> {
	private final ClassDump classDump;

	public InstanceArrayDump(Long objectId, ClassDump classDump, List<Object> values) {
		super(objectId, Object.class, values);
		this.classDump = classDump;
	}

	public ClassDump getClassDump() {
		return classDump;
	}
}
