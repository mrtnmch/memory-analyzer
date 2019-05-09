package cz.mxmx.memoryanalyzer.model.raw;

public class RawLoadClassDump {
	private final Long classObjectId;
	private final String className;
	private final int classSerialNum;

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
