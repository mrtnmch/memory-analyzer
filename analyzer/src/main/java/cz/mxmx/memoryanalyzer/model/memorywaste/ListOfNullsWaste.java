package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.List;

public class ListOfNullsWaste implements Waste {

	private final InstanceDump instance;
	private final List<?> list;
	private final long nullCount;

	public ListOfNullsWaste(InstanceDump instance, List<?> list, long nullCount) {
		this.instance = instance;
		this.list = list;
		this.nullCount = nullCount;
	}

	private static final String TITLE_TEMPLATE = "List full or mostly consisting of nulls";
	private static final String DESC_TEMPLATE = "All or most of the values in the list have a null value (%d/%dx).";

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
		return String.format(DESC_TEMPLATE, this.nullCount, this.list.size());
	}

	@Override
	public List<InstanceDump> getAffectedInstances() {
		return Lists.newArrayList(this.instance);
	}

	@Override
	public void addAffectedInstance(InstanceDump instanceDump) {
		throw new IllegalArgumentException("List of null items waste is always unique.");
	}
}
