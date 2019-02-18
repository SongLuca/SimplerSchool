package main.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.application.models.MetaData;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerAddOS {
	@FXML
	private JFXTextField nomeField;
	
	@FXML
    private Label msgLbl;
	
	@FXML
	public void apply(MouseEvent event) {
		String nomeOS = nomeField.getText();
		if(nomeOS.trim().length() == 0) {
			msgLbl.setText("Invalid name");
		}
		else {
			System.out.println("nuovo orario settimanale creato : "+nomeOS);
			((ControllerOrarioS) MetaData.controller).initCalendar(nomeOS);
			cancel(event);
		}
	}

	@FXML
	public void cancel(MouseEvent event) {
		WindowStyle.close((Stage)nomeField.getScene().getWindow());
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
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) nomeField.getScene().getWindow());
		});
	}
	/***********************************************/
}
