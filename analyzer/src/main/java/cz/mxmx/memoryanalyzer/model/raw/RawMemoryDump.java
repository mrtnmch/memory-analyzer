package cz.mxmx.memoryanalyzer.model.raw;

import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Raw representation of a whole memory dump.
 */
public class RawMemoryDump {

	/**
	 * Dump's header.
	 */
	private RawDumpHeader rawDumpHeader;

	/**
	 * Strings in the dump and their IDs as a key.
	 */
	private Map<Long, String> stringMap;

	/**
	 * Class info in the dump as their IDs as a key.
	 */
	private Map<Long, ClassInfo> classMap;

	/**
	 * Raw "load class" dumps as ID of the classes as a key.
	 */
	private Map<Long, RawLoadClassDump> rawLoadClassDumps;

	/**
	 * Classes in the memory dump and their IDs as a key.
	 */
	private Map<Long, RawClassDump> rawClassDumps;

	/**
	 * Instances in the memory dump and their IDs as a key.
	 */
	private Map<Long, RawInstanceDump> rawInstanceDumps;

	/**
	 * Primitive arrays in the memory dump and their IDs as a key.
	 */
	private Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps;

	/**
	 * Object type arrays in the memory dump and their IDs as a key.
	 */
	private Map<Long, RawObjectArrayDump> rawObjectArrayDumps;

	/**
	 * Summary of the memory dump.
	 */
	private List<RawHeapSummary> rawHeapSummaries = new ArrayList<>();

	/**
	 * A list of the alloc site parents (and from them, the individual alloc sites).
	 */
	private List<RawAllocSiteParent> rawAllocSiteParents = new ArrayList<>();

	/**
	 * A list of stack traces.
	 */
	private List<RawStackTrace> rawStackTraces = new ArrayList<>();

	/**
	 * A list of stack frames of the {@link #rawStackTraces}.
	 */
	private List<RawStackFrame> rawStackFrames = new ArrayList<>();

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

	public List<RawHeapSummary> getRawHeapSummaries() {
		return rawHeapSummaries;
	}

	public List<RawAllocSiteParent> getRawAllocSiteParents() {
		return rawAllocSiteParents;
	}

	public List<RawStackTrace> getRawStackTraces() {
		return rawStackTraces;
	}

	public List<RawStackFrame> getRawStackFrames() {
		return rawStackFrames;
	}

	public RawMemoryDump setRawDumpHeader(RawDumpHeader rawDumpHeader) {
		this.rawDumpHeader = rawDumpHeader;
		return this;
	}

	public RawMemoryDump setStringMap(Map<Long, String> stringMap) {
		this.stringMap = stringMap;
		return this;
	}

	public RawMemoryDump setClassMap(Map<Long, ClassInfo> classMap) {
		this.classMap = classMap;
		return this;
	}

	public RawMemoryDump setRawLoadClassDumps(Map<Long, RawLoadClassDump> rawLoadClassDumps) {
		this.rawLoadClassDumps = rawLoadClassDumps;
		return this;
	}

	public RawMemoryDump setRawClassDumps(Map<Long, RawClassDump> rawClassDumps) {
		this.rawClassDumps = rawClassDumps;
		return this;
	}

	public RawMemoryDump setRawInstanceDumps(Map<Long, RawInstanceDump> rawInstanceDumps) {
		this.rawInstanceDumps = rawInstanceDumps;
		return this;
	}

	public RawMemoryDump setRawPrimitiveArrayDumps(Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps) {
		this.rawPrimitiveArrayDumps = rawPrimitiveArrayDumps;
		return this;
	}

	public RawMemoryDump setRawObjectArrayDumps(Map<Long, RawObjectArrayDump> rawObjectArrayDumps) {
		this.rawObjectArrayDumps = rawObjectArrayDumps;
		return this;
	}

	public RawMemoryDump setRawHeapSummaries(List<RawHeapSummary> rawHeapSummaries) {
		this.rawHeapSummaries = rawHeapSummaries;
		return this;
	}

	public RawMemoryDump setRawAllocSiteParents(List<RawAllocSiteParent> rawAllocSiteParents) {
		this.rawAllocSiteParents = rawAllocSiteParents;
		return this;
	}

	public RawMemoryDump setRawStackTraces(List<RawStackTrace> rawStackTraces) {
		this.rawStackTraces = rawStackTraces;
		return this;
	}

	public RawMemoryDump setRawStackFrames(List<RawStackFrame> rawStackFrames) {
		this.rawStackFrames = rawStackFrames;
		return this;
	}
}
