package cz.mxmx.memoryanalyzer.app;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.DefaultMemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;

import java.io.IOException;
import java.util.List;

public class ConsoleApp {

	private static final List<String> NAMESPACES = Lists.newArrayList("cz.mxmx");

	public static void main(String[] args) throws IOException, MemoryDumpAnalysisException {
		MemoryDumpAnalyzer memoryDumpAnalyzer = new DefaultMemoryDumpAnalyzer(NAMESPACES);
		MemoryDump parsedDump = memoryDumpAnalyzer.analyze("sandbox/data/app3.hprof");
	}
}