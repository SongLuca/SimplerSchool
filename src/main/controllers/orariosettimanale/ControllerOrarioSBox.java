package main.controllers.orariosettimanale;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class ControllerOrarioSBox {
	
	@FXML
	private JFXButton osButton;
	 
    @FXML
    private ImageView imageOS;

    @FXML
    private Label nomeOS;

    @FXML
    private MenuItem open;

    @FXML
    private MenuItem rename;

    @FXML
    private MenuItem delete;
    
    public void initializa() {
    	
    }
    
    public void setOpenAction(EventHandler<ActionEvent> value) {
    	osButton.setOnAction(value);
    	open.setOnAction(value);
    }
    
    public void setDeleteAction(EventHandler<ActionEvent> value) {
    	delete.setOnAction(value);
    }
    
    public void setRenameAction(EventHandler<ActionEvent> value) {
    	rename.setOnAction(value);
    }
    
    public void setNome(String nome) {
    	nomeOS.setText(nome);
    }
}
