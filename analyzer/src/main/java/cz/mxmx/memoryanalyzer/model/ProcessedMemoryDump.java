package cz.mxmx.memoryanalyzer.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A default implementation of {@link MemoryDump}.
 */
public class ProcessedMemoryDump implements MemoryDump {

	private Collection<String> userNamespaces;
	private DumpHeader dumpHeader;
	private Map<Long, InstanceDump> instances;
	private Map<Long, ClassDump> classes;
	private Map<Long, InstanceDump> userInstances;
	private Map<Long, ClassDump> userClasses;
	private Map<Long, ArrayDump> primitiveArrays;
	private Map<Long, InstanceArrayDump> instanceArrays;
	private List<AllocSiteParent> allocSites;
	private List<StackTrace> stackTraces;

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
	public List<AllocSiteParent> getAllocSites() {
		return this.allocSites;
	}

	@Override
	public List<StackTrace> getStackTraces() {
		return this.stackTraces;
	}

	@Override
	public ProcessedMemoryDump setUserNamespaces(Collection<String> userNamespaces) {
		this.userNamespaces = userNamespaces;
		return this;
	}

	@Override
	public ProcessedMemoryDump setDumpHeader(DumpHeader dumpHeader) {
		this.dumpHeader = dumpHeader;
		return this;
	}

	@Override
	public ProcessedMemoryDump setInstances(Map<Long, InstanceDump> instances) {
		this.instances = instances;
		return this;
	}

	@Override
	public ProcessedMemoryDump setClasses(Map<Long, ClassDump> classes) {
		this.classes = classes;
		return this;
	}

	@Override
	public ProcessedMemoryDump setUserInstances(Map<Long, InstanceDump> userInstances) {
		this.userInstances = userInstances;
		return this;
	}

	@Override
	public ProcessedMemoryDump setUserClasses(Map<Long, ClassDump> userClasses) {
		this.userClasses = userClasses;
		return this;
	}

	@Override
	public ProcessedMemoryDump setPrimitiveArrays(Map<Long, ArrayDump> primitiveArrays) {
		this.primitiveArrays = primitiveArrays;
		return this;
	}

	@Override
	public ProcessedMemoryDump setInstanceArrays(Map<Long, InstanceArrayDump> instanceArrays) {
		this.instanceArrays = instanceArrays;
		return this;
	}

	@Override
	public ProcessedMemoryDump setAllocSites(List<AllocSiteParent> allocSiteParents) {
		this.allocSites = allocSiteParents;
		return this;
	}

	@Override
	public ProcessedMemoryDump setStackTraces(List<StackTrace> stackTraces) {
		this.stackTraces = stackTraces;
		return this;
	}
}
