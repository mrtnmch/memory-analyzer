package cz.mxmx.memoryanalyzer;

import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.parse.RawRecordHandler;
import cz.mxmx.memoryanalyzer.parse.RecordHandler;
import cz.mxmx.memoryanalyzer.process.GenericMemoryDumpProcessor;
import cz.mxmx.memoryanalyzer.process.FilteredMemoryDumpProcessor;
import cz.mxmx.memoryanalyzer.process.MemoryDumpProcessor;
import cz.mxmx.memoryanalyzer.util.Normalization;
import edu.tufts.eaftan.hprofparser.parser.HprofParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default memory analyzer implementation.
 */
public class DefaultMemoryDumpAnalyzer implements MemoryDumpAnalyzer {

	/**
	 * Memory handler to use in the Hprof Library.
	 */
	private final RecordHandler recordHandler;

	/**
	 * Path to the dump file.
	 */
	private final String path;

	/**
	 * Generic (raw) processor.
	 */
	private final MemoryDumpProcessor genericProcessor = new GenericMemoryDumpProcessor();

	/**
	 * Raw memory dump after loading.
	 */
	private RawMemoryDump memoryDump;

	/**
	 * Creates an analyzer of the given dump with default {@link RawRecordHandler}.
	 * @param path Path to the dump.
	 */
	public DefaultMemoryDumpAnalyzer(String path) {
		this(path, new RawRecordHandler());
	}

	/**
	 * CReates an analyzer of the given dump.
	 * @param path Path to the dump.
	 * @param recordHandler Record handler.
	 */
	public DefaultMemoryDumpAnalyzer(String path, RecordHandler recordHandler) {
		this.path = path;
		this.recordHandler = recordHandler;
	}

	/**
	 * Run an analysis of the given dump.
	 * @throws MemoryDumpAnalysisException Memory dump analysis problem.
	 * @throws FileNotFoundException Dump not found.
	 */
	private void runAnalysis() throws MemoryDumpAnalysisException, FileNotFoundException {
		if (this.memoryDump == null) {
			HprofParser parser = new HprofParser(this.recordHandler);
			FileInputStream fs = new FileInputStream(path);
			DataInputStream in = new DataInputStream(new BufferedInputStream(fs));

			try {
				parser.parse(in);
				in.close();
			} catch (IOException e) {
				throw new MemoryDumpAnalysisException(e);
			}

			this.memoryDump = this.recordHandler.getMemoryDump();
		}
	}

	@Override
	public Set<String> getNamespaces() throws FileNotFoundException, MemoryDumpAnalysisException {
		this.runAnalysis();
		MemoryDump dump = this.genericProcessor.process(this.memoryDump);
		Set<String> namespaces = new HashSet<>();

		dump.getClasses().forEach((id, cl) -> {
			namespaces.add(cl.getName());
		});

		return this.filterNamespaces(namespaces);
	}

	/**
	 * Translates namespaces into their regexes.
	 * @param namespaces Namespaces.
	 * @return Namespace regex representation.
	 */
	private Set<String> filterNamespaces(Set<String> namespaces) {
		return namespaces
				.stream()
				.filter(namespace -> !namespace.contains("$") && !namespace.startsWith("["))
				.map(namespace -> namespace.contains(".") ? namespace.substring(0, namespace.lastIndexOf(".")) : namespace)
				.collect(Collectors.toSet());
	}

	@Override
	public MemoryDump analyze(List<String> namespaces) throws FileNotFoundException, MemoryDumpAnalysisException {
		this.runAnalysis();
		MemoryDumpProcessor processor = new FilteredMemoryDumpProcessor(this.genericProcessor, Normalization.stringToRegexNamespaces(namespaces));
		return processor.process(this.memoryDump);
	}
}
