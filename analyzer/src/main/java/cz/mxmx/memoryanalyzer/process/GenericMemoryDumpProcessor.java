package cz.mxmx.memoryanalyzer.process;

import cz.mxmx.memoryanalyzer.model.AllocSite;
import cz.mxmx.memoryanalyzer.model.AllocSiteParent;
import cz.mxmx.memoryanalyzer.model.ArrayDump;
import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.DumpHeader;
import cz.mxmx.memoryanalyzer.model.InstanceArrayDump;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.ProcessedMemoryDump;
import cz.mxmx.memoryanalyzer.model.StackFrame;
import cz.mxmx.memoryanalyzer.model.StackTrace;
import cz.mxmx.memoryanalyzer.model.raw.RawAllocSite;
import cz.mxmx.memoryanalyzer.model.raw.RawAllocSiteParent;
import cz.mxmx.memoryanalyzer.model.raw.RawClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawDumpHeader;
import cz.mxmx.memoryanalyzer.model.raw.RawInstanceDump;
import cz.mxmx.memoryanalyzer.model.raw.RawLoadClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawObjectArrayDump;
import cz.mxmx.memoryanalyzer.model.raw.RawPrimitiveArrayDump;
import cz.mxmx.memoryanalyzer.model.raw.RawStackFrame;
import cz.mxmx.memoryanalyzer.model.raw.RawStackTrace;
import cz.mxmx.memoryanalyzer.util.Normalization;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Generic memory dump processor to handle translation of raw data into processed data.
 */
public class GenericMemoryDumpProcessor implements MemoryDumpProcessor {

	/**
	 * Cache to stop re-creating of the memory dumps.
	 */
	private final Map<RawMemoryDump, MemoryDump> cache = new HashMap<>();

	/**
	 * Translation map from string into class representation.
	 */
	private static final Map<String, Class<?>> TYPE_TRANSLATION_MAP = new HashMap<String, Class<?>>() {{
		put("object", Object.class);
		put(null, Void.class);
		put("boolean", Boolean.class);
		put("char", Character.class);
		put("float", Float.class);
		put("double", Double.class);
		put("byte", Byte.class);
		put("short", Short.class);
		put("int", Integer.class);
		put("long", Long.class);
	}};

	@Override
	public MemoryDump process(RawMemoryDump rawMemoryDump) {
		if (this.cache.containsKey(rawMemoryDump)) {
			return this.cache.get(rawMemoryDump);
		}

		DumpHeader dumpHeader = this.getDumpHeader(rawMemoryDump.getRawDumpHeader());
		Map<Long, ClassDump> classes = this.getClasses(rawMemoryDump);
		Map<Long, InstanceDump> instances = this.getInstances(rawMemoryDump, classes);
		Map<Long, ArrayDump> primitiveArrays = this.getPrimitiveArrays(rawMemoryDump.getRawPrimitiveArrayDumps());
		Map<Long, InstanceArrayDump> instanceArrays = this.getInstanceArrays(rawMemoryDump.getRawObjectArrayDumps(), instances, classes);
		List<AllocSiteParent> allocSites = this.getAllocSites(rawMemoryDump.getRawAllocSiteParents());
		List<StackTrace> stackTraces = this.getStackTraces(rawMemoryDump.getRawStackTraces(), rawMemoryDump.getRawStackFrames(), rawMemoryDump.getStringMap(), classes);

		ProcessedMemoryDump processedMemoryDump = new ProcessedMemoryDump()
				.setDumpHeader(dumpHeader)
				.setInstances(instances)
				.setClasses(classes)
				.setPrimitiveArrays(primitiveArrays)
				.setInstanceArrays(instanceArrays)
				.setAllocSites(allocSites)
				.setStackTraces(stackTraces);

		this.cache.put(rawMemoryDump, processedMemoryDump);

		return processedMemoryDump;
	}

	/**
	 * Translates raw alloc site parents into processed ones.
	 * @param rawAllocSiteParents Raw alloc site parents.
	 * @return Processed alloc site parents.
	 */
	private List<AllocSiteParent> getAllocSites(List<RawAllocSiteParent> rawAllocSiteParents) {
		List<AllocSiteParent> allocSiteParents = new ArrayList<>();

		for (RawAllocSiteParent rawAllocSiteParent : rawAllocSiteParents) {
			List<AllocSite> sites = new ArrayList<>();

			for (RawAllocSite rawSite : rawAllocSiteParent.getSites()) {
				sites.add(new AllocSite(
						rawSite.getArrayIndicator() == 1,
						rawSite.getClassSerialNum(),
						rawSite.getStackTraceSerialNum(),
						rawSite.getLiveBytes(),
						rawSite.getLiveInstances(),
						rawSite.getBytesAllocated(),
						rawSite.getInstancesAllocated()
				));
			}

			allocSiteParents.add(new AllocSiteParent(
					rawAllocSiteParent.getBitMaskFlags(),
					rawAllocSiteParent.getCutoffRatio(),
					rawAllocSiteParent.getLiveBytes(),
					rawAllocSiteParent.getLiveInstances(),
					rawAllocSiteParent.getBytesAllocated(),
					rawAllocSiteParent.getInstancesAllocated(),
					sites
			));
		}

		return allocSiteParents;
	}

	/**
	 * Translates raw stack traces into processed ones.
	 * @param rawStackTraces Raw stack traces.
	 * @param rawStackFrames Raw stack frames.
	 * @param stringMap Map of strings to translate String IDs into their values.
	 * @param classes Classes.
	 * @return Processed stack traces.
	 */
	private List<StackTrace> getStackTraces(List<RawStackTrace> rawStackTraces, List<RawStackFrame> rawStackFrames, Map<Long, String> stringMap, Map<Long, ClassDump> classes) {
		List<StackTrace> stackTraces = new ArrayList<>();
		Map<Long, StackFrame> idToRawStackFrame = rawStackFrames.stream().collect(Collectors.toMap(
				RawStackFrame::getStackFrameId,
				rawStackFrame -> new StackFrame(
						rawStackFrame.getStackFrameId(),
						stringMap.get(rawStackFrame.getMethodNameStringId()),
						stringMap.get(rawStackFrame.getMethodSigStringId()),
						stringMap.get(rawStackFrame.getSourceFileNameStringId()),
						classes.values().stream().filter(c -> c.getSerialNum() == rawStackFrame.getClassSerialNum()).findAny().orElse(null),
						rawStackFrame.getLocation()
				)
		));

		for (RawStackTrace rawStackTrace : rawStackTraces) {
			stackTraces.add(new StackTrace(
					rawStackTrace.getStackTraceSerialNum(),
					rawStackTrace.getThreadSerialNum(),
					rawStackTrace.getNumFrames(),
					Arrays.stream(rawStackTrace.getStackFrameIds()).mapToObj(idToRawStackFrame::get).collect(Collectors.toList())
			));
		}

		return stackTraces;
	}

	/**
	 * Translates raw instance arrays into processed ones.
	 * @param rawObjectArrayDumps Raw instance dumps.
	 * @param instanceDumpMap Instance map.
	 * @param classDumpMap Class map.
	 * @return Translated instance arrays.
	 */
	private Map<Long, InstanceArrayDump> getInstanceArrays(Map<Long, RawObjectArrayDump> rawObjectArrayDumps, Map<Long, InstanceDump> instanceDumpMap, Map<Long, ClassDump> classDumpMap) {
		Map<Long, InstanceArrayDump> arrays = new HashMap<>();

		rawObjectArrayDumps.forEach((key, value) -> {
			List<Object> instances = new ArrayList<>();

			value.getItems().forEach(item -> {
				instances.add(instanceDumpMap.get(item));
			});

			arrays.put(key, new InstanceArrayDump(key, classDumpMap.get(value.getItemClassObjectId()), instances));
		});

		return arrays;
	}

	/**
	 * Translates raw primitive arrays into processed ones.
	 * @param rawPrimitiveArrayDumps Raw primitive arrays.
	 * @return Processed primitive arrays.
	 */
	private Map<Long, ArrayDump> getPrimitiveArrays(Map<Long, RawPrimitiveArrayDump> rawPrimitiveArrayDumps) {
		Map<Long, ArrayDump> arrays = new HashMap<>();

		rawPrimitiveArrayDumps.forEach((key, value) -> {
			arrays.put(key, new ArrayDump<>(key, this.getClass(value.getItemClassObjectId()), value.getItems()));
		});

		return arrays;
	}

	/**
	 * Translates raw instances into processed ones.
	 * @param rawMemoryDump Raw memory dump to process.
	 * @param classes Classes.
	 * @return Processed instances.
	 */
	protected Map<Long, InstanceDump> getInstances(RawMemoryDump rawMemoryDump, Map<Long, ClassDump> classes) {
		Map<Long, InstanceDump> instances = new HashMap<>();

		rawMemoryDump.getRawInstanceDumps().forEach((key, value) -> {
			ClassDump classDump = classes.get(value.getObjectClassId());
			InstanceDump instanceDump = new InstanceDump(key, classDump);
			classDump.addInstance(instanceDump);
			instances.put(key, instanceDump);
		});

		this.processInstanceFields(instances, rawMemoryDump);

		return instances;
	}

	/**
	 * Translates instance fields into processed ones.
	 * @param instances Instances to process.
	 * @param rawMemoryDump Memory dump to use.
	 */
	private void processInstanceFields(Map<Long, InstanceDump> instances, RawMemoryDump rawMemoryDump) {
		Queue<Long> queue = new LinkedList<>(instances.keySet());

		while (!queue.isEmpty()) {
			final boolean[] skip = {false};

			Long key = queue.poll();
			InstanceDump value = instances.get(key);

			RawInstanceDump rawInstanceDump = rawMemoryDump.getRawInstanceDumps().get(key);
			Map<String, Object> instanceValues = rawInstanceDump.getInstanceValues();

			Map<InstanceFieldDump, Object> fieldsToAdd = new HashMap<>();

			instanceValues.forEach((fieldName, fieldValue) -> {
				Optional<InstanceFieldDump> any = value.getClassDump().getInstanceFields().stream().filter(field -> field.getName().equals(fieldName)).findAny();
				any.ifPresent(instanceFieldDump -> {
					if (instanceFieldDump.getType().equals(Object.class) && ((Value) fieldValue).value instanceof Long &&  instances.get(((Long) (((Value) fieldValue).value))) != null) {
						InstanceDump instanceDump = instances.get(((Long) (((Value) fieldValue).value)));
						if (this.isString(instanceDump)) {
							if (instanceDump.getInstanceFieldValues().isEmpty()) {
								queue.add(key);
								skip[0] = true;
								return;
							}

							fieldsToAdd.put(instanceFieldDump, this.extractString(instanceDump, rawMemoryDump.getRawPrimitiveArrayDumps()));
						} else {
							fieldsToAdd.put(instanceFieldDump, instanceDump);
						}
					} else {
						fieldsToAdd.put(instanceFieldDump, fieldValue);
					}
				});

				if (skip[0]) {
					return;
				}
			});

			if (skip[0]) {
				continue;
			}

			fieldsToAdd.forEach((instanceFieldDump, val) -> {
				value.addInstanceField(instanceFieldDump, val);
				instanceFieldDump.addValue(val);
			});
		}
	}

	/**
	 * Checks if an instance is String.
	 * @param instanceDump Instance to check
	 * @return True if the instance is {@link String}.
	 */
	private boolean isString(InstanceDump instanceDump) {
		return instanceDump.getClassDump().getName().equals(String.class.getName());
	}

	/**
	 * Extracts a string from arrays.
	 * @param instanceDump Instance to process.
	 * @param primitiveArrayDumpMap Primitive arrays.
	 * @return String from the array.
	 */
	private String extractString(InstanceDump instanceDump, Map<Long, RawPrimitiveArrayDump> primitiveArrayDumpMap) {
		final String[] ret = {null};

		instanceDump.getInstanceFieldValues().forEach((field, value) -> {
			if (Objects.equals(field.getName(), "value")) {
				RawPrimitiveArrayDump rawPrimitiveArrayDump = primitiveArrayDumpMap.get((Long) (((Value) value).value));
				if (rawPrimitiveArrayDump != null) {
					ret[0] = this.charArrayToString(rawPrimitiveArrayDump.getItems());
				}
			}
		});

		return ret[0];
	}

	/**
	 * Translates char array into string.
	 * @param list Values of the array.
	 * @return Created string.
	 */
	private String charArrayToString(List<Object> list) {
		StringBuilder sb = new StringBuilder();
		list.forEach(sb::append);
		return sb.toString();
	}

	/**
	 * Translates raw classes into processed ones.
	 * @param rawMemoryDump Memory dump to process.
	 * @return Translated processed classes.
	 */
	protected Map<Long, ClassDump> getClasses(RawMemoryDump rawMemoryDump) {
		Map<Long, ClassDump> classes = new HashMap<>();
		Queue<Long> unprocessedClasses = new LinkedList<>();

		new TreeMap<>(rawMemoryDump.getRawClassDumps()).forEach((key, value) -> {
			if (value.getSuperClassObjectId() != 0L && !classes.containsKey(value.getSuperClassObjectId())) {
				unprocessedClasses.add(key);
				return;
			}

			ClassDump classDump = this.processClass(rawMemoryDump, key, classes.get(value.getSuperClassObjectId()));
			classes.put(key, classDump);
		});

		int controlCount = unprocessedClasses.size();

		while (!unprocessedClasses.isEmpty()) {
			Long key = unprocessedClasses.poll();
			RawClassDump value = rawMemoryDump.getRawClassDumps().get(key);

			if (value.getSuperClassObjectId() != 0L
					&& (!classes.containsKey(value.getSuperClassObjectId())
					|| unprocessedClasses.contains(key))) {
				unprocessedClasses.add(key);
				controlCount--;

				if (controlCount == 0) {
					break;
				}

				continue;
			}

			classes.put(key, this.processClass(rawMemoryDump, key, classes.get(value.getSuperClassObjectId())));
			controlCount = unprocessedClasses.size();
		}

		this.processClassFields(classes, rawMemoryDump);

		return classes;
	}

	/**
	 * Process class fields - instance fields, static fields and constant fields.
	 * @param classes Classes to process.
	 * @param rawMemoryDump Raw memory dump to use.
	 */
	private void processClassFields(Map<Long, ClassDump> classes, RawMemoryDump rawMemoryDump) {
		classes.forEach((key, value) -> {
			rawMemoryDump.getRawClassDumps().get(key).getInstanceFields().forEach((name, strType) -> {
				value.addInstanceField(name, this.getClass(strType));
			});
		});

		classes.forEach((key, value) -> {
			rawMemoryDump.getRawClassDumps().get(key).getStaticFields().forEach((name, val) -> {
				value.addStaticField(name, this.getClass(((Value) val).type.toString()), ((Value) val).value);
			});
		});

		classes.forEach((key, value) -> {
			rawMemoryDump.getRawClassDumps().get(key).getStaticFields().forEach((name, val) -> {
				value.addStaticField(name, this.getClass(((Value) val).type.toString()), ((Value) val).value);
			});
		});
	}

	/**
	 * Translate a raw class into processed one.
	 * @param rawMemoryDump Raw memory class.
	 * @param id ID of the class.
	 * @param parent Class parent.
	 * @return Processed class.
	 */
	protected ClassDump processClass(RawMemoryDump rawMemoryDump, Long id, ClassDump parent) {
		RawLoadClassDump rawLoadClassDump = rawMemoryDump.getRawLoadClassDumps().get(id);
		ClassDump classDump = new ClassDump(id, Normalization.getNormalizedClassname(rawLoadClassDump.getClassName()), rawLoadClassDump.getClassSerialNum(), parent);

		if (parent != null) {
			parent.addChildClass(classDump);
		}

		return classDump;
	}

	/**
	 * Translate a raw header into processed one,
	 * @param rawDumpHeader Raw header.
	 * @return Processed header.
	 */
	protected DumpHeader getDumpHeader(RawDumpHeader rawDumpHeader) {
		return new DumpHeader(
				rawDumpHeader.getFormat(),
				rawDumpHeader.getIdSize(),
				Normalization.millisecondsToDate(rawDumpHeader.getTime())
		);
	}

	/**
	 * Translates a class name into an actual type.
	 * @param classType Class name (type).
	 * @return Translated type or null.
	 */
	protected Class<?> getClass(String classType) {
		return TYPE_TRANSLATION_MAP.get(classType.toLowerCase());
	}
}
