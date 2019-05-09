package cz.mxmx.memoryanalyzer.model.raw;

public class RawHeapSummary {
	private final int liveBytes;
	private final int liveInstances;
	private final long bytesAllocated;
	private final long instancesAllocated;

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
