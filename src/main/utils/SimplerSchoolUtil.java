package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.Main;

public class SimplerSchoolUtil {
	
	public static Properties readProperties(String propFileName) {
		Properties prop = new Properties();
		FileInputStream fit = null ;
		try{
			fit = new FileInputStream(propFileName);
			prop.load(fit);
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		try {
			fit.close();
		} catch (IOException e) {
			System.out.println("exception caught while closing config.properties stream");
		}
		System.out.println("config.properties loaded");
		return prop;
	}
	public static Stage loadWindow(String fxmlProp, Stage primaryStage, boolean resizable, String appIconPath, String title) {
		Stage stage = null;
		try {
			URL fxmlURL = new File(Main.prop.getProperty(fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = (Parent) fxmlLoader.load();	
			stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(root));
			HBox titleBox = (HBox)fxmlLoader.getNamespace().get("titleHBox");
			WindowStyle.allowDrag(titleBox, stage);
			if(resizable)
				new FXResizeHelper(stage,5,5);
			if(appIconPath != null)
				stage.getIcons().add(new Image(new File(Main.prop.getProperty("appIconPath")).toURI().toString()));
			if(title != null)
				stage.setTitle(Main.prop.getProperty(title));
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
	
	public static boolean isAlpha(String name) {
	    char[] chars = name.toCharArray();
	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static void errorMsg(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
    //    alert.setTitle(titleBar);
   //     alert.setHeaderText(headerMessage);
        alert.setContentText(msg);
        alert.showAndWait();
	}
	
	public static void confirmMsg(String msg) {
		Alert alert = new Alert(AlertType.NONE);
    //    alert.setTitle(titleBar);
   //     alert.setHeaderText(headerMessage);
        alert.setContentText(msg);
        alert.showAndWait();
	}
	
	public static void popUpDialog(StackPane root, String header, String body) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(header));
		content.setBody(new Text(body));
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER,true);
		JFXButton button = new JFXButton("Close");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
			}
		});
		content.setActions(button);
	    dialog.show();
	}
	
	
	public static boolean charArrayContains(char[] chars, char letter) {
		for (char x : chars) {
	        if (x == letter) {
	            return true;
	        }
	    }
		return false;
	}
}
