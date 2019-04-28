package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.ArrayList;
import java.util.List;

public class ListOfDuplicatesWaste implements Waste {

	private final WasteAnalyzer sourceWasteAnalyzer;
	private final InstanceDump instance;
	private final List<?> list;

	public ListOfDuplicatesWaste(WasteAnalyzer sourceWasteAnalyzer, InstanceDump instance, List<?> list) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		this.instance = instance;
		this.list = list;
	}

	private static final String TITLE_TEMPLATE = "List full of same values";
	private static final String DESC_TEMPLATE = "All values in the list have the same value (%dx).";

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
		return String.format(DESC_TEMPLATE, this.list.size());
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
}
