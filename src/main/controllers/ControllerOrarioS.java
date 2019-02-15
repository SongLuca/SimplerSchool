package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.utils.SimplerSchoolUtil;

public class ControllerOrarioS {
	@FXML
	private HBox weekdayHeader;

	@FXML
	private GridPane calendarGrid;

	@FXML
	private AnchorPane subContentPane;

	
	public void initialize() {
		initOSCalendarWeekDayHeader(weekdayHeader);
		initOSCalendarGrid(weekdayHeader, calendarGrid);
	}

	public void initOSCalendarWeekDayHeader(HBox weekdayHeader) {
		int weekdays = 7;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		for (int i = 0; i < weekdays; i++) {
			StackPane pane = new StackPane();
			pane.getStyleClass().add("weekday-header");
			HBox.setHgrow(pane, Priority.ALWAYS);
			pane.setMaxWidth(Double.MAX_VALUE);
			pane.setMinWidth(weekdayHeader.getPrefWidth() / weekdays);
			weekdayHeader.getChildren().add(pane);
			pane.getChildren().add(new Label(weekDays[i]));
		}
	}

	public void initOSCalendarGrid(HBox weekdayHeader, GridPane calendarGrid) {
		int rows = 11;
		int cols = 7;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					System.out.println("grid");
					addMateria();
					/*
					 * inputSubject = new TextInputDialog();
					 * inputSubject.setContentText("Insert the subject");
					 * inputSubject.setTitle(null); inputSubject.setHeaderText(null);
					 * inputSubject.setGraphic(null); DialogPane dialogPane =
					 * inputSubject.getDialogPane(); dialogPane.getStylesheets().add(
					 * getClass().getResource("../resources/gui/css/dialog.css").toExternalForm());
					 * dialogPane.getStyleClass().add("myDialog"); inputSubject.show();
					 */
				});
				GridPane.setVgrow(vPane, Priority.ALWAYS);
				calendarGrid.add(vPane, j, i);
			}
		}
		for (int i = 0; i < cols; i++) {
			RowConstraints row = new RowConstraints();
			calendarGrid.getRowConstraints().add(row);
		}
	}

	public void addMateria() {
		SimplerSchoolUtil.loadNoTitleWindow("addSubjectFXML", null, false, null, null);
	}

	
}
