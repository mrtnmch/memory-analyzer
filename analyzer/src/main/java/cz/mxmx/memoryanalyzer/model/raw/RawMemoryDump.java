package cz.mxmx.memoryanalyzer.model.raw;

import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RawMemoryDump {
	private final RawDumpHeader rawDumpHeader;
	private final Map<Long, String> stringMap;
	private final Map<Long, ClassInfo> classMap;
	private final Map<Long, RawLoadClassDump> rawLoadClassDumps;
	private final Map<Long, RawClassDump> rawClassDumps;
	private final Map<Long, RawInstanceDump> rawInstanceDumps;
	private final Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps;
	private final Map<Long, RawObjectArrayDump> rawObjectArrayDumps;

	public RawMemoryDump(RawDumpHeader rawDumpHeader, Map<Long, String> stringMap, Map<Long, ClassInfo> classMap, Map<Long, RawLoadClassDump> rawLoadClassDumps, Map<Long, RawClassDump> rawClassDumps, Map<Long, RawInstanceDump> rawInstanceDumps, Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps, Map<Long, RawObjectArrayDump> rawObjectArrayDumps) {
		this.rawDumpHeader = rawDumpHeader;
		this.stringMap = stringMap;
		this.classMap = classMap;
		this.rawLoadClassDumps = rawLoadClassDumps;
		this.rawClassDumps = rawClassDumps;
		this.rawInstanceDumps = rawInstanceDumps;
		this.rawPrimitiveArrayDumps = rawPrimitiveArrayDumps;
		this.rawObjectArrayDumps = rawObjectArrayDumps;
	}

	public RawDumpHeader getRawDumpHeader() {
		return rawDumpHeader;
	}

	public Map<Long, String> getStringMap() {
		return stringMap;
	}

	public Map<Long, ClassInfo> getClassMap() {
		return classMap;
	}

	public Map<Long, RawLoadClassDump> getRawLoadClassDumps() {
		return rawLoadClassDumps;
	}

	public Map<Long, RawClassDump> getRawClassDumps() {
		return rawClassDumps;
	}

	public Map<Long, RawInstanceDump> getRawInstanceDumps() {
		return rawInstanceDumps;
	}

	public Map<Long, RawPrimitiveArrayDump> getRawPrimitiveArrayDumps() {
		return rawPrimitiveArrayDumps;
	}

	public Map<Long, RawObjectArrayDump> getRawObjectArrayDumps() {
		return rawObjectArrayDumps;
	}
}
