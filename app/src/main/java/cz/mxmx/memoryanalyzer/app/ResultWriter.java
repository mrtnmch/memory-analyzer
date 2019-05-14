package cz.mxmx.memoryanalyzer.app;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.List;

public interface ResultWriter {
	void write(MemoryDump memoryDump);
	void write(List<Waste> wasteList, WasteAnalyzerPipeline wasteAnalyzer, boolean printFields);
	void close();
}
