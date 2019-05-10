package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;

import java.util.List;

public class ListOfNullsWaste implements Waste {

	private final WasteAnalyzer sourceWasteAnalyzer;
	private final InstanceDump instance;
	private final List<?> list;
	private final long nullCount;
	private final InstanceDump sourceInstance;
	private final InstanceFieldDump sourceField;

	public ListOfNullsWaste(WasteAnalyzer sourceWasteAnalyzer, InstanceDump instance, List<?> list, long nullCount, InstanceDump sourceInstance, InstanceFieldDump sourceField) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		this.instance = instance;
		this.list = list;
		this.nullCount = nullCount;
		this.sourceInstance = sourceInstance;
		this.sourceField = sourceField;
	}

	private static final String TITLE_TEMPLATE = "List full or mostly consisting of nulls";
	private static final String DESC_TEMPLATE = "All or most of the values in the %s in %s(%d)#%s have a null value (%d/%dx).";

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
		return String.format(
				DESC_TEMPLATE,
				this.instance.getClassDump().getName(),
				this.sourceInstance.getClassDump().getName(),
				this.sourceInstance.getInstanceId(),
				this.sourceField.getName(),
				this.nullCount,
				this.list.size()
		);
	}

	@Override
	public List<InstanceDump> getAffectedInstances() {
		return Lists.newArrayList(this.instance);
	}

	@Override
	public void addAffectedInstance(InstanceDump instanceDump) {
		throw new IllegalArgumentException("List of null items waste is always unique.");
	}

	@Override
	public WasteAnalyzer getSourceWasteAnalyzer() {
		return this.sourceWasteAnalyzer;
	}

	@Override
	public int compareTo(Waste o) {
		if(o instanceof ListOfNullsWaste) {
			return Long.compare(((ListOfNullsWaste) o).nullCount, this.nullCount);
		}

		return 0;
	}
}
