package cz.mxmx.memoryanalyzer.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MemoryDump {
	Collection<String> getUserNamespaces();

	DumpHeader getDumpHeader();

	Map<Long, InstanceDump> getInstances();

	Map<Long, ClassDump> getClasses();

	Map<Long, InstanceDump> getUserInstances();

	Map<Long, ClassDump> getUserClasses();

	Map<Long, ArrayDump> getPrimitiveArrays();

	Map<Long, InstanceArrayDump> getInstanceArrays();

	List<AllocSiteParent> getAllocSites();

	List<StackTrace> getStackTraces();

	MemoryDump setUserNamespaces(Collection<String> userNamespaces);

	MemoryDump setDumpHeader(DumpHeader dumpHeader);

	MemoryDump setInstances(Map<Long, InstanceDump> instances);

	MemoryDump setClasses(Map<Long, ClassDump> classes);

	MemoryDump setUserInstances(Map<Long, InstanceDump> userInstances);

	MemoryDump setUserClasses(Map<Long, ClassDump> userClasses);

	MemoryDump setPrimitiveArrays(Map<Long, ArrayDump> primitiveArrays);

	MemoryDump setInstanceArrays(Map<Long, InstanceArrayDump> instanceArrays);

	MemoryDump setAllocSites(List<AllocSiteParent> allocSiteParents);

	MemoryDump setStackTraces(List<StackTrace> stackTraces);
}
