package cz.mxmx.memoryanalyzer.app;


import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.DefaultMemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.memorywaste.DefaultWasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {

	@FXML
	private Pane pane;

	@FXML
	private TextField dumpPath;

	@FXML
	private Button selectDumpPath;

	@FXML
	private Button getNamespaces;

	public MainViewController() throws FileNotFoundException, MemoryDumpAnalysisException {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.dumpPath.setText("sandbox/data/heapdump-intellij-idea.hprof");
		this.selectDumpPath.setOnAction(click -> {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Open memory dump");
			File file = chooser.showOpenDialog(selectDumpPath.getScene().getWindow());
			if (file != null) {
				dumpPath.setText(file.getPath());
			}
		});

		this.getNamespaces.setOnAction(click -> {
			MemoryDumpAnalyzer memoryDumpAnalyzer = new DefaultMemoryDumpAnalyzer(this.dumpPath.getText());
			this.showDialog(
					"Loading namespaces",
					this.dumpPath.getScene().getWindow(),
					memoryDumpAnalyzer::getNamespaces,
					(namespaces) -> {
						this.handleNamespaces(memoryDumpAnalyzer, namespaces);
						return null;
					}
			);
		});
	}

	private void handleNamespaces(MemoryDumpAnalyzer analyzer, Set<String> namespaces) {
		Platform.runLater(() -> {
			Node xxx = this.pane.getChildren().stream().filter(node -> node.getId().equals("xxx")).findAny().orElse(null);
			if(xxx != null) {
				this.pane.getChildren().remove(xxx);
			}
			ListView<String> e = new ListView<>(FXCollections.observableList(new ArrayList<>(namespaces).stream().sorted().collect(Collectors.toList())));
			e.setId("xxx");
			e.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				System.out.println(newValue);
				MemoryDump parsedDump = null;
				try {
					parsedDump = analyzer.analyze(Lists.newArrayList(newValue));
				} catch (FileNotFoundException | MemoryDumpAnalysisException e1) {
					e1.printStackTrace();
				}
				System.out.println("Done, found:");
				System.out.println("\tClasses: " + parsedDump.getClasses().size());
				System.out.println("\tInstances: " + parsedDump.getInstances().size());
				System.out.println("Namespace " + newValue);
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
			});
			this.pane.getChildren().add(e);
		});
	}

	private <T> void showDialog(String title, Window owner, Callable<T> task, Function<T, Void> callback) {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(owner);
		Label loader = new Label(title);
		loader.setContentDisplay(ContentDisplay.BOTTOM);
		loader.setGraphic(new ProgressIndicator());
		dialog.getDialogPane().setGraphic(loader);

		ExecutorService executor = Executors.newFixedThreadPool(4);
		Future<T> future = executor.submit(task);
		executor.submit(() -> {
			System.out.println("Waiting");
			System.out.flush();
			callback.apply(future.get());
			Platform.runLater(() -> {
				dialog.setResult(Boolean.TRUE);
				dialog.close();
				executor.shutdown();
			});
			return null;
		});

		dialog.show();
	}
}
