package cz.mxmx.memoryanalyzer.model.raw;

public class RawStackFrame {
	private final long stackFrameId;
	private final long methodNameStringId;
	private final long methodSigStringId;
	private final long sourceFileNameStringId;
	private final int classSerialNum;
	private final int location;

	public RawStackFrame(long stackFrameId, long methodNameStringId, long methodSigStringId, long sourceFileNameStringId, int classSerialNum, int location) {
		this.stackFrameId = stackFrameId;
		this.methodNameStringId = methodNameStringId;
		this.methodSigStringId = methodSigStringId;
		this.sourceFileNameStringId = sourceFileNameStringId;
		this.classSerialNum = classSerialNum;
		this.location = location;
	}

	public long getStackFrameId() {
		return stackFrameId;
	}

	public long getMethodNameStringId() {
		return methodNameStringId;
	}

	public long getMethodSigStringId() {
		return methodSigStringId;
	}

	public long getSourceFileNameStringId() {
		return sourceFileNameStringId;
	}

	public int getClassSerialNum() {
		return classSerialNum;
	}

	public int getLocation() {
		return location;
	}
}
