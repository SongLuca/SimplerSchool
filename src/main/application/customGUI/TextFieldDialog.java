package main.application.customGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.models.Config;
import main.application.models.OrarioSettimanale;

public class TextFieldDialog extends Dialog<String>{
	public TextFieldDialog(Stage owner, HashMap<Integer, OrarioSettimanale> orariS, String nomeOriginale, String msg) {
		try {
			URL fxmlURL = new File(Config.getString("config", "customTFDialogFXML")).toURI().toURL();
			FXMLLoader loader = new FXMLLoader(fxmlURL);
			Parent root = loader.load();
			initModality(Modality.WINDOW_MODAL);
			initStyle(StageStyle.UNDECORATED);
			initOwner(owner);
			getDialogPane().setContent(root); 
			JFXButton yesButton = (JFXButton) root.getScene().lookup("#okBtn");
			JFXButton noButton = (JFXButton) root.getScene().lookup("#cancelBtn");
			Label msgLbl = (Label) root.getScene().lookup("#msg");
			Label errorLbl = (Label) root.getScene().lookup("#errorMsg");
			JFXTextField tf = (JFXTextField) root.getScene().lookup("#inputTF");
			errorLbl.setStyle("-fx-text-fill:red;");
			tf.setText(nomeOriginale);
			msgLbl.setText(msg);
			yesButton.setOnMouseClicked(e -> {
				boolean taken = false;
				if(tf.getText().trim().length() == 0) {
					errorLbl.setText("invalid name");
				}
				else if(tf.getText().equals(nomeOriginale)) {
					setResult(tf.getText());
				}
				else {
					for(int key : orariS.keySet()) {
						if(orariS.get(key).getNomeOrario().equals(tf.getText())) {
							errorLbl.setText("name already taken");
							taken = true;
						}
					}
				}
				if(!taken)
					setResult(tf.getText());
			});
			noButton.setOnMouseClicked(e -> {
				setResult("");
			});
			root.requestFocus();
			showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
