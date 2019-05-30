package main.controllers;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;

public class ControllerStatistics {
	@FXML
	private AnchorPane contentPane;
	@FXML
    private LineChart<String,Number> lineChart;
	
	private ArrayList<SchoolTask> attivita;
	
	public void initialize() {
		AnchorPane.setBottomAnchor(contentPane, 0.0);
		AnchorPane.setTopAnchor(contentPane, 30.0);
		AnchorPane.setLeftAnchor(contentPane, 69.0);
		AnchorPane.setRightAnchor(contentPane, 0.0);
		lineChart();
	}
	
	public void lineChart() {
		int Jan=0,Feb=0,Mar=0,Apr=0,May=0,Jun=0,Jul=0,Aug=0,Sep=0,Oct=0,Nov=0,Dec=0;
		attivita= DataBaseHandler.getInstance().getAttivita();
		for(SchoolTask task : attivita) {
			switch(task.getData().getMonth().getValue()){
				case 1:
					Jan++;
				case 2:
					Feb++;
				case 3:
					Mar++;
				case 4:
					Apr++;
				case 5:
					May++;
				case 6:
					Jun++;
				case 7:
					Jul++;
				case 8:
					Aug++;
				case 9:
					Sep++;
				case 10:
					Oct++;
				case 11:
					Nov++;
				case 12:
					Dec++;
			}
		}
	    lineChart.getXAxis().setLabel("Month");
	    lineChart.setTitle("iron man died");
	    XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
	    series.setName("attivita");
	    series.getData().add(new XYChart.Data<String,Number>("Jan", 23));
        series.getData().add(new XYChart.Data<String,Number>("Feb", 14));
        series.getData().add(new XYChart.Data<String,Number>("Mar", 15));
        series.getData().add(new XYChart.Data<String,Number>("Apr", 24));
        series.getData().add(new XYChart.Data<String,Number>("May", 34));
        series.getData().add(new XYChart.Data<String,Number>("Jun", 36));
        lineChart.getData().add(series);
	}
}
