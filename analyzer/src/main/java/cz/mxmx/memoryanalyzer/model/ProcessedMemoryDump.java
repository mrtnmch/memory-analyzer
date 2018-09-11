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

	public ProcessedMemoryDump(Collection<String> userNamespaces, DumpHeader dumpHeader, Map<Long, InstanceDump> instances, Map<Long, ClassDump> classes, Map<Long, InstanceDump> userInstances, Map<Long, ClassDump> userClasses) {
		this.userNamespaces = userNamespaces;
		this.dumpHeader = dumpHeader;
		this.instances = instances;
		this.classes = classes;
		this.userInstances = userInstances;
		this.userClasses = userClasses;
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
	public Map<Long, InstanceDump> getObjects() {
		return instances;
	}

	@Override
	public Map<Long, ClassDump> getClasses() {
		return classes;
	}

	@Override
	public Map<Long, InstanceDump> getUserObjects() {
		return userInstances;
	}

	@Override
	public Map<Long, ClassDump> getUserClasses() {
		return userClasses;
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
