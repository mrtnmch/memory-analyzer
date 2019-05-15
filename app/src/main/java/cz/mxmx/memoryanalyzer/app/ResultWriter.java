package cz.mxmx.memoryanalyzer.app;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.List;

/**
 * Writer to handle processing results.
 */
public interface ResultWriter {

	/**
	 * Write memory dump information.
	 * @param memoryDump Memory dump.
	 */
	void write(MemoryDump memoryDump);

	/**
	 * Write list of memory waste.
	 * @param wasteList Memory waste.
	 * @param wasteAnalyzer Analyzer pipeline.
	 * @param printFields True if the fields should be printed out.
	 */
	void write(List<Waste> wasteList, WasteAnalyzerPipeline wasteAnalyzer, boolean printFields);

	/**
	 * Close the writer.
	 */
	void close();
}
