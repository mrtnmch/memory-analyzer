package cz.mxmx.memoryanalyzer.parse;

import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;

import java.util.List;

public abstract class RecordHandler extends NullRecordHandler{
	public abstract RawMemoryDump getMemoryDump();
	public abstract List<String> getNamespaces();
}
