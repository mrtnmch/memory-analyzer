package cz.mxmx.memoryanalyzer.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	private List<Parent> parents;
	private List<Integer> sameList;
	private List<Integer> emptyList;
	private List<Integer> mostlyEmptyList;
	private List<Integer> listOfDuplicates;
	private Map<Integer, Object> mostlyEmptyMap;
	private int childCounter = 0;
	private int parentCounter = 0;

	public App(int run) throws IOException {
		System.out.format("Running %s iterations...\n", run);
		this.createLists(run);
		this.parents = this.createObjectStructure(run);
		System.out.format("%d objects of each type generated, take the heap dump now and press any key to exit.", run);
		System.in.read();
	}

	private void createLists(int count) {
		this.sameList = new ArrayList<>(count * 1000);
		this.mostlyEmptyMap = new HashMap<>(count * 1000);
		this.mostlyEmptyList = new ArrayList<>(count * 1000);
		this.emptyList = new ArrayList<>(count * 1000);
		this.listOfDuplicates = new ArrayList<>(count * 1000);

		for (int i = 0; i < count; i++) {
			sameList.add(i);
		}

		for (int i = 0; i < count; i++) {
			mostlyEmptyList.add(i);
			mostlyEmptyMap.put(i, "String " + i);
		}

		for (int i = 0; i < count; i++) {
			this.listOfDuplicates.add(1);
		}
	}

	private List<Parent> createObjectStructure(int count) {
		List<Parent> parents = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			Parent parent = this.generateParent(this.parentCounter++);

			for (int k = 0; k < count; k++) {
				Child child = this.generateChild(i, parent);
			}

			parents.add(parent);
		}

		return parents;
	}

	private Child generateChild(int id, Parent parent) {
		Child child = new Child();
		child.name = "Child " + id;
		child.parent = parent;
		parent.children.add(child);
		return child;
	}

	private Parent generateParent(int id) {
		Parent parent = new Parent();
		parent.name = "Parent " + id;
		return parent;
	}

	public static void main(String[] args) throws IOException {
        new App(args.length == 1 ? Integer.parseInt(args[0]) : 1000);
    }
}
