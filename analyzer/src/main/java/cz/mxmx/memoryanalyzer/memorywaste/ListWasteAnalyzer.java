package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.InstanceArrayDump;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.ListOfNullsWaste;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Analyzer to find ineffective list usage.
 */
public class ListWasteAnalyzer implements WasteAnalyzer {

	/**
	 * Ineffective list usage threshold above which the list is marked as ineffective.
	 */
	private static final double THRESHOLD = 0.5;

	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		List<Waste> wasteList = new ArrayList<>();

		memoryDump.getUserInstances().forEach((id, instance) -> {
			instance.getInstanceFieldValues().forEach((field, value)-> {
				if(value instanceof InstanceDump) {
					if(this.isList((InstanceDump)value)) {
						this.findWastedList(memoryDump, (InstanceDump)value, wasteList, instance, field);
					}
				}
			});
		});

		return wasteList;
	}

	/**
	 * Search for a wasted list in the given parameters.
	 * @param memoryDump Memory dump to look in.
	 * @param value Instance of the list itself.
	 * @param wasteList List with the results to put a new created Waste in.
	 * @param instance Instance with the field.
	 * @param instanceFieldDump Field where the list is stored in.
	 */
	private void findWastedList(MemoryDump memoryDump, InstanceDump value, List<Waste> wasteList, InstanceDump instance, InstanceFieldDump instanceFieldDump) {
		InstanceArrayDump elements = this.getElements(memoryDump, value);

		if(elements != null && elements.getValues() != null) {
			long nullCount = findNullWastedList(elements);
			if(nullCount > elements.getValues().size() * THRESHOLD) {
				wasteList.add(new ListOfNullsWaste(this, value, elements.getValues(), nullCount, instance, instanceFieldDump));
			}
		}
	}

	/**
	 * Find the number of nulls within the given array.
	 * @param instanceArrayDump Array to check.
	 * @return Number of nulls within the array.
	 */
	private long findNullWastedList(InstanceArrayDump instanceArrayDump) {
		final long[] nullCount = {0};

		instanceArrayDump.getValues().forEach(val -> {
			if(val == null) {
				nullCount[0]++;
			}
		});

		return nullCount[0];
	}

	/**
	 * Returns the elements from the given instance.
	 * @param memoryDump Memory dump with the instance.
	 * @param value Instance to process.
	 * @return Instance of the underlying array or null.
	 */
	private InstanceArrayDump getElements(MemoryDump memoryDump, InstanceDump value) {
		Map.Entry<InstanceFieldDump, Object> elementField = value.getInstanceFieldValues().entrySet().stream().filter((field) -> field.getKey().getName().equals("elementData")).findAny().orElse(null);

		if(elementField != null) {
			Long arrayId = (Long) ((Value)elementField.getValue()).value;
			return memoryDump.getInstanceArrays().get(arrayId);
		}

		return null;
	}

	/**
	 * Checks whether the given instance dump is a list (instance of {@link AbstractList}).
	 * @param value Value to check.
	 * @return True if the given parameter is a list, otherwise false.
	 */
	private boolean isList(InstanceDump value) {
		ClassDump parent = value.getClassDump();
		do {
			if(parent.getName().equals(AbstractList.class.getName())) {
				return true;
			}

			parent = parent.getSuperClassDump();
		} while(parent != null && parent.getName() != null && !parent.getName().equals(Object.class.getName()));

		return false;
	}
}
