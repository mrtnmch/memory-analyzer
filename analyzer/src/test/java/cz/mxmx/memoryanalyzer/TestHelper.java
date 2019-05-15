package cz.mxmx.memoryanalyzer;

public class TestHelper {
	public static final String PATH = "../sandbox/data/test-heapdump-10.hprof";
	public static final String NAMESPACE = "cz.mxmx.memoryanalyzer.example";

	public static MemoryDumpAnalyzer getMemoryDumpAnalyzer() {
		return new DefaultMemoryDumpAnalyzer(PATH);
	}
}
