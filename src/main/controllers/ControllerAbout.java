package main.controllers;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.WindowStyle;

public class ControllerAbout {
	
	@FXML
	private AnchorPane subContentPane;
    @FXML
    private JFXButton closeBtn;
    
	public void initialize() {
		Console.print("Opening about gui", "gui");
		initLangBindings();
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
	}
	
	public void initLangBindings() {
		closeBtn.setText(LanguageBundle.get("close"));
    }
	
	@FXML
	public void close() {
		WindowStyle.close((Stage) subContentPane.getScene().getWindow());
	}
}
