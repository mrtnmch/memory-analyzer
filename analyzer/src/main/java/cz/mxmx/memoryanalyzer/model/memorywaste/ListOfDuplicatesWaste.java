package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;

import java.util.List;

public class ListOfDuplicatesWaste implements Waste {

	protected final WasteAnalyzer sourceWasteAnalyzer;
	protected final InstanceDump instance;
	private final InstanceFieldDump field;
	protected final long size;

	public ListOfDuplicatesWaste(WasteAnalyzer sourceWasteAnalyzer, InstanceDump instance, InstanceFieldDump field, long size) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		this.instance = instance;
		this.field = field;
		this.size = size;
	}

	private static final String TITLE_TEMPLATE = "List full of same values";
	private static final String DESC_TEMPLATE = "All values in the list %s#%s have the same value (%dx).";

	@Override
	public Long estimateWastedBytes() {
		return null;
	}

	@Override
	public String getTitle() {
		return TITLE_TEMPLATE;
	}

	@Override
	public String getDescription() {
		return String.format(DESC_TEMPLATE, this.instance.getClassDump().getName(), this.field.getName(), this.size);
	}

	@Override
	public List<InstanceDump> getAffectedInstances() {
		return Lists.newArrayList(this.instance);
	}

	@Override
	public void addAffectedInstance(InstanceDump instanceDump) {
		throw new IllegalArgumentException("List of duplicate items waste is always unique.");
	}

	@Override
	public WasteAnalyzer getSourceWasteAnalyzer() {
		return this.sourceWasteAnalyzer;
	}

	@Override
	public int compareTo(Waste o) {
		if(o instanceof  ListOfDuplicatesWaste) {
			return Long.compare(((ListOfDuplicatesWaste) o).size, this.size);
		}

		return 0;
	}
}
