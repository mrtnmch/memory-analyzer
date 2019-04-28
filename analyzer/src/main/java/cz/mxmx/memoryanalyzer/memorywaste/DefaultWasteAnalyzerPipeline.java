package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWasteAnalyzerPipeline extends WasteAnalyzerPipeline {

	private static final Map<WasteAnalyzer, String> ANALYZERS = new HashMap<WasteAnalyzer, String>() {{
		put(new DuplicateInstanceWasteAnalyzer(), "Duplicate instances");
		put(new ListWasteAnalyzer(), "Ineffective list usage");
	}};

	public DefaultWasteAnalyzerPipeline() {
		super(ANALYZERS, true);
	}
}
