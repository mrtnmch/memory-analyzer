package cz.mxmx.memoryanalyzer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClassDump {
	private final Long classId;
	private final String name;
	private final int serialNum;
	private final ClassDump superClassDump;
	private final List<InstanceDump> instances = new ArrayList<>();
	private final List<FieldDump> constants = new ArrayList<>();
	private final List<FieldDump> staticFields = new ArrayList<>();
	private final List<InstanceFieldDump> instanceFields = new ArrayList<>();
	private final List<ClassDump> childrenClasses = new ArrayList<>();

	public ClassDump(Long classId, String name, int serialNum, ClassDump superClassDump) {
		this.classId = classId;
		this.name = name;
		this.serialNum = serialNum;
		this.superClassDump = superClassDump;
	}

	public void addInstance(InstanceDump objectDump) {
		this.instances.add(objectDump);
	}

	public void addChildClass(ClassDump classDump) {
		this.childrenClasses.add(classDump);
	}

	public void addInstanceField(String name, Class<?> type) {
		this.instanceFields.add(new InstanceFieldDump(name, type));
	}

	public void addStaticField(String name, Class<?> type, Object value) {
		this.staticFields.add(new FieldDump(name, type, value));
	}

	public Long getClassId() {
		return classId;
	}

	public String getName() {
		return name;
	}

	public ClassDump getSuperClassDump() {
		return superClassDump;
	}

	public List<InstanceDump> getInstances() {
		return instances;
	}

	public List<FieldDump> getConstants() {
		return constants;
	}

	public List<FieldDump> getStaticFields() {
		return staticFields;
	}

	public List<InstanceFieldDump> getInstanceFields() {
		return instanceFields;
	}

	public List<ClassDump> getChildrenClassDumps() {
		return childrenClasses;
	}

	public int getSerialNum() {
		return serialNum;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("classId", classId)
				.append("name", name)
				.append("serialNum", serialNum)
				.append("superClassDump", superClassDump)
				.append("instances", instances.size())
				.append("childrenClasses", childrenClasses.size())
				.append("constants", constants)
				.append("staticFields", staticFields)
				.append("instanceFields", instanceFields)
				.toString();
	}
}
