package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance field.
 * @param <T> Type of the field.
 */
public class InstanceFieldDump<T> {
	private final String name;
	private final Class<T> type;
	private final List<T> values = new ArrayList<>();

	/**
	 * Creates an instance field.
	 * @param name Name of the field.
	 * @param type Type of the field.
	 */
	public InstanceFieldDump(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public List<T> getValues() {
		return values;
	}

	public void addValue(T value) {
		this.values.add(value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", name)
				.append("type", type)
				.append("values", values)
				.toString();
	}
}
