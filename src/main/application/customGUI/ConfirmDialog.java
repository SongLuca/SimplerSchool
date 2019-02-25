package main.application.customGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.models.Config;

public class ConfirmDialog extends Dialog<Boolean>{
	
	public ConfirmDialog(Stage owner, String msg) {
        try {
        	URL fxmlURL = new File(Config.getString("config", "customDialogFXML")).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();
            initModality(Modality.WINDOW_MODAL);
            initStyle(StageStyle.UNDECORATED);
            initOwner(owner);
           	getDialogPane().setContent(root);
            JFXButton yesButton = (JFXButton)root.getScene().lookup("#yesBtn");
            JFXButton noButton = (JFXButton)root.getScene().lookup("#noBtn");
            Label msgLbl = (Label)root.getScene().lookup("#msg");
            msgLbl.setText(msg);
            yesButton.setOnMouseClicked(e->{
            	setResult(true);
            });
            noButton.setOnMouseClicked(e->{
            	setResult(false);
            });
            root.requestFocus();
            showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
