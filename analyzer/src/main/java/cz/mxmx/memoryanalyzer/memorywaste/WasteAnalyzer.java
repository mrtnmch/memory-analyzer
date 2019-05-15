package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.List;

/**
 * Waste analyzers looks for a memory waste in the given memory dump.
 */
public interface WasteAnalyzer {

	/**
	 * Process the given memory dump and get the found waste as the result.
	 * @param memoryDump Memory dump to process.
	 * @return Found memory waste.
	 */
	List<Waste> findMemoryWaste(MemoryDump memoryDump);
}
