package main.controllers;

import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerInsertTask {

	@FXML
	private JFXDatePicker datePicker;

	@FXML
	private JFXComboBox<String> tipoBox;

	@FXML
	private JFXTextArea commento;

	@FXML
	private JFXListView<?> fileListView;

	@FXML
	private JFXButton insertBtn;

	@FXML
	private JFXButton cancelBtn;

	public void initialize() {
		initTitleBox();
		initComponents();
	}
	
	public void initComponents() {
		
	}
	
	@FXML
	public void setToToday() {
		datePicker.setValue(LocalDate.now());
	}
	
	@FXML
	public void addFile() {
		
	}
	
	public void insert() {
		
	}
	
	public void cancel() {
		WindowStyle.close((Stage) titleHBox.getScene().getWindow());
	}
	
	
	/*********** Custom Window title bar ************/
	@FXML
	private HBox titleHBox;

	@FXML
	private Label title;

	@FXML
	private JFXButton titleCloseButton;

	@FXML
	private ImageView titleCloseImage;

	public void initTitleBox() {
		
		titleCloseButton.setOnMouseEntered(e -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) titleHBox.getScene().getWindow());
		});
	}
	/***********************************************/

}
