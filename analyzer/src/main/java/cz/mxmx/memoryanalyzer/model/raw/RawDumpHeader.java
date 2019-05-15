package cz.mxmx.memoryanalyzer.model.raw;

/**
 * Raw memory dump's header.
 */
public class RawDumpHeader {
	private final String format;
	private final int idSize;
	private final long time;

	/**
	 * Creates a new raw memory dump header.
	 * @param format Format of the memory dump.
	 * @param idSize Byte size of IDs in the given dump.
	 * @param time Base datetime.
	 */
	public RawDumpHeader(String format, int idSize, long time) {
		this.format = format;
		this.idSize = idSize;
		this.time = time;
	}

	public String getFormat() {
		return format;
	}

	public int getIdSize() {
		return idSize;
	}

	public long getTime() {
		return time;
	}
}
