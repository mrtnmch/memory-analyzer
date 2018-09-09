package cz.mxmx.memoryanalyzer;

import com.google.common.collect.Lists;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.parse.RawRecordHandler;
import cz.mxmx.memoryanalyzer.parse.RecordHandler;
import cz.mxmx.memoryanalyzer.process.DefaultMemoryDumpProcessor;
import cz.mxmx.memoryanalyzer.process.MemoryDumpProcessor;
import cz.mxmx.memoryanalyzer.util.Normalization;
import edu.tufts.eaftan.hprofparser.parser.HprofParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DefaultMemoryDumpAnalyzer implements MemoryDumpAnalyzer {

    private final Collection<String> namespaces;
    private final RecordHandler recordHandler;

	public DefaultMemoryDumpAnalyzer(String namespace) {
		this(Lists.newArrayList(namespace));
    }

	public DefaultMemoryDumpAnalyzer(String namespace, RecordHandler recordHandler) {
		this(Lists.newArrayList(namespace), recordHandler);
    }

	public DefaultMemoryDumpAnalyzer(Collection<String> namespaces) {
		this(namespaces, new RawRecordHandler());
	}

	public DefaultMemoryDumpAnalyzer(Collection<String> namespaces, RecordHandler recordHandler) {
		this.namespaces = new ArrayList<>(namespaces);
        this.recordHandler = recordHandler;
    }

    @Override
    public MemoryDump analyze(String path) throws FileNotFoundException, MemoryDumpAnalysisException {
	    HprofParser parser = new HprofParser(this.recordHandler);
	    FileInputStream fs = new FileInputStream(path);
	    DataInputStream in = new DataInputStream(new BufferedInputStream(fs));

	    try {
		    parser.parse(in);
		    in.close();
	    } catch (IOException e) {
		    throw new MemoryDumpAnalysisException(e);
	    }

	    RawMemoryDump memoryDump = this.recordHandler.getMemoryDump();
	    MemoryDumpProcessor processor = new DefaultMemoryDumpProcessor(Normalization.stringToRegexNamespaces(this.namespaces));
	    return processor.process(memoryDump);
    }
}
