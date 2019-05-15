package cz.mxmx.memoryanalyzer.model.raw;

/**
 * Raw "alloc site" data representation.
 */
public class RawAllocSite {
	private byte arrayIndicator;
	private int classSerialNum;
	private int stackTraceSerialNum;
	private int liveBytes;
	private int liveInstances;
	private int bytesAllocated;
	private int instancesAllocated;

	/**
	 * Creates new alloc site data representation.
	 * @param arrayIndicator 1 if the type is an array or 0.
	 * @param classSerialNum Class serial number.
	 * @param stackTraceSerialNum Stack trace serial number.
	 * @param liveBytes Number of "live" bytes.
	 * @param liveInstances Number of "live" instances.
	 * @param bytesAllocated Number of allocated bytes.
	 * @param instancesAllocated Number of allocated instances.
	 */
	public RawAllocSite(byte arrayIndicator, int classSerialNum, int stackTraceSerialNum, int liveBytes, int liveInstances, int bytesAllocated, int instancesAllocated) {
		this.arrayIndicator = arrayIndicator;
		this.classSerialNum = classSerialNum;
		this.stackTraceSerialNum = stackTraceSerialNum;
		this.liveBytes = liveBytes;
		this.liveInstances = liveInstances;
		this.bytesAllocated = bytesAllocated;
		this.instancesAllocated = instancesAllocated;
	}

	public byte getArrayIndicator() {
		return arrayIndicator;
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
