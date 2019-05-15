package cz.mxmx.memoryanalyzer.model.memorywaste;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;

import java.util.List;

/**
 * List of duplicates waste. The found instances are all lists and their content has the exact same value:
 * L1: A A A A A A -> one waste,
 * L2: B B B B B B -> another waste.
 */
public class ListOfDuplicatesWaste implements Waste {

	/**
	 * Title template.
	 */
	private static final String TITLE_TEMPLATE = "List full of same values";

	/**
	 * Description template.
	 */
	private static final String DESC_TEMPLATE = "All values in the list %s#%s have the same value (%dx).";

	/**
	 * the source analyzer.
	 */
	protected final WasteAnalyzer sourceWasteAnalyzer;

	/**
	 * Instance holding the list.
	 */
	protected final InstanceDump instance;

	/**
	 * Field holding the list.
	 */
	private final InstanceFieldDump field;

	/**
	 * Size of the list.
	 */
	protected final long size;

	/**
	 * Creates the list of duplicates waste.
	 * @param sourceWasteAnalyzer Source analyzer.
	 * @param instance Instance holding the list.
	 * @param field Field holding the list.
	 * @param size Size of the problematic list.
	 */
	public ListOfDuplicatesWaste(WasteAnalyzer sourceWasteAnalyzer, InstanceDump instance, InstanceFieldDump field, long size) {
		this.sourceWasteAnalyzer = sourceWasteAnalyzer;
		this.instance = instance;
		this.field = field;
		this.size = size;
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
	public String getNominal() {
		return String.format("%d", this.size);
	}

	@Override
	public String getSource() {
		return String.format("%s#%s", this.instance.getClassDump().getName(), this.field.getName());
	}

	@Override
	public int compareTo(Waste o) {
		if(o instanceof  ListOfDuplicatesWaste) {
			return Long.compare(((ListOfDuplicatesWaste) o).size, this.size);
		}

		return 0;
	}
}
