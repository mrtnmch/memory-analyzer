package cz.mxmx.memoryanalyzer.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Processed memory dump representation.
 */
public interface MemoryDump {

	/**
	 * List of user's namespaces to filter.
	 * @return List of the namespaces.
	 */
	Collection<String> getUserNamespaces();

	/**
	 * Dump file header.
	 * @return The header.
	 */
	DumpHeader getDumpHeader();

	/**
	 * Instance list.
	 * @return Instance list and their ID as a key.
	 */
	Map<Long, InstanceDump> getInstances();

	/**
	 * Class list.
	 * @return Class list and their ID as a key.
	 */
	Map<Long, ClassDump> getClasses();

	/**
	 * Instances filtered via {@link #getUserNamespaces()}.
	 * @return List of the user namespace instances and their ID as a key.
	 */
	Map<Long, InstanceDump> getUserInstances();

	/**
	 * Classes filtered via {@link #getUserNamespaces()}.
	 * @return List of the user namespace classes and their ID as a key.
	 */
	Map<Long, ClassDump> getUserClasses();

	/**
	 * List of primitive type arrays.
	 * @return List of the arrays and their ID as a key.
	 */
	Map<Long, ArrayDump> getPrimitiveArrays();

	/**
	 * List of instance type arrays.
	 * @return List of the arrays and their ID as a key.
	 */
	Map<Long, InstanceArrayDump> getInstanceArrays();

	/**
	 * Alloc sites in the dump.
	 * @return List of alloc sites.
	 */
	List<AllocSiteParent> getAllocSites();

	/**
	 * Stack traces in the dump.
	 * @return List of stack traces.
	 */
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
