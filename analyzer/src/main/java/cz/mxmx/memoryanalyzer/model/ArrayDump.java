package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ArrayDump<T> extends GenericDump {
	private final Class<T> type;
	private final List<Object> values;

	public ArrayDump(Long objectId, Class<T> type, List<Object> values) {
		super(objectId);
		this.type = type;
		this.values = values;
	}


	public Class<T> getType() {
		return type;
	}

	public List<Object> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("type", type)
				.append("instanceId", instanceId)
				.toString();
	}
}
