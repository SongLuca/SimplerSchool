package main.controllers;

import com.jfoenix.controls.JFXTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class attivitaBoxController {
	@FXML
	private Label idLbl;
	
	@FXML
	private Label materiaLbl;

	@FXML
	private JFXTextArea comment;
	
	@FXML
    private TitledPane titlePane;
	
	@FXML
    private Pane contentPane;
	
	public void initialize() {
		
	}
	
	@FXML
	void edit(MouseEvent event) {

	}

	@FXML
	void remove(MouseEvent event) {

	}

	@FXML
	void viewAllegati(MouseEvent event) {

	}
}
