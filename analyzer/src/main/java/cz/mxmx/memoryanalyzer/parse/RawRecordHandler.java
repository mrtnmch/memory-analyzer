package cz.mxmx.memoryanalyzer.parse;

import cz.mxmx.memoryanalyzer.model.raw.RawAllocSite;
import cz.mxmx.memoryanalyzer.model.raw.RawAllocSiteParent;
import cz.mxmx.memoryanalyzer.model.raw.RawClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawDumpHeader;
import cz.mxmx.memoryanalyzer.model.raw.RawHeapSummary;
import cz.mxmx.memoryanalyzer.model.raw.RawInstanceDump;
import cz.mxmx.memoryanalyzer.model.raw.RawLoadClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawObjectArrayDump;
import cz.mxmx.memoryanalyzer.model.raw.RawPrimitiveArrayDump;
import cz.mxmx.memoryanalyzer.model.raw.RawStackFrame;
import cz.mxmx.memoryanalyzer.model.raw.RawStackTrace;
import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RawRecordHandler extends RecordHandler {

	private static final Map<Integer, String> TYPE_TRANSLATION_MAP = new HashMap<Integer, String>() {{
		put(2, "object");
		put(3, null);
		put(4, "boolean");
		put(5, "char");
		put(6, "float");
		put(7, "double");
		put(8, "byte");
		put(9, "short");
		put(10, "int");
		put(11, "long");
	}};

	private RawDumpHeader rawDumpHeader;
	private Map<Long, String> stringMap = new HashMap<>();
	private Map<Long, ClassInfo> classMap = new HashMap<>();
	private Map<Long, RawLoadClassDump> rawLoadClassDumps = new HashMap<>();
	private Map<Long, RawClassDump> rawClassDumps = new HashMap<>();
	private Map<Long, RawInstanceDump> rawInstanceDumps = new HashMap<>();
	private Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps = new HashMap<>();
	private Map<Long, RawObjectArrayDump> rawObjectArrayDumps = new HashMap<>();
	private List<RawHeapSummary> rawHeapSummaries = new ArrayList<>();
	private List<RawAllocSiteParent> rawAllocSiteParents = new ArrayList<>();
	private List<RawStackTrace> rawStackTraces = new ArrayList<>();
	private List<RawStackFrame> rawStackFrames = new ArrayList<>();

	private static String getBasicType(byte type) {
		return TYPE_TRANSLATION_MAP.get(new Byte(type).intValue());
	}

	@Override
	public RawMemoryDump getMemoryDump() {
		return new RawMemoryDump()
				.setRawDumpHeader(this.rawDumpHeader)
				.setStringMap(this.stringMap)
				.setClassMap(this.classMap)
				.setRawLoadClassDumps(this.rawLoadClassDumps)
				.setRawClassDumps(this.rawClassDumps)
				.setRawInstanceDumps(this.rawInstanceDumps)
				.setRawPrimitiveArrayDumps(this.rawPrimitiveArrayDumps)
				.setRawObjectArrayDumps(this.rawObjectArrayDumps)
				.setRawHeapSummaries(this.rawHeapSummaries)
				.setRawAllocSiteParents(this.rawAllocSiteParents)
				.setRawStackTraces(this.rawStackTraces)
				.setRawStackFrames(this.rawStackFrames);
	}

	@Override
	public List<String> getNamespaces() {
		return null;
	}

	public void header(String format, int idSize, long time) {
		this.rawDumpHeader = new RawDumpHeader(format, idSize, time);
	}

	@Override
	public void stringInUTF8(long id, String data) {
		this.stringMap.put(id, data);
	}

	@Override
	public void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum, long classNameStringId) {
		this.rawLoadClassDumps.put(classObjId, new RawLoadClassDump(classObjId, this.stringMap.get(classNameStringId), classSerialNum));
	}

	@Override
	public void allocSites(short bitMaskFlags, float cutoffRatio, int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated, AllocSite[] sites) {
		List<RawAllocSite> allocSites = Arrays
				.stream(sites)
				.map(s -> new RawAllocSite(
						s.arrayIndicator,
						s.classSerialNum,
						s.stackTraceSerialNum,
						s.numLiveBytes,
						s.numLiveInstances,
						s.numBytesAllocated,
						s.numInstancesAllocated)
				).collect(Collectors.toList());

		this.rawAllocSiteParents.add(
				new RawAllocSiteParent(
						bitMaskFlags,
						cutoffRatio,
						totalLiveBytes,
						totalLiveInstances,
						totalBytesAllocated,
						totalInstancesAllocated,
						allocSites
				)
		);
	}

	@Override
	public void heapSummary(int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated) {
		this.rawHeapSummaries.add(new RawHeapSummary(totalLiveBytes, totalLiveInstances, totalBytesAllocated, totalInstancesAllocated));
	}

	@Override
	public void classDump(long classObjId, int stackTraceSerialNum, long superClassObjId, long classLoaderObjId, long signersObjId, long protectionDomainObjId, long reserved1, long reserved2, int instanceSize, Constant[] constants, Static[] statics, InstanceField[] instanceFields) {
		RawClassDump dummy = new RawClassDump(classObjId, superClassObjId, instanceSize);
		this.rawClassDumps.put(classObjId, dummy);
		int var21 = constants.length;

		int var22;
		for (var22 = 0; var22 < var21; ++var22) {
			Constant c = constants[var22];
			dummy.addConstantField(c.constantPoolIndex, c.value);
		}

		var21 = statics.length;

		for (var22 = 0; var22 < var21; ++var22) {
			Static s = statics[var22];
			dummy.addStaticField(this.stringMap.get(s.staticFieldNameStringId), s.value);
		}

		var21 = instanceFields.length;

		for (var22 = 0; var22 < var21; ++var22) {
			InstanceField i = instanceFields[var22];
			dummy.addInstanceField(this.stringMap.get(i.fieldNameStringId), i.type.toString());
		}

		this.classMap.put(classObjId, new ClassInfo(classObjId, superClassObjId, instanceSize, instanceFields));
	}

	@Override
	public void instanceDump(long objId, int stackTraceSerialNum, long classObjId, Value<?>[] instanceFieldValues) {
		RawInstanceDump rawInstanceDump = new RawInstanceDump(objId, classObjId);
		this.rawInstanceDumps.put(objId, rawInstanceDump);

		if (instanceFieldValues.length > 0) {
			int i = 0;
			long nextClass = classObjId;

			while (nextClass != 0L) {
				ClassInfo ci = this.classMap.get(nextClass);
				nextClass = ci.superClassObjId;
				InstanceField[] var11 = ci.instanceFields;
				int var12 = var11.length;

				for (int var13 = 0; var13 < var12; ++var13) {
					InstanceField field = var11[var13];
					rawInstanceDump.addInstanceValue(this.stringMap.get(field.fieldNameStringId), instanceFieldValues[i]);
					++i;
				}
			}
		}

	}

	@Override
	public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId, long[] elems) {
		RawObjectArrayDump arrayDummy = new RawObjectArrayDump(objId, elemClassObjId);
		this.rawObjectArrayDumps.put(objId, arrayDummy);

		for (long elem : elems) {
			arrayDummy.addItem(elem);
		}

	}

	@Override
	public void primArrayDump(long objId, int stackTraceSerialNum, byte elemType, Value<?>[] elems) {
		RawPrimitiveArrayDump arrayDummy = new RawPrimitiveArrayDump(objId, getBasicType(elemType));
		this.rawPrimitiveArrayDumps.put(objId, arrayDummy);

		for (Value<?> elem : elems) {
			arrayDummy.addItem(elem);
		}

	}

	@Override
	public void stackFrame(long stackFrameId, long methodNameStringId, long methodSigStringId, long sourceFileNameStringId, int classSerialNum, int location) {
		this.rawStackFrames.add(new RawStackFrame(stackFrameId, methodNameStringId, methodSigStringId, sourceFileNameStringId, classSerialNum, location));
	}

	@Override
	public void stackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames, long[] stackFrameIds) {
		this.rawStackTraces.add(new RawStackTrace(stackTraceSerialNum, threadSerialNum, numFrames, stackFrameIds));
	}
}
