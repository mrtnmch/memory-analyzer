package cz.mxmx.memoryanalyzer.model;

import java.util.List;

/**
 * Stack trace.
 */
public class StackTrace {
	private final int stackTraceSerialNum;
	private final int threadSerialNum;
	private final int numFrames;
	private final List<StackFrame> stackFrames;

	/**
	 * Creates a stack trace representation.
	 * @param stackTraceSerialNum Stack trace serial number.
	 * @param threadSerialNum Thread serial number.
	 * @param numFrames Number of frames.
	 * @param stackFrames List of the stack frames ({@link StackFrame}).
	 */
	public StackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames, List<StackFrame> stackFrames) {
		this.stackTraceSerialNum = stackTraceSerialNum;
		this.threadSerialNum = threadSerialNum;
		this.numFrames = numFrames;
		this.stackFrames = stackFrames;
	}

	public int getStackTraceSerialNum() {
		return stackTraceSerialNum;
	}

	public int getThreadSerialNum() {
		return threadSerialNum;
	}

	public int getNumFrames() {
		return numFrames;
	}

	public List<StackFrame> getStackFrames() {
		return stackFrames;
	}
}
