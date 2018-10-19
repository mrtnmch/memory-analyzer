package cz.mxmx.memoryanalyzer.model.memorywaste;

import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DuplicateInstanceWaste implements Waste {
	private static final String TITLE_TEMPLATE = "Duplicate instances of '%s'";
	private static final String DESC_TEMPLATE = "Some instances of the '%s' class contain exactly the same data. They could be possibly replaced with one copy.";

	private final Collection<InstanceDump> duplicates;

	public DuplicateInstanceWaste(Collection<InstanceDump> duplicates) {
		if(duplicates.isEmpty()) {
			throw new IllegalArgumentException("There must be at least 2 duplicates");
		}

		this.duplicates = duplicates;
	}

	@Override
	public Long estimateWastedBytes() {
		return null;
	}

	@Override
	public String getTitle() {
		return String.format(TITLE_TEMPLATE, this.getAffectedClass());
	}

	@Override
	public String getDescription() {
		return String.format(DESC_TEMPLATE, this.getAffectedClass());
	}

	@Override
	public List<InstanceDump> getAffectedInstances() {
		return new ArrayList<>(this.duplicates);
	}

	@Override
	public void addAffectedInstance(InstanceDump instanceDump) {
		this.duplicates.add(instanceDump);
	}

	private String getAffectedClass() {
		return new ArrayList<>(this.getAffectedInstances())
				.stream()
				.findFirst()
				.map(instance -> instance.getClassDump().getName())
				.orElse(null);
	}
}
