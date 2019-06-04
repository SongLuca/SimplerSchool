package main.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Config;
import main.application.models.MetaData;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.Preferences;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerConfiguration {
	
	@FXML
	private AnchorPane subContentPane;
	 
	@FXML
    private JFXButton saveBtn, defaultBtn, closeBtn;

    @FXML
    private Label votoRangeLbl, languageLbl;

    @FXML
    private JFXTextField votoMinField, votoMaxField;

    @FXML
    private JFXComboBox<String> langComboBox;
    
    private List<String> langs = Arrays.asList("English","Italiano");
    
    private boolean votoRangeCheck;
    
    public void initialize() {
    	votoRangeCheck = true;
    	AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
    	initComponents();
    	initLangBindings();
    }
    
    public void initComponents() {
    	langComboBox.getItems().addAll(langs);
    	langComboBox.getSelectionModel().select(Preferences.defaultLang);
    	votoMinField.setText(Preferences.votoMin);
    	votoMaxField.setText(Preferences.votoMax);
    	saveBtn.setOnMouseClicked(e->{
    		if(votoRangeCheck) {
    			String selected = langComboBox.getSelectionModel().getSelectedItem();
        		if(!selected.equals(Preferences.defaultLang)) {
        			Preferences.defaultLang = selected;
        			String lang = selected.substring(0,2);
            		LanguageBundle.setLocale(new Locale(lang));
            		Config.userConfig.setProperty("selectedLanguage", selected);
            		Utils.saveProperties(Main.USERCONFIG, true);
            		Console.print("Change UI language to " + selected, "GUI");
            		popMsg("Changes have been applied");
        		}
        		
        		MetaData.cm.loadNoteBoard();
    		}
    		else {
    			popMsg("votoRangeCheck");
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
    
    @FXML
	public void close() {
		WindowStyle.close((Stage) subContentPane.getScene().getWindow());
	}
    
    public void popMsg(String msg) {
    	StackPane root = (StackPane) votoRangeLbl.getScene().lookup("#dialogStack");
    	Utils.popUpDialog(root, subContentPane, LanguageBundle.get("message"),LanguageBundle.get("changesSaved"));
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
	    		votoRangeCheck = false;
	    	}
	    	else {
	    		votoMinField.setStyle("");
	    		votoMaxField.setStyle("");
	    		votoRangeCheck = true;
	    	}
    	}
    }
}
