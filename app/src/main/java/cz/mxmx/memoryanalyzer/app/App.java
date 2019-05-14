package cz.mxmx.memoryanalyzer.app;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Strings;
import cz.mxmx.memoryanalyzer.DefaultMemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.memorywaste.DefaultWasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.model.InstanceDump;
import cz.mxmx.memoryanalyzer.model.InstanceFieldDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javax.xml.transform.Result;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class App {

	public static void main(String[] args) throws IOException, MemoryDumpAnalysisException {
		new App(args);
	}

	public App(String[] args) {
		Options options = new Options();

		Option listOptions = new Option("l", "list", false, "list namespaces and exit");
		listOptions.setRequired(false);
		options.addOption(listOptions);

		Option printFieldsOptions = new Option("f", "fields", false, "print the fields' values of the affected instances");
		printFieldsOptions.setRequired(false);
		options.addOption(printFieldsOptions);

		Option pathOption = new Option("p", "path", true, "input dump file path");
		pathOption.setRequired(true);
		options.addOption(pathOption);

		Option namespacesOption = new Option("n", "namespace", true, "namespace to analyze");
		namespacesOption.setRequired(false);
		options.addOption(namespacesOption);

		Option helpOption = new Option("h", "help", false, "print help");
		helpOption.setRequired(false);
		options.addOption(helpOption);

		Option csvOption = new Option("c", "csv", false, "write the result also to an csv file");
		csvOption.setRequired(false);
		options.addOption(csvOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			String inputFilePath = cmd.getOptionValue("path");
			String namespace = cmd.getOptionValue("namespace");
			boolean list = cmd.hasOption("list");
			boolean help = cmd.hasOption("help");
			boolean fields = cmd.hasOption("fields");
			boolean csv = cmd.hasOption("csv");

			if (list && !Strings.isNullOrEmpty(inputFilePath)) {
				Runnable measure = this.measure();

				DefaultMemoryDumpAnalyzer analyzer = new DefaultMemoryDumpAnalyzer(inputFilePath);
				System.out.format("Loading namespaces from %s...\n\n", inputFilePath);
				Set<String> namespaces = new TreeSet<>(this.getNamespaces(analyzer));

				System.out.println("List of namespaces in the given memory dump:");
				namespaces.forEach(ns -> {
					System.out.format("\t%s\n", ns);
				});

				measure.run();
			} else if (!Strings.isNullOrEmpty(namespace) && !Strings.isNullOrEmpty(inputFilePath)) {
				List<ResultWriter> resultWriters = new ArrayList<>();
				resultWriters.add(new ConsoleResultWriter(namespace));

				if(csv) {
					resultWriters.add(new CsvResultWriter("result.csv", namespace));
				}

				Runnable measure = this.measure();

				DefaultMemoryDumpAnalyzer analyzer = new DefaultMemoryDumpAnalyzer(inputFilePath);
				System.out.format("Analyzing classes from namespace `%s` in `%s`...\n\n", namespace, inputFilePath);
				MemoryDump memoryDump = this.getMemoryDump(analyzer, namespace);
				this.processMemoryDump(memoryDump, namespace, fields, resultWriters);

				measure.run();

				resultWriters.forEach(ResultWriter::close);
			} else if (help) {
				formatter.printHelp("memory-analyzer", options);
			} else {
				System.out.println("No action defined. See --help for more info.");
			}

			System.exit(0);

		} catch (ParseException | MemoryDumpAnalysisException e) {
			System.out.println("Error: " + e.getMessage());
			formatter.printHelp("memory-analyzer", options);
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Couldn't find the specified input file.");
		} catch (Exception e) { // print out anything else
			e.printStackTrace();
		}
	}

	private Set<String> getNamespaces(MemoryDumpAnalyzer analyzer) throws FileNotFoundException, MemoryDumpAnalysisException {
		return analyzer.getNamespaces();
	}

	private MemoryDump getMemoryDump(MemoryDumpAnalyzer analyzer, String namespace) throws FileNotFoundException, MemoryDumpAnalysisException {
		return analyzer.analyze(Lists.newArrayList(namespace));
	}

	private void processMemoryDump(MemoryDump memoryDump, String namespace, boolean printFields, List<ResultWriter> resultWriters) {
		resultWriters.forEach(writer -> writer.write(memoryDump));
		WasteAnalyzerPipeline wasteAnalyzer = new DefaultWasteAnalyzerPipeline();
		List<Waste> memoryWaste = wasteAnalyzer.findMemoryWaste(memoryDump);
		resultWriters.forEach(writer -> writer.write(memoryWaste, wasteAnalyzer, printFields));
	}

	private Runnable measure() {
		Instant now = Instant.now();

		return () -> {
			Duration duration = Duration.between(now, Instant.now());
			System.out.format("Duration: %s\n", duration);
		};
	}

}
