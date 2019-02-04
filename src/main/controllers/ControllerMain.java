package main.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.application.Main;

public class ControllerMain {
	private final int HAMMENUSIZE = 230;
	@FXML
	private Pane menuPane;
	@FXML
	private Pane hiddenMenu;
	@FXML
	private JFXHamburger hamMenu;
	@FXML
	private HBox weekdayHeader;
	@FXML
	private GridPane calendarGrid;
	@FXML
    private ScrollPane scrollCalendarPane;
	@FXML
    private JFXButton settingsButton;
	@FXML
    private JFXButton profileButton;
	@FXML
    private ImageView image;
	@FXML
	private TextInputDialog inputSubject;
	@FXML
	public void hamclicked(MouseEvent event) {
		if (menuPane.getPrefWidth() == 300) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth()-HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0,0,0,0));
			settingsButton.setPrefSize(settingsButton.getPrefWidth()-HAMMENUSIZE, settingsButton.getPrefHeight());
			settingsButton.setPadding(new Insets(0,0,0,0));
			settingsButton.setText("");
			profileButton.setPrefSize(profileButton.getPrefWidth()-HAMMENUSIZE, profileButton.getPrefHeight());
			profileButton.setPadding(new Insets(0,0,0,0));
			profileButton.setText("");
			hamMenuAnimation(menuPane, menuPane.getPrefWidth() - HAMMENUSIZE, menuPane.getPrefHeight());
		}
		else if (menuPane.getPrefWidth() == 70) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth()+HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0,HAMMENUSIZE,0,0));
			settingsButton.setPrefSize(settingsButton.getPrefWidth()+HAMMENUSIZE, settingsButton.getPrefHeight());
			settingsButton.setPadding(new Insets(0,0,0,0));
			settingsButton.setText("Settings");
			settingsButton.setTextAlignment(TextAlignment.LEFT);
			profileButton.setPrefSize(profileButton.getPrefWidth()+HAMMENUSIZE, profileButton.getPrefHeight());
			profileButton.setPadding(new Insets(0,0,0,0));
			profileButton.setText("Profile");
			hamMenuAnimation(menuPane, menuPane.getPrefWidth() + HAMMENUSIZE, menuPane.getPrefHeight());
		}
	}

	public void initialize() {
		System.out.println("init menu gui");
		initHamMenu();
		initCalendarWeekDayHeader();
		initCalendarGrid();
	}
	
	public void initHamMenu() {
		HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamMenu);
		transition.setRate(-1);
		hamMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();
		});
	//	settingsButton.setStyle(Main.prop.getProperty("settingsIconPath"));
	//	settingsButton.setStyle("-fx-background-image: url('../../images/settings.png')");
	}
	
	public void resizePane(Pane p, double w, double h) {
		p.setPrefWidth(w);
		p.setPrefHeight(h);
	}

	public void hamMenuAnimation(final Pane pane, double width, double height) {
		hamMenu.setDisable(true);
		Duration cycleDuration = Duration.millis(200);
		Timeline timeline = new Timeline(
				new KeyFrame(cycleDuration, new KeyValue(pane.prefWidthProperty(), width, Interpolator.EASE_BOTH)),
				new KeyFrame(cycleDuration, new KeyValue(pane.prefHeightProperty(), height, Interpolator.EASE_BOTH)));
		timeline.play();
		timeline.setOnFinished(event -> {
			hamMenu.setDisable(false);
		});
		
	}

	public void initCalendarWeekDayHeader() {
		int weekdays = 6;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
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
		int cols = 6;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					inputSubject = new TextInputDialog();
					inputSubject.setContentText("Insert the subject");
					inputSubject.setTitle(null);
					inputSubject.setHeaderText(null);
					inputSubject.setGraphic(null);
					DialogPane dialogPane = inputSubject.getDialogPane();
					dialogPane.getStylesheets().add(
							getClass().getResource("../resources/gui/css/dialog.css").toExternalForm());
					dialogPane.getStyleClass().add("myDialog");


					inputSubject.show();
				});
				GridPane.setVgrow(vPane, Priority.ALWAYS);
				calendarGrid.add(vPane, j, i);

			}
		}
		for (int i = 0; i < cols; i++) {
			RowConstraints row = new RowConstraints();
			row.setMinHeight(scrollCalendarPane.getHeight() / cols);
			calendarGrid.getRowConstraints().add(row);
		}
	}

}
