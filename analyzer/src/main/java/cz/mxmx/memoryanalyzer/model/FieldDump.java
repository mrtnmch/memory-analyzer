package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Processed field of a class/instance.
 */
public class FieldDump {
	private final String name;
	private final Class<?> type;
	private final Object value;

	/**
	 * Creates a field representation.
	 * @param name Field name.
	 * @param type Field type.
	 * @param value Field value.
	 */
	public FieldDump(String name, Class<?> type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", name)
				.append("type", type)
				.append("value", value)
				.toString();
	}
}
