package main.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.application.models.MetaData;
import main.utils.Console;
import main.utils.WindowStyle;

public class ControllerAddOS {
	@FXML
	private JFXTextField nomeField;
	
	@FXML
    private Label msgLbl;
	
	@FXML
	public void apply(MouseEvent event) {
		String nomeOS = nomeField.getText();
		if(nomeOS.trim().length() == 0) {
			msgLbl.setText("Invalid name");
		}
		else {
			Console.print("nuovo orario settimanale creato : "+nomeOS, "app");
			((ControllerOrarioS) MetaData.controller).initCalendar(nomeOS);
			cancel(event);
		}
	}

	@FXML
	public void cancel(MouseEvent event) {
		WindowStyle.close((Stage)nomeField.getScene().getWindow());
	}
	
}
