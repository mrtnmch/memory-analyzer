package cz.mxmx.memoryanalyzer.process;

import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.DumpHeader;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.ProcessedMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawDumpHeader;
import cz.mxmx.memoryanalyzer.model.raw.RawInstanceDump;
import cz.mxmx.memoryanalyzer.model.raw.RawLoadClassDump;
import cz.mxmx.memoryanalyzer.model.raw.RawMemoryDump;
import cz.mxmx.memoryanalyzer.model.raw.RawPrimitiveArrayDump;
import cz.mxmx.memoryanalyzer.util.Normalization;
import edu.tufts.eaftan.hprofparser.parser.datastructures.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;

public class DefaultMemoryDumpProcessor implements MemoryDumpProcessor {

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

	private final Collection<String> namespaces;

	public DefaultMemoryDumpProcessor(Collection<String> namespaces) {
		this.namespaces = namespaces;
	}

	@Override
	public MemoryDump process(RawMemoryDump rawMemoryDump) {
		DumpHeader dumpHeader = this.getDumpHeader(rawMemoryDump.getRawDumpHeader());
		Map<Long, ClassDump> classes = this.getClasses(rawMemoryDump);
		Map<Long, InstanceDump> instances = this.getInstances(rawMemoryDump, classes);
		Map<Long, ClassDump> userClasses = this.getUserClasses(classes);
		Map<Long, InstanceDump> userInstances = this.getUserInstances(instances, userClasses);

		return new ProcessedMemoryDump(
				this.namespaces,
				dumpHeader,
				instances,
				classes,
				userInstances,
				userClasses
		);
	}

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
					if (instanceFieldDump.getType().equals(Object.class) && instances.get(((Long) (((Value) fieldValue).value))) != null) {
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

	private boolean isString(InstanceDump instanceDump) {
		return instanceDump.getClassDump().getName().equals(String.class.getName());
	}

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

	private String charArrayToString(List<Object> list) {
		StringBuilder sb = new StringBuilder();
		list.forEach(sb::append);
		return sb.toString();
	}

	protected Map<Long, InstanceDump> getUserInstances(Map<Long, InstanceDump> instances, Map<Long, ClassDump> userClasses) {
		Map<Long, InstanceDump> userInstances = new HashMap<>();

		instances.forEach((key, value) -> {
			if (userClasses.containsKey(value.getClassDump().getClassId())) {
				userInstances.put(key, value);
			}
		});

		return userInstances;
	}

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
					&& !classes.containsKey(value.getSuperClassObjectId())
					&& unprocessedClasses.contains(key)) {
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

	protected Map<Long, ClassDump> getUserClasses(Map<Long, ClassDump> classes) {
		Map<Long, ClassDump> userClasses = new HashMap<>();

		classes.forEach((key, value) -> {
			this.namespaces.forEach((namespace) -> {
				if (value.getName().matches(namespace)) {
					userClasses.put(key, value);
				}
			});
		});

		return userClasses;
	}

	protected ClassDump processClass(RawMemoryDump rawMemoryDump, Long id, ClassDump parent) {
		RawLoadClassDump rawLoadClassDump = rawMemoryDump.getRawLoadClassDumps().get(id);
		ClassDump classDump = new ClassDump(id, Normalization.getNormalizedClassname(rawLoadClassDump.getClassName()), parent);

		if (parent != null) {
			parent.addChildClass(classDump);
		}

		return classDump;
	}

	protected DumpHeader getDumpHeader(RawDumpHeader rawDumpHeader) {
		return new DumpHeader(
				rawDumpHeader.getFormat(),
				rawDumpHeader.getIdSize(),
				Normalization.millisecondsToDate(rawDumpHeader.getTime())
		);
	}

	protected Class<?> getClass(String classType) {
		return TYPE_TRANSLATION_MAP.get(classType.toLowerCase());
	}
}
