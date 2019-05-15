package cz.mxmx.memoryanalyzer.model;

/**
 * Processed alloc site.
 */
public class AllocSite {
	private boolean isArray;
	private int classSerialNum;
	private int stackTraceSerialNum;
	private int liveBytes;
	private int liveInstances;
	private int bytesAllocated;
	private int instancesAllocated;

	/**
	 * Creates a processed alloc site.
	 * @param arrayIndicator True if the instance is an array or false.
	 * @param classSerialNum Class serial number.
	 * @param stackTraceSerialNum Stack trace serial number.
	 * @param liveBytes Number of the "live" bytes.
	 * @param liveInstances Number of the "live" instances.
	 * @param bytesAllocated Number of allocated bytes.
	 * @param instancesAllocated Number of allocated instances.
	 */
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
