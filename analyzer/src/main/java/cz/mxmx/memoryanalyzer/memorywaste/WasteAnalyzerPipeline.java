package cz.mxmx.memoryanalyzer.memorywaste;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base implementation of waste analyzer pipeline. Offers basic functionality that can be extended by any future pipeline
 * to run analyzers in single or multiple threads.
 */
public abstract class WasteAnalyzerPipeline implements WasteAnalyzer {

	/**
	 * Analyzers in the pipeline and their description.
	 */
	private final Map<WasteAnalyzer, String> analyzers;

	/**
	 * True if the analysis should be run in multiple threads.
	 */
	private final boolean multiThreaded;

	/**
	 * Creates a new pipeline with the given analyzers.
	 * @param analyzers Analyzers of the new pipeline and their description.
	 * @param multiThreaded True if the analysis should be run in multiple threads.
	 */
	public WasteAnalyzerPipeline(Map<WasteAnalyzer, String> analyzers, boolean multiThreaded) {
		this.analyzers = analyzers;
		this.multiThreaded = multiThreaded;
	}

	@Override
	public List<Waste> findMemoryWaste(MemoryDump memoryDump) {
		return this.multiThreaded ? this.findMemoryWasteMultiThreaded(memoryDump) : this.findMemoryWasteSingleThreaded(memoryDump);
	}

	/**
	 * Runs the analysis in multiple threads.
	 * @param memoryDump Memory dump to analyse.
	 * @return Found waste.
	 */
	private List<Waste> findMemoryWasteMultiThreaded(MemoryDump memoryDump) {
		List<Waste> wasteList = new ArrayList<>();
		List<Thread> threads = new ArrayList<>();

		for (WasteAnalyzer analyzer : this.analyzers.keySet()) {
			Thread t = new Thread(() -> {
				List<Waste> memoryWaste = analyzer.findMemoryWaste(memoryDump);
				wasteList.addAll(memoryWaste);
			});

			threads.add(t);
			t.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return wasteList;
	}

	/**
	 * Runs the analysis in a single thread.
	 * @param memoryDump Memory dump to analyse.
	 * @return Found waste.
	 */
	private List<Waste> findMemoryWasteSingleThreaded(MemoryDump memoryDump) {
		return this.analyzers.keySet()
				.stream()
				.map(analyzer -> analyzer.findMemoryWaste(memoryDump))
				.reduce(
						new ArrayList<>(),
						(result1, result2) -> Stream.concat(result1.stream(), result2.stream()).collect(Collectors.toList())
				);
	}

	/**
	 * Returns the title (description) of the given analyzer.
	 * @param wasteAnalyzer Analyzer.
	 * @return Analyzer's title.
	 */
	public String getWasteTitle(WasteAnalyzer wasteAnalyzer) {
		return this.analyzers.get(wasteAnalyzer);
	}
}
