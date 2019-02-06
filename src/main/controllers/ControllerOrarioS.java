package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.utils.SimplerSchoolUtil;

public class ControllerOrarioS {
	
	@FXML
	private HBox weekdayHeader;
	
	@FXML
	private GridPane calendarGrid;
	
	
	@FXML
	private AnchorPane subContentPane;
	
	public void initialize() {
		SimplerSchoolUtil.initCalendarWeekDayHeader(weekdayHeader);
		SimplerSchoolUtil.initCalendarGrid(weekdayHeader,calendarGrid);
	}

}
