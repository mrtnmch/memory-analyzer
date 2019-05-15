package cz.mxmx.memoryanalyzer.model;

import java.util.List;

/**
 * Object-type array representation.
 */
public class InstanceArrayDump extends ArrayDump<Object> {
	private final ClassDump classDump;

	/**
	 * Creates an object-type array representation.
	 * @param objectId Object ID.
	 * @param classDump Dump of the item's class.
	 * @param values Values of the array.
	 */
	public InstanceArrayDump(Long objectId, ClassDump classDump, List<Object> values) {
		super(objectId, Object.class, values);
		this.classDump = classDump;
	}

	public ClassDump getClassDump() {
		return classDump;
	}
}
