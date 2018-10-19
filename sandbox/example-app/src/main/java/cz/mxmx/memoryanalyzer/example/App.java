package cz.mxmx.memoryanalyzer.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	private List<Integer> sameList = new ArrayList<>();
	private List<Integer> emptyList = new ArrayList<>(1000);
	private List<Integer> mostlyEmptyList = new ArrayList<>(1000);
	private Map<Integer, Object> mostlyEmptyMap = new HashMap<>(1000);

	public App() throws IOException {
		Parent parent = new Parent();
		parent.name = "Parent 1";

		Child child = new Child();
		child.name = "Child 1";
		child.parent = parent;

		Child child2 = new Child();
		child2.name = "Child 2";
		child2.parent = parent;

		Child child3 = new Child();
		child3.name = "Child 2";
		child3.parent = parent;

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < 1000; i++) {
			sameList.add(i);
		}

		for (int i = 0; i < 100; i++) {
			mostlyEmptyList.add(i);
			mostlyEmptyMap.put(i, "String " + i);
		}

		System.in.read();
	}

	public static void main(String[] args) throws IOException {
        new App();
    }
}
