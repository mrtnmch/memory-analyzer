package cz.mxmx.memoryanalyzer.model;

import java.util.Collection;
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


}
