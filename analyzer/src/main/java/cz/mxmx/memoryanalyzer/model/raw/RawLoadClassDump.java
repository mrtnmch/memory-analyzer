package cz.mxmx.memoryanalyzer.model.raw;

/**
 * Raw "load class" type.
 */
public class RawLoadClassDump {
	private final Long classObjectId;
	private final String className;
	private final int classSerialNum;

	/**
	 * Creates a new load class raw type.
	 * @param classObjectId Object ID of the class.
	 * @param className Class name.
	 * @param classSerialNum Class serial number.
	 */
	public RawLoadClassDump(Long classObjectId, String className, int classSerialNum) {
		this.classObjectId = classObjectId;
		this.className = className;
		this.classSerialNum = classSerialNum;
	}

	public String getClassName() {
		return className;
	}

	public Long getClassObjectId() {
		return classObjectId;
	}

	public int getClassSerialNum() {
		return classSerialNum;
	}
}
