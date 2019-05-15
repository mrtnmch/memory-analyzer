package cz.mxmx.memoryanalyzer.parse;

import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import edu.tufts.eaftan.hprofparser.handler.NullRecordHandler;

import java.util.List;

/**
 * An abstract record handler adds an aggregation fetching to {@link NullRecordHandler}.
 */
public abstract class RecordHandler extends NullRecordHandler {

	/**
	 * Returns aggregated results of the memory dump.
	 * @return Aggregated results.
	 */
	public abstract RawMemoryDump getMemoryDump();
}
