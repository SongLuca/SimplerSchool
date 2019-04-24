package main.controllers.login;

import java.io.IOException;
import java.net.MalformedURLException;
import com.jfoenix.controls.JFXButton;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.utils.Console;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerBackgroundLogin {
	@FXML
	private StackPane rootStack;
	@FXML
	private AnchorPane rootPane;
	
	void openLogin() {
		try {
			AnchorPane loginPane = FXMLLoader.load(Utils.getFileURIByPath("config", "loginFXML").toURL());
			WindowStyle.setAnchorPaneConstraints(loginPane, 50, 50, 275, 275);
			rootPane.getChildren().add(loginPane);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		new ZoomIn(rootPane).play();
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
			String img = Utils.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) rootStack.getScene().getWindow());
			Console.print("Terminating application", "app");
		});
	}
	
	/***********************************************/
}
