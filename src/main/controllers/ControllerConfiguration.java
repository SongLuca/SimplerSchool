package main.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
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
    private Label votoRangeLbl, languageLbl, notificaLbl, themeLbl;

    @FXML
    private JFXTextField votoMinField, votoMaxField;

    @FXML
    private JFXComboBox<String> langComboBox, themeBox;
    
    @FXML
    private JFXRadioButton notificaHomework, notificaExam, notificaOral;
    
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
    	notificaHomework.setSelected(Config.getBoolean(Main.USERCONFIG, "compitiPerCasaNotifica"));
    	notificaExam.setSelected(Config.getBoolean(Main.USERCONFIG, "verificaNotifica"));
    	notificaOral.setSelected(Config.getBoolean(Main.USERCONFIG, "interrogazioneNotifica"));
    	langComboBox.getItems().addAll(langs);
    	langComboBox.getSelectionModel().select(Preferences.defaultLang);
    	votoMinField.setText(Preferences.votoMin);
    	votoMaxField.setText(Preferences.votoMax);
    	saveBtn.setOnMouseClicked(e->{
    		StackPane root = (StackPane) subContentPane.getScene().lookup("#dialogStack");
			String selected = langComboBox.getSelectionModel().getSelectedItem();
    		if(!selected.equals(Preferences.defaultLang)) {
    			Preferences.defaultLang = selected;
    			String lang = selected.substring(0,2);
        		LanguageBundle.setLocale(new Locale(lang));
        		Config.appConfig.setProperty("selectedLanguage", selected);
        		Utils.saveProperties(Main.APPCONFIG, true);
        		Console.print("Change UI language to " + selected, "GUI");
    		}
    		Config.userConfig.setProperty("votoMax", votoMaxField.getText());
    		Config.userConfig.setProperty("votoMin", votoMinField.getText());
    		Config.userConfig.setProperty("compitiPerCasaNotifica", notificaHomework.isSelected()+"");
    		Config.userConfig.setProperty("verificaNotifica", notificaExam.isSelected()+"");
    		Config.userConfig.setProperty("interrogazioneNotifica", notificaOral.isSelected()+"");
    		Utils.saveProperties(Main.USERCONFIG, true);
    		Preferences.loadPreferences();
    		Utils.popUpDialog(root, subContentPane, LanguageBundle.get("message"),LanguageBundle.get("changesSaved"));
    		MetaData.cm.loadNoteBoard();
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
    		if(Double.parseDouble(votoMinField.getText())  > Double.parseDouble(votoMaxField.getText())) {
	    		votoMinField.setStyle("-jfx-focus-color: red; -jfx-unfocus-color: red");
	    		votoMaxField.setStyle("-jfx-focus-color: red; -jfx-unfocus-color: red");
	    		saveBtn.setDisable(true);
	    	}
	    	else {
	    		votoMinField.setStyle("");
	    		votoMaxField.setStyle("");
	    		saveBtn.setDisable(false);
	    	}
    	}
    }
}
