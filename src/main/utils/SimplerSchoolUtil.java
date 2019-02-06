package main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
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
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stage;
	}
}
