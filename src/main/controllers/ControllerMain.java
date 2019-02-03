package main.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControllerMain {

	@FXML
	private Pane menuPane;

	@FXML
	private JFXHamburger hamMenu;

	@FXML
	private HBox weekdayHeader;

	@FXML
	private GridPane calendarGrid;
	
	@FXML
    private ScrollPane scrollCalendarPane;
	
	@FXML
	public void hamclicked(MouseEvent event) {
		if (menuPane.getPrefWidth() == 300)
			hamMenuAnimation(menuPane, menuPane.getPrefWidth() - 230, menuPane.getPrefHeight());
		else if (menuPane.getPrefWidth() == 70)
			hamMenuAnimation(menuPane, menuPane.getPrefWidth() + 230, menuPane.getPrefHeight());
	}

	public void initialize() {
		HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamMenu);
		transition.setRate(-1);
		hamMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();
		});
		initCalendarWeekDayHeader();
		initCalendarGrid();
	}

	public void resizePane(Pane p, double w, double h) {
		p.setPrefWidth(w);
		p.setPrefHeight(h);
	}

	public void hamMenuAnimation(final Pane pane, double width, double height) {
		hamMenu.setDisable(true);
		Duration cycleDuration = Duration.millis(330);
		Timeline timeline = new Timeline(
				new KeyFrame(cycleDuration, new KeyValue(pane.prefWidthProperty(), width, Interpolator.EASE_BOTH)),
				new KeyFrame(cycleDuration, new KeyValue(pane.prefHeightProperty(), height, Interpolator.EASE_BOTH)));
		timeline.play();
		timeline.setOnFinished(event -> {
			hamMenu.setDisable(false);
		});
	}

	public void initCalendarWeekDayHeader() {
		int weekdays = 7;
		String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
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

	public void initCalendarGrid() {
		int rows = 11;
		int cols = 7;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / 7);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					System.out.println("click");
				});
				GridPane.setVgrow(vPane, Priority.ALWAYS);
				calendarGrid.add(vPane, j, i);
			}
		}
		for (int i = 0; i < 7; i++) {
			RowConstraints row = new RowConstraints();
			row.setMinHeight(scrollCalendarPane.getHeight() / 7);
			calendarGrid.getRowConstraints().add(row);
		}
	}
}
