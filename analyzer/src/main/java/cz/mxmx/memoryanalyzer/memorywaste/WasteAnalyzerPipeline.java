package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WasteAnalyzerPipeline implements WasteAnalyzer {

	private final List<WasteAnalyzer> analyzers;

	public WasteAnalyzerPipeline(List<WasteAnalyzer> analyzers) {
		this.analyzers = analyzers;
	}

	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		return this.analyzers
				.stream()
				.map(analyzer -> analyzer.findMemoryWaste(memoryDump))
				.reduce(
						new ArrayList<>(),
						(result1, result2) -> Stream.concat(result1.stream(), result2.stream()).collect(Collectors.toList())
				);
	}
}
