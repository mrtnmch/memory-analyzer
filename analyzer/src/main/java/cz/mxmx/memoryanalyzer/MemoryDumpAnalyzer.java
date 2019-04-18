package cz.mxmx.memoryanalyzer;

import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public interface MemoryDumpAnalyzer {
	Set<String> getNamespaces() throws FileNotFoundException, MemoryDumpAnalysisException;
	MemoryDump analyze(List<String> namespaces) throws FileNotFoundException, MemoryDumpAnalysisException;
}
