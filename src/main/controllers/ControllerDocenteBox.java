package main.controllers;

import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeOutRight;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.application.models.MetaData;
import main.utils.LanguageBundle;

public class ControllerDocenteBox {
    @FXML
    private HBox docenteBox;

    @FXML
    private JFXTextField nomeField;

    @FXML
    private JFXTextField cognomeField;

    @FXML
    private Label idDocente;
    
	public void initialize() {
		nomeField.setPromptText(LanguageBundle.get("firstname"));
		cognomeField.setPromptText(LanguageBundle.get("surname"));
	}
    
    @FXML
    void delete(MouseEvent event) {
    	VBox mBox = (VBox) ((Node) event.getSource()).getScene().lookup("#docentiBox");
		if(!idDocente.getText().isEmpty())
			MetaData.cd.addToRemove(Integer.parseInt(idDocente.getText()));
		new FadeOutRight(docenteBox).play();
		mBox.getChildren().remove(docenteBox);
    }

}
