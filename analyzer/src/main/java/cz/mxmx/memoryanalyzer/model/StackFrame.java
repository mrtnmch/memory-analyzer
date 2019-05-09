package cz.mxmx.memoryanalyzer.model;

public class StackFrame {
	private final long stackFrameId;
	private final String methodNameStringId;
	private final String methodSigStringId;
	private final String sourceFileNameStringId;
	private final ClassDump classDump;
	private final int location;

	public StackFrame(long stackFrameId, String methodNameStringId, String methodSigStringId, String sourceFileNameStringId, ClassDump classDump, int location) {
		this.stackFrameId = stackFrameId;
		this.methodNameStringId = methodNameStringId;
		this.methodSigStringId = methodSigStringId;
		this.sourceFileNameStringId = sourceFileNameStringId;
		this.classDump = classDump;
		this.location = location;
	}

	public long getStackFrameId() {
		return stackFrameId;
	}

	public String getMethodNameStringId() {
		return methodNameStringId;
	}

	public String getMethodSigStringId() {
		return methodSigStringId;
	}

	public String getSourceFileNameStringId() {
		return sourceFileNameStringId;
	}

	public ClassDump getClassDump() {
		return classDump;
	}

	public int getLocation() {
		return location;
	}
}
