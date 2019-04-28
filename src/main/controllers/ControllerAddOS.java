package main.controllers;

import java.util.HashMap;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.database.DataBaseHandler;
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
			HashMap<Integer, OrarioSettimanale> orariS = DataBaseHandler.getInstance().getOS();
			boolean valido = true;
			for(int key : orariS.keySet()) {
				if(nomeOS.equals(orariS.get(key).getNomeOrario())) {
					valido = false;
					break;
				}
			}
			if(valido) {
				Console.print("nuovo orario settimanale creato : "+nomeOS, "app");
				((ControllerOrarioS) MetaData.controller).initCalendar(nomeOS);
				((ControllerOrarioS) MetaData.controller).setOSButtonVisible(true);
				cancel(event);
			}
			else {
				msgLbl.setText("Nome esistente");
			}
		}
	}

	@FXML
	public void cancel(MouseEvent event) {
		WindowStyle.close((Stage)nomeField.getScene().getWindow());
	}
	
}
