package main.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.application.Main;
import main.application.models.Config;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.Utils;

public class ControllerConfiguration {
	@FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton defaultBtn;

    @FXML
    private Label votoRangeLbl;

    @FXML
    private JFXTextField votoMinField;

    @FXML
    private JFXTextField votoMaxField;

    @FXML
    private JFXComboBox<String> langComboBox;

    @FXML
    private Label languageLbl;
    
    private String selectedLang;
    
    private List<String> langs = Arrays.asList("English","Italino");
    
    public void initialize() {
    	initComponents();
    	initLangBindings();
    }
    
    public void initComponents() {
    	selectedLang = Config.getString(Main.USERCONFIG, "selectedLanguage");
    	langComboBox.getItems().addAll(langs);
    	langComboBox.getSelectionModel().select(selectedLang);
    	saveBtn.setOnMouseClicked(e->{
    		String selected = langComboBox.getSelectionModel().getSelectedItem();
    		if(!selected.equals(selectedLang)) {
    			selectedLang = selected;
    			String lang = selected.substring(0,2);
        		LanguageBundle.setLocale(new Locale(lang));
        		Config.userConfig.setProperty("selectedLanguage", selected);
        		Utils.saveProperties(Main.USERCONFIG, true);
        		Console.print("Change UI language to " + selected, "GUI");
        		popMsg("Changes have been applied");
    		}
    	});
    }
    
    public void popMsg(String msg) {
    	StackPane root = (StackPane) votoRangeLbl.getScene().lookup("#rootStack");
		AnchorPane pane = (AnchorPane) votoRangeLbl.getScene().lookup("#rootPane");
		Utils.popUpDialog(root, pane, "Message",msg);
    }
    
    public void initLangBindings() {
    	LanguageBundle.labelForValue(languageLbl, ()->LanguageBundle.get("languageLbl", 0));
    	LanguageBundle.buttonForValue(saveBtn, ()->LanguageBundle.get("saveBtn", 0));
    }
}
