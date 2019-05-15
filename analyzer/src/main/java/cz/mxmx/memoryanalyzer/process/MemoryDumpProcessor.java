package cz.mxmx.memoryanalyzer.process;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;

/**
 * Processor to translate raw memory dumps into processed ones.
 */
public interface MemoryDumpProcessor {

	/**
	 * Process a raw memory dump into processed one.
	 * @param rawMemoryDump Raw memory dump.
	 * @return Processed memory dump.
	 */
	MemoryDump process(RawMemoryDump rawMemoryDump);
}
