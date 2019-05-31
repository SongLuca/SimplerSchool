package main.controllers;

import java.util.ArrayList;
import java.util.Arrays;

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
		ArrayList<Integer> taskCounts = new ArrayList<Integer>();
		for(int i=0;i<12;i++) {
			taskCounts.add(0);
		}
		int Jan=0,Feb=0,Mar=0,Apr=0,May=0,Jun=0,Jul=0,Aug=0,Sep=0,Oct=0,Nov=0,Dec=0;
		attivita= DataBaseHandler.getInstance().getAttivita();
		for(SchoolTask task : attivita) {
			int month = task.getData().getMonth().getValue()-1;
			taskCounts.set(month, taskCounts.get(month)+1);
		}
		
	/*	for(SchoolTask task : attivita) {
			switch(task.getData().getMonth().getValue()){
				case 1:
					Jan++;
					break;
				case 2:
					Feb++;
					break;
				case 3:
					Mar++;
					break;
				case 4:
					Apr++;
					break;
				case 5:
					May++;
					break;
				case 6:
					Jun++;
					break;
				case 7:
					Jul++;
					break;
				case 8:
					Aug++;
					break;
				case 9:
					Sep++;
					break;
				case 10:
					Oct++;
					break;
				case 11:
					Nov++;
					break;
				case 12:
					Dec++;
					break;
			}
		}*/
	    lineChart.getXAxis().setLabel("Month");
	    lineChart.setTitle("iron man died");
	    XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
	    series.setName("attivita");
	    series.getData().add(new XYChart.Data<String,Number>("Jan", taskCounts.get(0)));
        series.getData().add(new XYChart.Data<String,Number>("Feb", taskCounts.get(1)));
        series.getData().add(new XYChart.Data<String,Number>("Mar", taskCounts.get(2)));
        series.getData().add(new XYChart.Data<String,Number>("Apr", taskCounts.get(3)));
        series.getData().add(new XYChart.Data<String,Number>("May", taskCounts.get(4)));
        series.getData().add(new XYChart.Data<String,Number>("Jun", taskCounts.get(5)));
        series.getData().add(new XYChart.Data<String,Number>("Jul", taskCounts.get(6)));
        series.getData().add(new XYChart.Data<String,Number>("Aug", taskCounts.get(7)));
        series.getData().add(new XYChart.Data<String,Number>("Sep", taskCounts.get(8)));
        series.getData().add(new XYChart.Data<String,Number>("Oct", taskCounts.get(9)));
        series.getData().add(new XYChart.Data<String,Number>("Nov", taskCounts.get(10)));
        series.getData().add(new XYChart.Data<String,Number>("Dec", taskCounts.get(11)));
        lineChart.getData().add(series);
	}
}
