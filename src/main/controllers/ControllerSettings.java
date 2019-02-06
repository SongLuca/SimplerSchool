package main.controllers;

import java.io.File;
import com.jfoenix.controls.JFXButton;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.application.Main;
import main.utils.WindowStyle;

public class ControllerSettings {
	@FXML
	private Pane pop;
	@FXML
	private JFXButton backButton;

	private double prefHeight = 630, prefWidth = 765;

	public void initialize() {
		initTitleBox();
		backButton.setDisable(true);
		new ZoomOut(pop).play();
	}

	/*********** Custom Window title bar ************/
	@FXML
	private HBox titleHBox;
	@FXML
	private Label title;
	@FXML
	private JFXButton titleCloseButton;
	@FXML
	private JFXButton titleMaxmizeButton;
	@FXML
	private ImageView titleCloseImage;
	@FXML
	private ImageView titleMaxmizeImage;
	
	public void initTitleBox() {
		WindowStyle.stageDimension(prefWidth, prefHeight);
		titleCloseButton.setOnMouseEntered(e -> {
			String img = new File(Main.prop.getProperty("titleCloseHoverImagePath")).toURI().toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = new File(Main.prop.getProperty("titleCloseImagePath")).toURI().toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = new File(Main.prop.getProperty("titleMaxmizeHoverImagePath")).toURI().toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = new File(Main.prop.getProperty("titleMaxmizeImagePath")).toURI().toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) pop.getScene().getWindow());
		});
		titleMaxmizeButton.setOnMouseClicked(e ->{
			 MaxMinScreen();
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					 MaxMinScreen();
				}
			}
		});

	}
	
	public void MaxMinScreen() {
		Stage stage = (Stage) pop.getScene().getWindow();
		if(!WindowStyle.isFullScreen(stage)) {
			WindowStyle.fullScreen(stage);
		}
		else {
			WindowStyle.restoreScreen(stage);
		}
	}

	/***********************************************/
}
