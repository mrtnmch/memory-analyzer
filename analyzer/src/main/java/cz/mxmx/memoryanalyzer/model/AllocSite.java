package cz.mxmx.memoryanalyzer.model;

public class AllocSite {
	private boolean isArray;
	private int classSerialNum;
	private int stackTraceSerialNum;
	private int liveBytes;
	private int liveInstances;
	private int bytesAllocated;
	private int instancesAllocated;

	public AllocSite(boolean arrayIndicator, int classSerialNum, int stackTraceSerialNum, int liveBytes, int liveInstances, int bytesAllocated, int instancesAllocated) {
		this.isArray = arrayIndicator;
		this.classSerialNum = classSerialNum;
		this.stackTraceSerialNum = stackTraceSerialNum;
		this.liveBytes = liveBytes;
		this.liveInstances = liveInstances;
		this.bytesAllocated = bytesAllocated;
		this.instancesAllocated = instancesAllocated;
	}

	public boolean isArray() {
		return isArray;
	}

	public int getClassSerialNum() {
		return classSerialNum;
	}

	public int getStackTraceSerialNum() {
		return stackTraceSerialNum;
	}

	public int getLiveBytes() {
		return liveBytes;
	}

	public int getLiveInstances() {
		return liveInstances;
	}

	public int getBytesAllocated() {
		return bytesAllocated;
	}

	public int getInstancesAllocated() {
		return instancesAllocated;
	}
}
