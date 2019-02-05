package main.controllers;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

public class ControllerSettings {
	@FXML
	private Pane pop;
	@FXML
	private Label title;
	
	public void initialize() {
		title.setTextAlignment(TextAlignment.JUSTIFY);
		title.setWrapText(true);
		new ZoomOut(pop).play();
	}
}
