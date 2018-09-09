package cz.mxmx.memoryanalyzer.model.raw;

public class RawDumpHeader {
	private final String format;
	private final int idSize;
	private final long time;

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
