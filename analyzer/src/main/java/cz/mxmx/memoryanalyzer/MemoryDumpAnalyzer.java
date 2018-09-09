package cz.mxmx.memoryanalyzer;

import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;

import java.io.FileNotFoundException;

public interface MemoryDumpAnalyzer {
	MemoryDump analyze(String path) throws FileNotFoundException, MemoryDumpAnalysisException;
}
