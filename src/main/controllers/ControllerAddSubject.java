package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerAddSubject {
	
	@FXML
	private ChoiceBox<String> subjectBox;

	@FXML
	private ChoiceBox<String> professorBox;

	@FXML
	private ChoiceBox<String> professorBox2;

	@FXML
	void cancel(MouseEvent event) {
		Stage stage = (Stage) subjectBox.getScene().getWindow();
        stage.close();
	}

	@FXML
	void save(MouseEvent event) {

	}
	
	public void initialize() {
		
	}
}
