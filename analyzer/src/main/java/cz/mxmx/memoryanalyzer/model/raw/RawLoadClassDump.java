package cz.mxmx.memoryanalyzer.model.raw;

public class RawLoadClassDump {
	private final Long classObjectId;
	private final String className;

	public RawLoadClassDump(Long classObjectId, String className) {
		this.classObjectId = classObjectId;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public Long getClassObjectId() {
		return classObjectId;
	}
}
