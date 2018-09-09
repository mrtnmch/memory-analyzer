package cz.mxmx.memoryanalyzer.model;

import java.util.Collection;
import java.util.Map;

public interface MemoryDump {
	Collection<String> getUserNamespaces();

	DumpHeader getDumpHeader();

	Map<Long, InstanceDump> getObjects();

	Map<Long, ClassDump> getClasses();

	Map<Long, InstanceDump> getUserObjects();

	Map<Long, ClassDump> getUserClasses();
}
