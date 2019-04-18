package cz.mxmx.memoryanalyzer.process;

import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.ProcessedMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserMemoryDumpProcessor implements MemoryDumpProcessor {

	private final Map<RawMemoryDump, MemoryDump> cache = new HashMap<>();
	private final MemoryDumpProcessor genericMemoryDumpProcessor;
	private final Collection<String> namespaces;

	public UserMemoryDumpProcessor(MemoryDumpProcessor genericMemoryDumpProcessor, Collection<String> namespaces) {
		this.genericMemoryDumpProcessor = genericMemoryDumpProcessor;
		this.namespaces = namespaces;
	}

	@Override
	public MemoryDump process(RawMemoryDump rawMemoryDump) {
		if (this.cache.containsKey(rawMemoryDump)) {
			return this.cache.get(rawMemoryDump);
		}

		MemoryDump preprocessed = this.genericMemoryDumpProcessor.process(rawMemoryDump);
		Map<Long, ClassDump> userClasses = this.getUserClasses(preprocessed.getClasses());
		Map<Long, InstanceDump> userInstances = this.getUserInstances(preprocessed.getInstances(), userClasses);

		ProcessedMemoryDump processedMemoryDump = new ProcessedMemoryDump(
				this.namespaces,
				preprocessed.getDumpHeader(),
				preprocessed.getInstances(),
				preprocessed.getClasses(),
				userInstances,
				userClasses,
				preprocessed.getPrimitiveArrays(),
				preprocessed.getInstanceArrays()
		);

		this.cache.put(rawMemoryDump, processedMemoryDump);
		return processedMemoryDump;
	}

	protected Map<Long, InstanceDump> getUserInstances(Map<Long, InstanceDump> instances, Map<Long, ClassDump> userClasses) {
		Map<Long, InstanceDump> userInstances = new HashMap<>();

		instances.forEach((key, value) -> {
			if (userClasses.containsKey(value.getClassDump().getClassId())) {
				userInstances.put(key, value);
			}
		});

		return userInstances;
	}

	protected Map<Long, ClassDump> getUserClasses(Map<Long, ClassDump> classes) {
		Map<Long, ClassDump> userClasses = new HashMap<>();

		classes.forEach((key, value) -> {
			this.namespaces.forEach((namespace) -> {
				if (value.getName().matches(namespace)) {
					userClasses.put(key, value);
				}
			});
		});

		return userClasses;
	}
}
