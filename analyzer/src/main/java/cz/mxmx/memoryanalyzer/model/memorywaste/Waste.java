package cz.mxmx.memoryanalyzer.model.memorywaste;

import cz.mxmx.memoryanalyzer.model.InstanceDump;

import java.util.List;

public interface Waste {
	Long estimateWastedBytes();
	String getTitle();
	String getDescription();
	List<InstanceDump> getAffectedInstances();
	void addAffectedInstance(InstanceDump instanceDump);
}
