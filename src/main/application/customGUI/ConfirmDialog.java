package main.application.customGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.Main;
import main.application.models.Config;
import main.utils.LanguageBundle;
import main.utils.Preferences;

public class ConfirmDialog{
	private Stage stage;
	private boolean result;
	
	public ConfirmDialog(Stage owner, String msg) {
        try {
        	URL fxmlURL = new File(Config.getString(Main.CONFIG, "customDialogFXML")).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(owner);
            String cssUrl = getClass().getResource("/main/resources/gui/css/"+Preferences.theme+".css").toExternalForm();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(cssUrl);
    		scene.setFill(Color.TRANSPARENT);
    		stage.setScene(scene);
            JFXButton yesButton = (JFXButton)root.getScene().lookup("#yesBtn");
            JFXButton noButton = (JFXButton)root.getScene().lookup("#noBtn");
            Label msgLbl = (Label)root.getScene().lookup("#msg");
            LanguageBundle.labelForValue(msgLbl, ()->LanguageBundle.get(msg, 0));
            LanguageBundle.buttonForValue(yesButton, ()->LanguageBundle.get("yesBtn", 0));
            LanguageBundle.buttonForValue(noButton, ()->LanguageBundle.get("noBtn", 0));
            yesButton.setOnMouseClicked(e->{
            	result=true;
            	stage.close();
            });
            noButton.setOnMouseClicked(e->{
            	result=false;
            	stage.close();
            });
            root.requestFocus();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public boolean getResult() {
		return result;
	}
}
