package main.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import main.application.Main;
import main.application.models.Config;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.Preferences;
import main.utils.Utils;

public class ControllerConfiguration {
	
	@FXML
	private AnchorPane subContentPane;
	 
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
    
    private List<String> langs = Arrays.asList("English","Italiano");
    
    public void initialize() {
    	AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
    	initComponents();
    	initLangBindings();
    }
    
    public void initComponents() {
    	selectedLang = Preferences.defaultLang;
    	langComboBox.getItems().addAll(langs);
    	langComboBox.getSelectionModel().select(selectedLang);
    	votoMinField.setText(Preferences.votoMin);
    	votoMaxField.setText(Preferences.votoMax);
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
    	
    	votoMinField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	detectOnChange();
		    }
		});
    	
    	votoMaxField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	detectOnChange();
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
    
    public void checkVotoRange() {
    	int votoMin = Integer.parseInt(votoMinField.getText());
    	int votoMax = Integer.parseInt(votoMaxField.getText());
    	
    	if(votoMin >= votoMax) {
    	
    	}
    }
    
    public void detectOnChange() {
    	if(!votoMinField.getText().isEmpty() && !votoMaxField.getText().isEmpty()) {
    		if(Integer.parseInt(votoMinField.getText())  > Integer.parseInt(votoMaxField.getText())) {
	    		votoMinField.setStyle("-jfx-focus-color: red; -jfx-unfocus-color: red");
	    		votoMaxField.setStyle("-jfx-focus-color: red; -jfx-unfocus-color: red");
	    	}
	    	else {
	    		votoMinField.setStyle("");
	    		votoMaxField.setStyle("");
	    	}
    	}
    }
    
   
    
    
    
    
}
