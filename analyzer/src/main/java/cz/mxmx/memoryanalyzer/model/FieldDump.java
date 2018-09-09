package cz.mxmx.memoryanalyzer.model;

public class FieldDump {
	private final String name;
	private final Class<?> type;
	private final Object value;

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
}
