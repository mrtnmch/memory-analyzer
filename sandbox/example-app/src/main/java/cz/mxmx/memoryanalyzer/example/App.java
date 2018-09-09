package cz.mxmx.memoryanalyzer.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
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

		System.in.read();
	}

	public static void main(String[] args) throws IOException {
        new App();
    }
}
