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

public class DuplicateInstanceWasteAnalyzer implements WasteAnalyzer {
	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		List<Waste> wasteList = new ArrayList<>();
		Set<InstanceDump> processedInstances = new HashSet<>();
//		List<InstanceDump> processedInstances = new ArrayList<>();

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

			double i = new ArrayList<>(memoryDump.getUserInstances().keySet()).indexOf(id);
			if(i > 10000) { // TODO
				break;
			}
		}

		System.out.println();

		return wasteList;
	}

	private boolean instancesAreSame(InstanceDump instance, InstanceDump instance2) {
		if(!this.instancesOfSameClass(instance, instance2)) {
			return false;
		}

		final boolean[] same = {true};
		ClassDump classDump = instance.getClassDump();

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
			optWaste.get().addAffectedInstance(instance);
			optWaste.get().addAffectedInstance(instance2);
		} else {
			wasteList.add(new DuplicateInstanceWaste(Lists.newArrayList(instance, instance2)));
		}
	}
}
