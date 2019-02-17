package main.controllers;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControllerMateriaBox {

	@FXML
	private HBox materiaBox;

	@FXML
	private JFXTextField nomeMateria;

	@FXML
	private JFXColorPicker coloreMateria;

	public void initialize() {
	}
	
	@FXML
	public void deleteMateria(MouseEvent event) {
		System.out.println("delete materia");
		VBox mBox = (VBox) ((Node) event.getSource()).getScene().lookup("#materieBox");
		mBox.getChildren().remove(materiaBox);
	}

}
