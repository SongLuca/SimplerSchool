package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.application.models.Materia;
import main.application.models.MateriaStatistico;
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
	private PieChart pieChart, pieChart1;
	@FXML
	private BarChart<String, Number> barChart;

	private ArrayList<SchoolTask> attivita;
	private ArrayList<Materia> materie;

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
				materie = DataBaseHandler.getInstance().getMaterie();
				lineChart();
				pieChart();
				pieChart1();
				barChart();
			} else {
				Utils.popUpDialog(rootStack, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				rootPane.setDisable(false);
			}
		});

		new Thread(getAllInfoTask).start();

	}

	public void barChart() {
		HashMap<String, MateriaStatistico> counts = new HashMap<String, MateriaStatistico>();
		for (SchoolTask task : attivita) {
			if (!task.getTipo().equalsIgnoreCase("allegato file")) {
				String nomeM = getMateriaNomeById(task.getIdMateria());
				if (!counts.containsKey(nomeM)) {
					counts.put(nomeM, new MateriaStatistico(nomeM));
				}
				if (task.getTipo().equalsIgnoreCase("verifica")) {
					counts.get(nomeM).incrementV();
				} else if (task.getTipo().equalsIgnoreCase("interrogazione")) {
					counts.get(nomeM).incrementI();
				}
			}
		}
		for (String key : counts.keySet()) {
			System.out.println(counts.get(key).toString());
		}
		barChart.setTitle("Interrogazioni e verifiche");
		barChart.getXAxis().setLabel("Materie");
		barChart.getYAxis().setLabel("Quantita");
		barChart.getXAxis().setAnimated(false);
		barChart.getYAxis().setAnimated(false);
		XYChart.Series<String, Number> verifiche = new XYChart.Series<String, Number>();
		verifiche.setName("Verifiche");
		XYChart.Series<String, Number> interrogazioni = new XYChart.Series<String, Number>();
		interrogazioni.setName("Interrogazioni");

		for (String key : counts.keySet()) {
			if (!counts.get(key).isEmpty()) {
				if (counts.get(key).getVerificheCount() > 0) {
					verifiche.getData().add(new XYChart.Data<String, Number>(key, counts.get(key).getVerificheCount()));
				}
				if (counts.get(key).getInterrCount() > 0) {
					interrogazioni.getData()
							.add(new XYChart.Data<String, Number>(key, counts.get(key).getInterrCount()));
				}
			} else {
				verifiche.getData().add(new XYChart.Data<String, Number>(key, counts.get(key).getVerificheCount()));
				interrogazioni.getData().add(new XYChart.Data<String, Number>(key, counts.get(key).getInterrCount()));
			}

		}
		barChart.getData().add(verifiche);
		barChart.getData().add(interrogazioni);
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
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		if(compitiCount != 0)
			pieChartData.add(new PieChart.Data("Compiti per casa", compitiCount));
		if(verificheCount !=0)
			pieChartData.add(new PieChart.Data("Verifica", verificheCount));
		if(interrCount !=0)
			pieChartData.add(new PieChart.Data("Interrogazione", interrCount));
			
		pieChart.setTitle("Tipo attivita");
		pieChart.setData(pieChartData);
		pieChart.setLabelLineLength(10);
		pieChart.getData().stream().forEach(data -> {
			Tooltip tooltip = new Tooltip();
			tooltip.setText(String.format("%.0f", data.getPieValue()));
			data.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent me) {
					if (me.getSource() instanceof Node) {
						Node sender = (Node) me.getSource();
						Tooltip.install(sender, tooltip);
						sender.setEffect(new Glow(0.5));
					}
				}
			});
			data.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent me) {
					if (me.getSource() instanceof Node) {
						Node sender = (Node) me.getSource();
						sender.setEffect(null);
						tooltip.hide();
					}
				}
			});
		});
		// pieChart.setLegendSide(Side.LEFT);
		// pieChart.setLabelsVisible(false);
	}

	public void pieChart1() {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		for (SchoolTask task : attivita) {
			if (!task.getTipo().equalsIgnoreCase("allegato file")) {
				String nomeM = getMateriaNomeById(task.getIdMateria());
				if (!counts.containsKey(nomeM)) {
					counts.put(nomeM, 1);
				} else {
					counts.put(nomeM, counts.get(nomeM) + 1);
				}
			}
		}
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (String key : counts.keySet()) {
			pieChartData.add(new PieChart.Data(key, counts.get(key)));
		}

		pieChart1.setTitle("Attivita per materia");
		pieChart1.setData(pieChartData);
		pieChart1.setLabelLineLength(10);
		/*
		 * pieChart1.getData().stream().forEach(data -> { Tooltip tooltip = new
		 * Tooltip(); tooltip.setStyle("-fx-show-delay: 10ms;");
		 * tooltip.setText(String.format("%.0f", data.getPieValue()));
		 * Tooltip.install(data.getNode(), tooltip);
		 * data.pieValueProperty().addListener((observable, oldValue, newValue) ->
		 * tooltip.setText(String.format("%.0f", newValue))); });
		 */
		pieChart1.getData().stream().forEach(data -> {
			Tooltip tooltip = new Tooltip();
			tooltip.setText(String.format("%.0f", data.getPieValue()));
			data.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent me) {
					if (me.getSource() instanceof Node) {
						Node sender = (Node) me.getSource();
						Tooltip.install(sender, tooltip);
						sender.setEffect(new Glow(0.5));
					}
				}
			});
			data.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent me) {
					if (me.getSource() instanceof Node) {
						Node sender = (Node) me.getSource();
						sender.setEffect(null);
						tooltip.hide();
					}
				}
			});
		});

	// pieChart1.setLegendSide(Side.LEFT);
	// pieChart1.setLabelsVisible(false);
	}

	public String getMateriaNomeById(int id) {
		for (Materia m : materie) {
			if (m.getId() == id)
				return m.getNome();
		}
		return "";
	}
}
