package cz.mxmx.memoryanalyzer.memorywaste;

import java.util.ArrayList;
import java.util.List;

public class DefaultWasteAnalyzerPipeline extends WasteAnalyzerPipeline {

	private static final List<WasteAnalyzer> ANALYZERS = new ArrayList<WasteAnalyzer>() {{
		add(new DuplicateInstanceWasteAnalyzer());
//		add(new ListWasteAnalyzer());
	}};

	public DefaultWasteAnalyzerPipeline() {
		super(ANALYZERS, true);
	}
}
