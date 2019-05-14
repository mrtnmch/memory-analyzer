package cz.mxmx.memoryanalyzer.model.memorywaste;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.List;

public interface Waste extends Comparable<Waste> {
	Long estimateWastedBytes();
	String getTitle();
	String getDescription();
	List<InstanceDump> getAffectedInstances();
	void addAffectedInstance(InstanceDump instanceDump);
	WasteAnalyzer getSourceWasteAnalyzer();
	String getNominal();
	String getSource();
}
