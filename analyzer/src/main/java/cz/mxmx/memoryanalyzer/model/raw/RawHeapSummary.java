package cz.mxmx.memoryanalyzer.model.raw;

/**
 * Raw memory heap dump summary.
 */
public class RawHeapSummary {
	private final int liveBytes;
	private final int liveInstances;
	private final long bytesAllocated;
	private final long instancesAllocated;

	/**
	 * Creates a new raw memory dump summary.
	 * @param liveBytes Number of "live" bytes in the heap.
	 * @param liveInstances Number of "live" instances in the heap.
	 * @param bytesAllocated Number of allocated bytes.
	 * @param instancesAllocated Number of allocated instances.
	 */
	public RawHeapSummary(int liveBytes, int liveInstances, long bytesAllocated, long instancesAllocated) {
		this.liveBytes = liveBytes;
		this.liveInstances = liveInstances;
		this.bytesAllocated = bytesAllocated;
		this.instancesAllocated = instancesAllocated;
	}

	public int getLiveBytes() {
		return liveBytes;
	}

	public int getLiveInstances() {
		return liveInstances;
	}

	public long getBytesAllocated() {
		return bytesAllocated;
	}

	public long getInstancesAllocated() {
		return instancesAllocated;
	}
}
