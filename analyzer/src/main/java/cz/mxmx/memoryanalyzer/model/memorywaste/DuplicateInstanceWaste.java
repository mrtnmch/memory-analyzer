package cz.mxmx.memoryanalyzer.model.memorywaste;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Waste type that represents duplicate instances.
 */
public class DuplicateInstanceWaste implements Waste {
	private static final String TITLE_TEMPLATE = "Duplicates of '%s'";
	private static final String DESC_TEMPLATE = "%d instances of the '%s' class contain exactly the same data.";

	private final Collection<InstanceDump> duplicates;
	private final WasteAnalyzer sourceWasteAnalyzer;

	public DuplicateInstanceWaste(WasteAnalyzer sourceWasteAnalyzer, Collection<InstanceDump> duplicates) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		if(duplicates.isEmpty()) {
			throw new IllegalArgumentException("There must be at least 2 duplicates");
		}

		this.duplicates = duplicates;
	}

	protected int getDuplicatesSize() {
		return this.duplicates.size();
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
		return String.format(DESC_TEMPLATE, this.duplicates.size(), this.getAffectedClass());
	}

	@Override
	public List<InstanceDump> getAffectedInstances() {
		return new ArrayList<>(this.duplicates);
	}

	@Override
	public void addAffectedInstance(InstanceDump instanceDump) {
		this.duplicates.add(instanceDump);
	}

	@Override
	public WasteAnalyzer getSourceWasteAnalyzer() {
		return this.sourceWasteAnalyzer;
	}

	@Override
	public String getNominal() {
		return String.format("%d", this.duplicates.size());
	}

	@Override
	public String getSource() {
		return this.getAffectedClass();
	}

	private String getAffectedClass() {
		return new ArrayList<>(this.getAffectedInstances())
				.stream()
				.findFirst()
				.map(instance -> instance.getClassDump().getName())
				.orElse(null);
	}

	@Override
	public int compareTo(Waste o) {
		if(o instanceof DuplicateInstanceWaste) {
			return Integer.compare(((DuplicateInstanceWaste) o).getDuplicatesSize(), this.getDuplicatesSize());
		}

		return 0;
	}
}
