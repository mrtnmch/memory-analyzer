package cz.mxmx.memoryanalyzer.memorywaste;

import com.google.common.collect.Lists;
import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.DuplicateInstanceWaste;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Analyzer to find duplicate instances of objects.
 */
public class DuplicateInstanceWasteAnalyzer implements WasteAnalyzer {

	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		List<Waste> wasteList = new ArrayList<>();
		Set<InstanceDump> processedInstances = new HashSet<>();

		Map<Long, InstanceDump> userInstances = memoryDump.getUserInstances();

		for(Map.Entry<Long, InstanceDump> entry : userInstances.entrySet()) {
			Long id = entry.getKey();
			InstanceDump instance = entry.getValue();

			processedInstances.add(instance);

			memoryDump.getUserInstances().forEach((id2, instance2) -> {
				if(id.equals(id2) || processedInstances.contains(instance2)) {
					return;
				}

				if(this.instancesAreSame(instance, instance2)) {
					this.mergeWasteList(wasteList, instance, instance2);
				}
			});
		}

		System.out.println();

		return wasteList;
	}

	private boolean instancesAreSame(InstanceDump instance, InstanceDump instance2) {
		if(!this.instancesOfSameClass(instance, instance2)) {
			return false;
		}

		ClassDump classDump = instance.getClassDump();
		final boolean[] same = {classDump.getInstanceFields().size() > 0};

		classDump.getInstanceFields().forEach(field -> {
			Object value = instance.getInstanceFieldValues().get(field);
			Object value2 = instance2.getInstanceFieldValues().get(field);

			if(!value.equals(value2)) {
				same[0] = false;
			}
		});

		return same[0];
	}

	private boolean instancesOfSameClass(InstanceDump instance, InstanceDump instance2) {
		return instance.getClassDump().equals(instance2.getClassDump());
	}

	private void mergeWasteList(List<Waste> wasteList, InstanceDump instance, InstanceDump instance2) {
		Optional<Waste> optWaste = wasteList
				.stream()
				.filter(waste -> this.instancesAreSame(waste.getAffectedInstances().get(0), instance)).findFirst();

		if(optWaste.isPresent()) {
			if(!optWaste.get().getAffectedInstances().contains(instance)) {
				optWaste.get().addAffectedInstance(instance);
			}

			if(!optWaste.get().getAffectedInstances().contains(instance2)) {
				optWaste.get().addAffectedInstance(instance2);
			}
		} else {
			wasteList.add(new DuplicateInstanceWaste(this, Lists.newArrayList(instance, instance2)));
		}
	}
}
