package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DumpHeader {
	private final String format;
	private final int idSize;
	private final Date time;

	public DumpHeader(String format, int idSize, Date time) {
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

	public Date getTime() {
		return time;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("format", format)
				.append("idSize", idSize)
				.append("time", time)
				.toString();
	}
}
