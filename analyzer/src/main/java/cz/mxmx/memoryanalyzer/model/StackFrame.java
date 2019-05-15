package cz.mxmx.memoryanalyzer.model;

/**
 * Processed stack frame.
 */
public class StackFrame {
	private final long stackFrameId;
	private final String methodNameStringId;
	private final String methodSigStringId;
	private final String sourceFileNameStringId;
	private final ClassDump classDump;
	private final int location;

	/**
	 * Creates a stack frame representation.
	 * @param stackFrameId Stack frame ID.
	 * @param methodNameString Method name.
	 * @param methodSigString Method signature.
	 * @param sourceFileNameString Source file name.
	 * @param classDump Class dump.
	 * @param location Location.
	 */
	public StackFrame(long stackFrameId, String methodNameString, String methodSigString, String sourceFileNameString, ClassDump classDump, int location) {
		this.stackFrameId = stackFrameId;
		this.methodNameStringId = methodNameString;
		this.methodSigStringId = methodSigString;
		this.sourceFileNameStringId = sourceFileNameString;
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
