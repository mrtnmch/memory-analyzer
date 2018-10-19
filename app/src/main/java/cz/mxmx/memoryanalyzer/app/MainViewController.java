package cz.mxmx.memoryanalyzer.app;


import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.DefaultMemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.MemoryDumpAnalyzer;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.memorywaste.DefaultWasteAnalyzerPipeline;
import cz.mxmx.memoryanalyzer.memorywaste.WasteAnalyzer;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import cz.mxmx.memoryanalyzer.model.memorywaste.Waste;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

	@FXML
	private TableView<Waste> tableView;

	public MainViewController() throws FileNotFoundException, MemoryDumpAnalysisException {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MemoryDumpAnalyzer memoryDumpAnalyzer = new DefaultMemoryDumpAnalyzer(App.NAMESPACES);
		MemoryDump parsedDump = null;
		try {
			parsedDump = memoryDumpAnalyzer.analyze("sandbox/data/heapdump-1536661038659.hprof");
		} catch (FileNotFoundException | MemoryDumpAnalysisException e) {
			e.printStackTrace();
		}
		WasteAnalyzer wasteAnalyzer = new DefaultWasteAnalyzerPipeline();
		List<Waste> memoryWaste = wasteAnalyzer.findMemoryWaste(parsedDump);

		this.tableView.setItems(FXCollections.observableArrayList(memoryWaste));

		TableColumn<Waste, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		this.tableView.getColumns().add(titleColumn);
	}
}
