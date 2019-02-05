package main.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.application.Main;

public class SimplerSchoolUtil {

	public static void loadWindow(String fxmlProp, String iconProp, String title) {
		try {
			URL fxmlURL = new File(Main.prop.getProperty(fxmlProp)).toURI().toURL();
			String iconURI = new File(Main.prop.getProperty(iconProp)).toURI().toString();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(Main.primaryStage);
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image(iconURI));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
