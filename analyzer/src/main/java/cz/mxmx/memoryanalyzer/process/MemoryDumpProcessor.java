package cz.mxmx.memoryanalyzer.process;

import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;

public interface MemoryDumpProcessor {
	MemoryDump process(RawMemoryDump rawMemoryDump);
}
