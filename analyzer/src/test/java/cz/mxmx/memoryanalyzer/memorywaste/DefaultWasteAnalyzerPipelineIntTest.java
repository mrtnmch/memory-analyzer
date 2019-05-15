package cz.mxmx.memoryanalyzer.memorywaste;

import com.google.common.collect.Lists;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.TestHelper;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.mxmx.memoryanalyzer.TestHelper.NAMESPACE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultWasteAnalyzerPipelineIntTest {

	private MemoryDumpAnalyzer analyzer;
	private DefaultWasteAnalyzerPipeline wasteAnalyzerPipeline;

	@Before
	public void setUp() {
		this.analyzer = TestHelper.getMemoryDumpAnalyzer();
		this.wasteAnalyzerPipeline = new DefaultWasteAnalyzerPipeline();
	}

	@Test
	public void testSize() throws FileNotFoundException, MemoryDumpAnalysisException {
		List<Waste> memoryWaste = this.wasteAnalyzerPipeline.findMemoryWaste(this.analyzer.analyze(Lists.newArrayList(NAMESPACE)));
		assertThat(memoryWaste.size(), is(15));
	}

	@Test
	public void testIndividualSize() throws FileNotFoundException, MemoryDumpAnalysisException {
		List<Waste> memoryWaste = this.wasteAnalyzerPipeline.findMemoryWaste(this.analyzer.analyze(Lists.newArrayList(NAMESPACE)));

		for (Waste waste : memoryWaste) {
			if(waste.getSourceWasteAnalyzer() instanceof DuplicateInstanceWasteAnalyzer) {
				assertThat(waste.getAffectedInstances().size(), is(10));
			} else if(waste.getSourceWasteAnalyzer() instanceof ListWasteAnalyzer) {
				assertThat(waste.getAffectedInstances().size(), is(1));
			} else if(waste.getSourceWasteAnalyzer() instanceof ListOfDuplicatesAnalyzer) {
				assertThat(waste.getAffectedInstances().size(), is(1));
			}
		}
	}

	@Test
	public void testCombinedSize() throws FileNotFoundException, MemoryDumpAnalysisException {
		List<Waste> memoryWaste = this.wasteAnalyzerPipeline.findMemoryWaste(this.analyzer.analyze(Lists.newArrayList(NAMESPACE)));
		Map<WasteAnalyzer, Integer> counts = new HashMap<>();

		for (Waste waste : memoryWaste) {
			WasteAnalyzer source = waste.getSourceWasteAnalyzer();

			if(!counts.containsKey(source)) {
				counts.put(source, 0);
			}

			counts.put(source, counts.get(source) + 1);
		}

		assertThat(counts.size(), is(3));

		counts.forEach((k, v) -> {
			if(k instanceof DuplicateInstanceWasteAnalyzer) {
				assertThat(v, is(10));
			} else if(k instanceof ListWasteAnalyzer) {
				assertThat(v, is(4));
			} else if(k instanceof ListOfDuplicatesAnalyzer) {
				assertThat(v, is(1));
			}
		});
	}

}