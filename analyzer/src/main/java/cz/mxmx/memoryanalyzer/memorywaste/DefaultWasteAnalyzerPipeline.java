package cz.mxmx.memoryanalyzer.memorywaste;

import java.util.HashMap;
import java.util.Map;

/**
 * Default pipeline with memory dump analyzers.
 */
public class DefaultWasteAnalyzerPipeline extends WasteAnalyzerPipeline {

	/**
	 * Analyzers registered here will be automatically used to process a memory dump.
	 */
	private static final Map<WasteAnalyzer, String> ANALYZERS = new HashMap<WasteAnalyzer, String>() {{
		put(new DuplicateInstanceWasteAnalyzer(), "Duplicate instances");
		put(new ListWasteAnalyzer(), "Ineffective list usage");
		put(new ListOfDuplicatesAnalyzer(), "List of duplicates");
	}};

	/**
	 * Set to true of the analysis should be run in multiple threads.
	 */
	private static final boolean RUN_MULTI_THREADED = true;

	public DefaultWasteAnalyzerPipeline() {
		super(ANALYZERS, RUN_MULTI_THREADED);
	}
}
