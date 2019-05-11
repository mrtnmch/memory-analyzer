package cz.mxmx.memoryanalyzer.memorywaste;

import com.google.common.collect.Lists;
import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.DuplicateInstanceWaste;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Analyzer to find duplicate instances of objects.
 */
public class DuplicateInstanceWasteAnalyzer implements WasteAnalyzer {

	private Map<List<Waste>, Object> locks = Collections.synchronizedMap(new HashMap<>());

	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		List<Waste> wasteList = new ArrayList<>();
		Set<InstanceDump> processedInstances = Collections.synchronizedSet(new HashSet<>());

		Map<Long, InstanceDump> userInstances = memoryDump.getUserInstances();

		userInstances.keySet().parallelStream().forEach(id -> {
			InstanceDump instance = userInstances.get(id);

			processedInstances.add(instance);

			memoryDump.getUserInstances().keySet().parallelStream().forEach(id2 -> {
				InstanceDump instance2 = memoryDump.getUserInstances().get(id2);

				if (id.equals(id2) || processedInstances.contains(instance2)) {
					return;
				}

				if (this.instancesAreSame(instance, instance2)) {
					this.mergeWasteList(wasteList, instance, instance2);
				}
			});
		});

		System.out.println();

		return wasteList;
	}

	private boolean instancesAreSame(InstanceDump instance, InstanceDump instance2) {
		if(!this.instancesOfSameClass(instance, instance2)
				|| instance.getInstanceFieldValues().size() != instance2.getInstanceFieldValues().size()) {
			return false;
		}

		ClassDump classDump = instance.getClassDump();

		for (InstanceFieldDump field : classDump.getInstanceFields()) {
			Object value = instance.getInstanceFieldValues().get(field);
			Object value2 = instance2.getInstanceFieldValues().get(field);

			if (!value.equals(value2)) {
				return false;
			}
		}

		return classDump.getInstanceFields().size() > 0;
	}

	private boolean instancesOfSameClass(InstanceDump instance, InstanceDump instance2) {
		ClassDump parent = instance.getClassDump();

		do {
			if (parent.equals(instance2.getClassDump())) {
				return true;
			}

			parent = parent.getSuperClassDump();
		} while (parent != null && parent.getName() != null && !parent.getName().equals(Object.class.getName()));

		parent = instance2.getClassDump();

		do {
			if (parent.equals(instance.getClassDump())) {
				return true;
			}

			parent = parent.getSuperClassDump();
		} while (parent != null && parent.getName() != null && !parent.getName().equals(Object.class.getName()));

		return false;
	}

	private void mergeWasteList(List<Waste> wasteList, InstanceDump instance, InstanceDump instance2) {
		if(!this.locks.containsKey(wasteList)) {
			this.locks.put(wasteList, new Object());
		}

		synchronized (this.locks.get(wasteList)) {
			Optional<Waste> optWaste = wasteList
					.stream()
					.filter(waste -> this.instancesAreSame(waste.getAffectedInstances().get(0), instance)).findFirst();

			if (optWaste.isPresent()) {
				if (!optWaste.get().getAffectedInstances().contains(instance)) {
					optWaste.get().addAffectedInstance(instance);
				}

				if (!optWaste.get().getAffectedInstances().contains(instance2)) {
					optWaste.get().addAffectedInstance(instance2);
				}
			} else {
				wasteList.add(new DuplicateInstanceWaste(this, Lists.newArrayList(instance, instance2)));
			}
		}
	}
}
