package cz.mxmx.memoryanalyzer.model;

import java.util.List;

public class StackTrace {
	private final int stackTraceSerialNum;
	private final int threadSerialNum;
	private final int numFrames;
	private final List<StackFrame> stackFrames;

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
