package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.List;

public interface WasteAnalyzer {
	List<Waste> findMemoryWaste(MemoryDump memoryDump);
}
