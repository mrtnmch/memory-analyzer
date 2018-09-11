package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ArrayDump<T> extends GenericDump {
	private final Integer size;
	private final Class<T> type;

	public ArrayDump(Long objectId, Integer size, Class<T> type) {
		super(objectId);
		this.size = size;
		this.type = type;
	}

	public Integer getSize() {
		return size;
	}

	public Class<T> getType() {
		return type;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("size", size)
				.append("type", type)
				.append("instanceId", instanceId)
				.toString();
	}
}
