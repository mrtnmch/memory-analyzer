package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.Map;

public class ProcessedMemoryDump implements MemoryDump {

	private final Collection<String> userNamespaces;
	private final DumpHeader dumpHeader;
	private final Map<Long, InstanceDump> instances;
	private final Map<Long, ClassDump> classes;
	private final Map<Long, InstanceDump> userInstances;
	private final Map<Long, ClassDump> userClasses;
	private final Map<Long, ArrayDump> primitiveArrays;
	private final Map<Long, InstanceArrayDump> instanceArrays;

	public ProcessedMemoryDump(Collection<String> userNamespaces, DumpHeader dumpHeader, Map<Long, InstanceDump> instances, Map<Long, ClassDump> classes, Map<Long, InstanceDump> userInstances, Map<Long, ClassDump> userClasses, Map<Long, ArrayDump> primitiveArrays, Map<Long, InstanceArrayDump> instanceArrays) {
		this.userNamespaces = userNamespaces;
		this.dumpHeader = dumpHeader;
		this.instances = instances;
		this.classes = classes;
		this.userInstances = userInstances;
		this.userClasses = userClasses;
		this.primitiveArrays = primitiveArrays;
		this.instanceArrays = instanceArrays;
	}

	@Override
	public Collection<String> getUserNamespaces() {
		return userNamespaces;
	}

	@Override
	public DumpHeader getDumpHeader() {
		return dumpHeader;
	}

	@Override
	public Map<Long, InstanceDump> getInstances() {
		return instances;
	}

	@Override
	public Map<Long, ClassDump> getClasses() {
		return classes;
	}

	@Override
	public Map<Long, InstanceDump> getUserInstances() {
		return userInstances;
	}

	@Override
	public Map<Long, ClassDump> getUserClasses() {
		return userClasses;
	}

	public Map<Long, ArrayDump> getPrimitiveArrays() {
		return primitiveArrays;
	}

	public Map<Long, InstanceArrayDump> getInstanceArrays() {
		return instanceArrays;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("userNamespaces", userNamespaces)
				.append("dumpHeader", dumpHeader)
				.append("instances", instances.size())
				.append("classes", classes.size())
				.append("userInstances", userInstances.size())
				.append("userClasses", userClasses.size())
				.toString();
	}
}
