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

public class DefaultMemoryDumpAnalyzer implements MemoryDumpAnalyzer {

	private final RecordHandler recordHandler;
	private final String path;
	private final MemoryDumpProcessor genericProcessor = new GenericMemoryDumpProcessor();
	private RawMemoryDump memoryDump;

	public DefaultMemoryDumpAnalyzer(String path) {
		this(path, new RawRecordHandler());
	}

	public DefaultMemoryDumpAnalyzer(String path, RecordHandler recordHandler) {
		this.path = path;
		this.recordHandler = recordHandler;
	}

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
