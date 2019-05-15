package cz.mxmx.memoryanalyzer.model.memorywaste;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.List;

/**
 * Representation of memory waste.
 */
public interface Waste extends Comparable<Waste> {
	/**
	 * Returns the estimated wasted bytes by the given waste instance.
	 * @return Number of bytes.
	 */
	Long estimateWastedBytes();

	/**
	 * Returns the title of the waste.
	 * @return Waste title.
	 */
	String getTitle();

	/**
	 * Returns the description of the waste.
	 * @return Waste description.
	 */
	String getDescription();

	/**
	 * Return a list of the instances affected by this memory waste.
	 * @return List of the instances.
	 */
	List<InstanceDump> getAffectedInstances();

	/**
	 * Add an instance to the given memory waste and mark it as affected by it.
	 * @param instanceDump Instance to add.
	 */
	void addAffectedInstance(InstanceDump instanceDump);

	/**
	 * Get the analyzer which is the source of the waste.
	 * @return Source analyzer.
	 */
	WasteAnalyzer getSourceWasteAnalyzer();

	/**
	 * Return the nominal value of the number (or severity) of the waste.
	 * @return Nominal representation of the waste.
	 */
	String getNominal();

	/**
	 * Return the source of the waste (ie. the class, field or instance of its origin).
	 * @return Waste origin.
	 */
	String getSource();
}
