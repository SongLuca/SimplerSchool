package main.controllers;

import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeOutRight;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.application.models.MetaData;

public class ControllerMateriaBox {

	@FXML
	private HBox materiaBox;

	@FXML
	private JFXTextField nomeMateria;

	@FXML
	private ColorPicker coloreMateria;
	
	@FXML
    private Label idMateria;
	
	public void initialize() {
		
	}
	
	@FXML
	public void deleteMateria(MouseEvent event) {
		VBox mBox = (VBox) ((Node) event.getSource()).getScene().lookup("#materieBox");
		if(!idMateria.getText().isEmpty())
			MetaData.cmat.addToRemove(Integer.parseInt(idMateria.getText()));
		new FadeOutRight(materiaBox).play();
		mBox.getChildren().remove(materiaBox);
	}

}
