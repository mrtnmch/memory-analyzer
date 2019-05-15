package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;

import java.util.List;

/**
 * Waste type - list full of nulls. The given instances are lists and their values are all or mostly nulls.
 */
public class ListOfNullsWaste implements Waste {

	/**
	 * Title template.
	 */
	private static final String TITLE_TEMPLATE = "List full or mostly consisting of nulls";

	/**
	 * Description template.
	 */
	private static final String DESC_TEMPLATE = "Values in the %s in %s(%d)#%s have a null value (%d/%dx).";

	/**
	 * Source analyzer.
	 */
	private final WasteAnalyzer sourceWasteAnalyzer;

	/**
	 * Instance of the list.
	 */
	private final InstanceDump instance;

	/**
	 * List of the values.
	 */
	private final List<?> list;

	/**
	 * Number of nulls in the {@link #instance}.
	 */
	private final long nullCount;

	/**
	 * Source instance holding the list as its variable.
	 */
	private final InstanceDump sourceInstance;

	/**
	 * Source field.
	 */
	private final InstanceFieldDump sourceField;

	/**
	 * Creates the list of nulls waste type.
	 * @param sourceWasteAnalyzer Source analyzer.
	 * @param instance Instance of the list.
	 * @param list Values in the list.
	 * @param nullCount Number of nulls in the list.
	 * @param sourceInstance Source instance.
	 * @param sourceField Source field.
	 */
	public ListOfNullsWaste(WasteAnalyzer sourceWasteAnalyzer, InstanceDump instance, List<?> list, long nullCount, InstanceDump sourceInstance, InstanceFieldDump sourceField) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		this.instance = instance;
		this.list = list;
		this.nullCount = nullCount;
		this.sourceInstance = sourceInstance;
		this.sourceField = sourceField;
	}


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
	public String getNominal() {
		return String.format("%d/%d", this.nullCount, this.list.size());
	}

	@Override
	public String getSource() {
		return String.format("%s#%s", this.sourceInstance.getClassDump().getName(), this.sourceField.getName());
	}

	@Override
	public int compareTo(Waste o) {
		if(o instanceof ListOfNullsWaste) {
			return Long.compare(((ListOfNullsWaste) o).nullCount, this.nullCount);
		}

		return 0;
	}
}
