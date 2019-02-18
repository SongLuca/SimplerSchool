package main.controllers;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.application.models.Config;
import main.utils.FXResizeHelper;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerSettings {
	@FXML
	private Pane pop;
	
	@FXML
	private JFXButton backButton;
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private StackPane rootStack;
	
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
			String img = SimplerSchoolUtil.getFileURI("config", "backHoverImagePath").toString();
			backImage.setImage(new Image(img));
		});
		backButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURI("config", "backImagePath").toString();
			backImage.setImage(new Image(img));
		});
		new ZoomOut(pop).play();
	}
	@FXML
	public void backToSettings() { 
		try {
			Parent fxml = FXMLLoader.load(SimplerSchoolUtil.getFileURI("config", "settingsFXML").toURL());
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
			AnchorPane fxml = FXMLLoader.load(SimplerSchoolUtil.getFileURI("config", "orarioSettimanaleFXML").toURL());
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
			stage.setMinHeight(Config.getDouble("config", "prefHeightOS"));
			stage.setMinWidth(Config.getDouble("config", "prefWidthOS"));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			new FadeInUp(contentPane).setOnFinished(e->{
				
			});
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void testt(MouseEvent event) {
		
		Object o = event.getSource();
		   if(o instanceof Region){
		       Background b = mainPane.getBackground();
		       Paint p = b.getFills().get(0).getFill();//paint is actually your color :)
		       if(p instanceof Color){
		    	   Color c = (Color) p;
		    	   String hex = String.format( "#%02X%02X%02X",
		    	               (int)( c.getRed() * 255 ),
		    	               (int)( c.getGreen() * 255 ),
		    	               (int)( c.getBlue() * 255 ) );
		    	   System.out.println(hex);
		       }
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
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("config", "titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("config", "titleMaxmizeImagePath").toString();
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
