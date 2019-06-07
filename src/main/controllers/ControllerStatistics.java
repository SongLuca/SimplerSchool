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
	private PieChart pieChart, pieChart1;
	@FXML
	private BarChart<String, Number> barChart, barChart1;

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
		HashMap<String, ArrayList<Integer>> mediaVoti = new HashMap<String, ArrayList<Integer>>();
		for (SchoolTask task : attivita) {
			if (!mediaVoti.containsKey(task.getMateriaNome()))
				mediaVoti.put(task.getMateriaNome(), new ArrayList<Integer>());
			if (task.getTipo().equals("Verifica") || task.getTipo().equals("Interrogazione")) {
				if (task.getVoto() > -1) {
					mediaVoti.get(task.getMateriaNome()).add(task.getVoto());
				}
			}
		}

		barChart1.setTitle("Media Voti");
		barChart1.getXAxis().setLabel("Materie");
		barChart1.getYAxis().setLabel("Media");
		barChart1.getXAxis().setAnimated(false);
		barChart1.getYAxis().setAnimated(false);
		XYChart.Series<String, Number> media = new XYChart.Series<String, Number>();
		for (String key : mediaVoti.keySet()) {
			media.getData().add(new XYChart.Data<String, Number>(key, calcolaMedia(mediaVoti.get(key))));
		}
		barChart1.getData().add(media);
		for (XYChart.Series<String, Number> s : barChart1.getData()) {
			for (XYChart.Data<String, Number> d : s.getData()) {
				Tooltip tooltip = new Tooltip();
				tooltip.setText(d.getYValue() + "");
				// Adding class on hover
				d.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent me) {
						if (me.getSource() instanceof Node) {
							Node sender = (Node) me.getSource();
							Tooltip.install(sender, tooltip);
							sender.setEffect(new Glow(0.5));
						}
					}
				});
				d.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent me) {
						if (me.getSource() instanceof Node) {
							Node sender = (Node) me.getSource();
							sender.setEffect(null);
							tooltip.hide();
						}
					}
				});
			}
		}
	}

	public double calcolaMedia(ArrayList<Integer> media) {
		double somma = 0;
		for (int i = 0; i < media.size(); i++)
			somma += media.get(i);
		return somma / media.size();
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
		if (compitiCount != 0)
			pieChartData.add(new PieChart.Data("Compiti per casa", compitiCount));
		if (verificheCount != 0)
			pieChartData.add(new PieChart.Data("Verifica", verificheCount));
		if (interrCount != 0)
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
