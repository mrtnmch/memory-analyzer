package cz.mxmx.memoryanalyzer.parse;

import cz.mxmx.memoryanalyzer.model.raw.RawClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawDumpHeader;
import cz.mxmx.memoryanalyzer.model.raw.RawInstanceDump;
import cz.mxmx.memoryanalyzer.model.raw.RawLoadClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawObjectArrayDump;
import cz.mxmx.memoryanalyzer.model.raw.RawPrimitiveArrayDump;
import edu.tufts.eaftan.hprofparser.parser.datastructures.AllocSite;
import edu.tufts.eaftan.hprofparser.parser.datastructures.ClassInfo;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Constant;
import edu.tufts.eaftan.hprofparser.parser.datastructures.InstanceField;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Static;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

	private static String getBasicType(byte type) {
		return TYPE_TRANSLATION_MAP.get(new Byte(type).intValue());
	}

	@Override
	public RawMemoryDump getMemoryDump() {
		return new RawMemoryDump(
				this.rawDumpHeader,
				this.stringMap,
				this.classMap,
				this.rawLoadClassDumps,
				this.rawClassDumps,
				this.rawInstanceDumps,
				this.rawPrimitiveArrayDumps,
				this.rawObjectArrayDumps
		);
	}

	public void header(String format, int idSize, long time) {
		this.rawDumpHeader = new RawDumpHeader(format, idSize, time);
	}

	public void stringInUTF8(long id, String data) {
		this.stringMap.put(id, data);
	}

	public void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum, long classNameStringId) {
		this.rawLoadClassDumps.put(classObjId, new RawLoadClassDump(classObjId, this.stringMap.get(classNameStringId)));
	}

	public void allocSites(short bitMaskFlags, float cutoffRatio, int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated, AllocSite[] sites) {
		/*System.out.println("Alloc Sites:");
		System.out.println("    bit mask flags: " + bitMaskFlags);
		System.out.println("    incremental vs. complete: " + testBitMask(bitMaskFlags, 1));
		System.out.println("    sorted by allocation vs. line: " + testBitMask(bitMaskFlags, 2));
		System.out.println("    whether to force GC: " + testBitMask(bitMaskFlags, 4));
		System.out.println("    cutoff ratio: " + cutoffRatio);
		System.out.println("    total live bytes: " + totalLiveBytes);
		System.out.println("    total live instances: " + totalLiveInstances);
		System.out.println("    total bytes allocated: " + totalBytesAllocated);
		System.out.println("    total instances allocated: " + totalInstancesAllocated);

		for(int i = 0; i < sites.length; ++i) {
			System.out.println("        alloc site " + (i + 1) + ":");
			System.out.print("            array indicator: ");
			if (sites[i].arrayIndicator == 0) {
				System.out.println("not an array");
			} else {
				System.out.println("array of " + getBasicType(sites[i].arrayIndicator));
			}

			System.out.println("            class serial num: " + sites[i].classSerialNum);
			System.out.println("            stack trace serial num: " + sites[i].stackTraceSerialNum);
			System.out.println("            num live bytes: " + sites[i].numLiveBytes);
			System.out.println("            num live instances: " + sites[i].numLiveInstances);
			System.out.println("            num bytes allocated: " + sites[i].numBytesAllocated);
			System.out.println("            num instances allocated: " + sites[i].numInstancesAllocated);
		}
*/
	}

	public void heapSummary(int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated) {
		/*System.out.println("Heap Summary:");
		System.out.println("    total live bytes: " + totalLiveBytes);
		System.out.println("    total live instances: " + totalLiveInstances);
		System.out.println("    total bytes allocated: " + totalBytesAllocated);
		System.out.println("    total instances allocated: " + totalInstancesAllocated);*/
	}

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

	public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId, long[] elems) {
		RawObjectArrayDump arrayDummy = new RawObjectArrayDump(objId, elemClassObjId);
		this.rawObjectArrayDumps.put(objId, arrayDummy);

		for (int i = 0; i < elems.length; ++i) {
			arrayDummy.addItem(elems[i]);
		}

	}

	public void primArrayDump(long objId, int stackTraceSerialNum, byte elemType, Value<?>[] elems) {
		RawPrimitiveArrayDump arrayDummy = new RawPrimitiveArrayDump(objId, getBasicType(elemType));
		this.rawPrimitiveArrayDumps.put(objId, arrayDummy);

		for (int i = 0; i < elems.length; ++i) {
			arrayDummy.addItem(elems[i]);
		}

	}
}
