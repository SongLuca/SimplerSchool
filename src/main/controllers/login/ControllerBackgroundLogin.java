package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.application.Main;
import main.utils.WindowStyle;

public class ControllerBackgroundLogin {
	@FXML
	private AnchorPane root;

	void openLogin() {
		try {
			AnchorPane loginPane = FXMLLoader.load(new File(Main.prop.getProperty("loginFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(loginPane, 50, 50, 275, 275);
			root.getChildren().add(loginPane);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		new ZoomIn(root).play();
		initTitleBox();
		openLogin();
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
			String img = new File(Main.prop.getProperty("titleCloseHoverImagePath")).toURI().toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = new File(Main.prop.getProperty("titleCloseImagePath")).toURI().toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) root.getScene().getWindow());
		});
	}
	
	/***********************************************/
}