package main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.Main;

public class SimplerSchoolUtil {

	public static Stage loadWindow(String fxmlProp, String iconProp, String title, Stage primaryStage) {
		Stage stage = null;
		try {
			URL fxmlURL = new File(Main.prop.getProperty(fxmlProp)).toURI().toURL();
			String iconURI = new File(Main.prop.getProperty(iconProp)).toURI().toString();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = (Parent) fxmlLoader.load();	
			stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initOwner(primaryStage);
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image(iconURI));
			HBox titleBox = (HBox)fxmlLoader.getNamespace().get("titleHBox");
			WindowStyle.allowDrag(titleBox, stage);
			new FXResizeHelper(stage,5,5);
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stage;
	}
	
	public static void initCalendarWeekDayHeader(HBox weekdayHeader) {
		int weekdays = 7;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat","Sun"};
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
	
	public static void initCalendarGrid(HBox weekdayHeader, GridPane calendarGrid) {
		int rows = 11;
		int cols = 7;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					System.out.println("grid");
				/*	inputSubject = new TextInputDialog();
					inputSubject.setContentText("Insert the subject");
					inputSubject.setTitle(null);
					inputSubject.setHeaderText(null);
					inputSubject.setGraphic(null);
					DialogPane dialogPane = inputSubject.getDialogPane();
					dialogPane.getStylesheets().add(
							getClass().getResource("../resources/gui/css/dialog.css").toExternalForm());
					dialogPane.getStyleClass().add("myDialog");
					inputSubject.show();*/
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
}
