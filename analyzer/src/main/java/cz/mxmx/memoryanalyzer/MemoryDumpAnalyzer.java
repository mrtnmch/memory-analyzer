package cz.mxmx.memoryanalyzer;

import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

/**
 * Memory dump analyzer.
 */
public interface MemoryDumpAnalyzer {

	/**
	 * Get namespaces from the given dump.
	 * @return List of namespaces in a dump.
	 * @throws FileNotFoundException Dump not found.
	 * @throws MemoryDumpAnalysisException An exception during processing.
	 */
	Set<String> getNamespaces() throws FileNotFoundException, MemoryDumpAnalysisException;

	/**
	 * Get a processed dump of the given namespaces.
	 * @param namespaces Namespaces to use as a filter.
	 * @return Processed dump.
	 * @throws FileNotFoundException Dump not found.
	 * @throws MemoryDumpAnalysisException An exception during processing.
	 */
	MemoryDump analyze(List<String> namespaces) throws FileNotFoundException, MemoryDumpAnalysisException;
}
