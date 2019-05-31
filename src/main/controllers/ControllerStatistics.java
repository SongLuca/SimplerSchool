package main.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Effect;
import main.utils.Utils;

public class ControllerStatistics {
	@FXML
	private AnchorPane contentPane;
	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private PieChart pieChart;
	private ArrayList<SchoolTask> attivita;

	public void initialize() {
		AnchorPane.setBottomAnchor(contentPane, 0.0);
		AnchorPane.setTopAnchor(contentPane, 30.0);
		AnchorPane.setLeftAnchor(contentPane, 69.0);
		AnchorPane.setRightAnchor(contentPane, 0.0);
		getAllInfoFromDb();
	}

	public void getAllInfoFromDb() {
		JFXSpinner loading = MetaData.cm.getLoading();
		AnchorPane rootPane = MetaData.cm.getRootPane();
		StackPane rootStack = MetaData.cm.getRootStack();

		Task<Boolean> getAllInfoTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				rootPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().getAttivitaQuery();
			}
		};

		getAllInfoTask.setOnFailed(event -> {
			loading.setVisible(false);
			rootPane.setDisable(false);
			getAllInfoTask.getException().printStackTrace();
		});

		getAllInfoTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			rootPane.setEffect(null);
			if (getAllInfoTask.getValue()) {
				attivita = DataBaseHandler.getInstance().getAttivita();
				lineChart();
				pieChart();
			} else {
				Utils.popUpDialog(rootStack, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				rootPane.setDisable(false);
			}
		});

		new Thread(getAllInfoTask).start();

	}

	public void lineChart() {
		ArrayList<Integer> taskCounts = new ArrayList<Integer>();
		for (int i = 0; i < 12; i++) {
			taskCounts.add(0);
		}
		for (SchoolTask task : attivita) {
			int month = task.getData().getMonth().getValue() - 1;
			taskCounts.set(month, taskCounts.get(month) + 1);
		}
		lineChart.getXAxis().setLabel("Month");
		lineChart.getXAxis().setAnimated(false);
		lineChart.getYAxis().setAnimated(false);
		lineChart.setTitle("iron man died");
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("attivita");
		series.getData().add(new XYChart.Data<String, Number>("Jan", taskCounts.get(0)));
		series.getData().add(new XYChart.Data<String, Number>("Feb", taskCounts.get(1)));
		series.getData().add(new XYChart.Data<String, Number>("Mar", taskCounts.get(2)));
		series.getData().add(new XYChart.Data<String, Number>("Apr", taskCounts.get(3)));
		series.getData().add(new XYChart.Data<String, Number>("May", taskCounts.get(4)));
		series.getData().add(new XYChart.Data<String, Number>("Jun", taskCounts.get(5)));
		series.getData().add(new XYChart.Data<String, Number>("Jul", taskCounts.get(6)));
		series.getData().add(new XYChart.Data<String, Number>("Aug", taskCounts.get(7)));
		series.getData().add(new XYChart.Data<String, Number>("Sep", taskCounts.get(8)));
		series.getData().add(new XYChart.Data<String, Number>("Oct", taskCounts.get(9)));
		series.getData().add(new XYChart.Data<String, Number>("Nov", taskCounts.get(10)));
		series.getData().add(new XYChart.Data<String, Number>("Dec", taskCounts.get(11)));
		lineChart.getData().add(series);
	}

	public void pieChart() {
		int compitiCount = 0, verificheCount = 0, interrCount = 0;
		for (SchoolTask task : attivita) {
			switch (task.getTipo()) {
			case "Compiti per casa":
				compitiCount++;
				break;
			case "Verifica":
				verificheCount++;
				break;
			case "Interrogazione":
				interrCount++;
				break;
			}
		}
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Compiti per casa", compitiCount), new PieChart.Data("Verifica", verificheCount),
				new PieChart.Data("Interrogazione", interrCount));
		pieChart.setTitle("Tipo attivita");
		pieChart.setData(pieChartData);
		pieChart.setLabelLineLength(10);
		pieChart.setLegendSide(Side.LEFT);
		pieChart.setLabelsVisible(false);
	}
}
