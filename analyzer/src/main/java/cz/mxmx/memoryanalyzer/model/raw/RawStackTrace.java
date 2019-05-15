package cz.mxmx.memoryanalyzer.model.raw;

/**
 * Raw stack trace.
 */
public class RawStackTrace {
	private final int stackTraceSerialNum;
	private final int threadSerialNum;
	private final int numFrames;
	private final long[] stackFrameIds;

	/**
	 * Creates a raw stack trace.
	 * @param stackTraceSerialNum Stack trace serial number.
	 * @param threadSerialNum Thread serial number.
	 * @param numFrames Number of frames.
	 * @param stackFrameIds IDs of the stack frames (see {@link RawStackFrame#getStackFrameId()}
	 */
	public RawStackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames, long[] stackFrameIds) {
		this.stackTraceSerialNum = stackTraceSerialNum;
		this.threadSerialNum = threadSerialNum;
		this.numFrames = numFrames;
		this.stackFrameIds = stackFrameIds;
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

	public long[] getStackFrameIds() {
		return stackFrameIds;
	}
}
