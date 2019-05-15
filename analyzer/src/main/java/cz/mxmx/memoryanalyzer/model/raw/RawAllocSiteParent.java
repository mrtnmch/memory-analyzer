package cz.mxmx.memoryanalyzer.model.raw;

import java.util.List;

/**
 * Alloc site parent type which holds the list of its {@link RawAllocSite} children.
 */
public class RawAllocSiteParent {
	private final short bitMaskFlags;
	private final float cutoffRatio;
	private final int liveBytes;
	private final int liveInstances;
	private final long bytesAllocated;
	private final long instancesAllocated;
	private final List<RawAllocSite> sites;

	/**
	 * Creates a new parent type.
	 * @param bitMaskFlags Flag map.
	 * @param cutoffRatio Cutoff ration from the alloc site.
	 * @param liveBytes Number of "live" bytes.
	 * @param liveInstances Number of "live" instances.
	 * @param bytesAllocated Number of bytes allocated.
	 * @param instancesAllocated number of instances allocated.
	 * @param sites List with the children sites.
	 */
	public RawAllocSiteParent(short bitMaskFlags, float cutoffRatio, int liveBytes, int liveInstances, long bytesAllocated, long instancesAllocated, List<RawAllocSite> sites) {
		this.bitMaskFlags = bitMaskFlags;
		this.cutoffRatio = cutoffRatio;
		this.liveBytes = liveBytes;
		this.liveInstances = liveInstances;
		this.bytesAllocated = bytesAllocated;
		this.instancesAllocated = instancesAllocated;
		this.sites = sites;
	}

	public short getBitMaskFlags() {
		return bitMaskFlags;
	}

	public float getCutoffRatio() {
		return cutoffRatio;
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

	public List<RawAllocSite> getSites() {
		return sites;
	}
}
