package main.controllers;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.application.Main;
import main.utils.FXResizeHelper;
import main.utils.WindowStyle;

public class ControllerSettings {
	@FXML
	private Pane pop;
	
	@FXML
	private JFXButton backButton;
	
	@FXML
	private AnchorPane contentPane;
	
	@FXML
	private AnchorPane mainPane;
	
	@FXML
	private ImageView backImage;
	
	private double prefHeight = 630, prefWidth = 765;
	
	public void initialize() {
		initTitleBox();
		backButton.setVisible(false);
		backButton.setOnMouseEntered(e -> {
			String img = new File(Main.prop.getProperty("backHoverImagePath")).toURI().toString();
			backImage.setImage(new Image(img));
		});
		backButton.setOnMouseExited(e -> {
			String img = new File(Main.prop.getProperty("backImagePath")).toURI().toString();
			backImage.setImage(new Image(img));
		});
		new ZoomOut(pop).play();
	}
	@FXML
	public void backToSettings() { 
		try {
			Parent fxml = FXMLLoader.load(new File(Main.prop.getProperty("settingsFXML")).toURI().toURL());
			Stage currentStage = (Stage)pop.getScene().getWindow();
			currentStage.setScene(new Scene(fxml));
			mainPane.setPrefHeight(currentStage.getHeight());
			mainPane.setPrefWidth(currentStage.getWidth());
			new FXResizeHelper(currentStage,5,5);
			prefHeight = 630;
			prefWidth = 765;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void openOrarioSettimanale(){
		try {
			AnchorPane fxml = FXMLLoader.load(new File(Main.prop.getProperty("orarioSettimanaleFXML")).toURI().toURL());
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(fxml);
			title.setText("Orario Settimanale");
			backButton.setPrefWidth(40);
			backButton.setVisible(true);
			
			AnchorPane subcontentpane = (AnchorPane) contentPane.getScene().lookup("#subContentPane");
			AnchorPane.setBottomAnchor(subcontentpane, 0.0);
			AnchorPane.setTopAnchor(subcontentpane, 0.0);
			AnchorPane.setLeftAnchor(subcontentpane, 0.0);
			AnchorPane.setRightAnchor(subcontentpane, 0.0);
			
			Stage stage = (Stage)pop.getScene().getWindow();
			stage.setMinHeight(Double.parseDouble(Main.prop.getProperty("prefHeightOS")));
			stage.setMinWidth(Double.parseDouble(Main.prop.getProperty("prefWidthOS")));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			new FadeInUp(contentPane).setOnFinished(e->{
				
			});
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			WindowStyle. MaxMinScreen((Stage) pop.getScene().getWindow());
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					WindowStyle. MaxMinScreen((Stage) pop.getScene().getWindow());
				}
			}
		});
	}
	/***********************************************/
}
