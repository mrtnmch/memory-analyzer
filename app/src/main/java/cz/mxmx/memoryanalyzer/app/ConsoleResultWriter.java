package cz.mxmx.memoryanalyzer.app;

import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Result writer to the console.
 */
public class ConsoleResultWriter implements ResultWriter {

	/**
	 * Processed namespace.
	 */
	private final String namespace;

	public ConsoleResultWriter(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public void write(MemoryDump memoryDump) {
		System.out.println("Done, found:");
		System.out.println("\tClasses: " + memoryDump.getClasses().size());
		System.out.println("\tInstances: " + memoryDump.getInstances().size());
		System.out.println("Namespace " + this.namespace);
		System.out.println("\tClasses: " + memoryDump.getUserClasses().size());
		System.out.println("\tInstances: " + memoryDump.getUserInstances().size());

		System.out.println();
		System.out.println("Analyzing memory waste...");
	}

	@Override
	public void write(List<Waste> wasteList, WasteAnalyzerPipeline wasteAnalyzer, boolean printFields) {
		System.out.println("Done, found " + wasteList.size() + " possible ways to save memory:");
		Map<WasteAnalyzer, List<Waste>> classes = wasteList
				.stream()
				.collect(Collectors.groupingBy(Waste::getSourceWasteAnalyzer));

		classes
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.comparingInt(value -> -value.size())))
				.forEach(kv -> {
					WasteAnalyzer type = kv.getKey();
					List<Waste> list = kv.getValue();

					System.out.format("\t%s (%d):\n", wasteAnalyzer.getWasteTitle(type), list.size());
					list
							.stream()
							.sorted()
							.map(waste -> String.format(
									"\t\t%s: %s%s",
									waste.getTitle(),
									waste.getDescription(),
									printFields ? this.dumpInstanceFields(waste) : ""
							))
							.forEach(System.out::println);
					System.out.println();
				});
	}

	/**
	 * Creates a string with instance fields values.
	 * @param waste Memory waste
	 * @return String with fields.
	 */
	private String dumpInstanceFields(Waste waste) {
		StringBuilder sb = new StringBuilder();
		Optional<InstanceDump> first = waste.getAffectedInstances().stream().findFirst();

		sb.append("\n");

		if (first.isPresent()) {
			for (InstanceFieldDump instanceField : first.get().getClassDump().getInstanceFields()) {
				Object value = first.get().getInstanceFieldValues().get(instanceField);

				sb.append(String.format("\t\t\t%s = `%s`\n", instanceField.getName(), value));
			}
		}

		return sb.toString();
	}

	@Override
	public void close() {
		// empty
	}
}
