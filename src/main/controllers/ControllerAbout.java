package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import main.utils.Console;

public class ControllerAbout {
	
	@FXML
	private AnchorPane subContentPane;
	
	public void initialize() {
		Console.print("Opening about gui", "gui");
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
	}
}
