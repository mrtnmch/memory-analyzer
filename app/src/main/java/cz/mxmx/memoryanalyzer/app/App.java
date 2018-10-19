package cz.mxmx.memoryanalyzer.app;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.DefaultMemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.memorywaste.DefaultWasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

public class App /*extends Application*/ {
	public static final List<String> NAMESPACES = Lists.newArrayList("cz.mxmx");
	public static final String PATH = "sandbox/data/heapdump-1536661038659.hprof";


	public static void main(String[] args) throws IOException, MemoryDumpAnalysisException {
		//launch(args);

		System.out.println("Processing " + PATH + "...");
		MemoryDumpAnalyzer memoryDumpAnalyzer = new DefaultMemoryDumpAnalyzer(NAMESPACES);
		MemoryDump parsedDump = memoryDumpAnalyzer.analyze(PATH);
		System.out.println("Done, found:");
		System.out.println("\tClasses: " + parsedDump.getClasses().size());
		System.out.println("\tInstances: " + parsedDump.getInstances().size());
		System.out.println("Namespace " + NAMESPACES.get(0));
		System.out.println("\tClasses: " + parsedDump.getUserClasses().size());
		System.out.println("\tInstances: " + parsedDump.getUserInstances().size());

		System.out.println();
		System.out.println("Analyzing memory waste...");
		WasteAnalyzer wasteAnalyzer = new DefaultWasteAnalyzerPipeline();
		List<Waste> memoryWaste = wasteAnalyzer.findMemoryWaste(parsedDump);
		System.out.println("Done, found " + memoryWaste.size() + " possible ways to save memory:");

		for (Waste waste : memoryWaste) {
			System.out.println("\t" + waste.getTitle() + ": " + waste.getDescription());
		}


	}

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        String resourcePath = "/fxml/MainView.fxml";
        URL location = getClass().getResource(resourcePath);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
}
