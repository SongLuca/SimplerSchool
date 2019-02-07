package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import main.application.Main;
import main.utils.WindowStyle;

public class ControllerRegister {
	@FXML
	private Label registerLabel;
	@FXML
	private AnchorPane registerPane;
	@FXML
	private Circle profileImage;

	@FXML
	void animation(MouseEvent event) {
		// new FadeOutRight(loginPane).play();
	}

	@FXML
	void openLogin(MouseEvent e1) {
		try {
			AnchorPane login = FXMLLoader.load(new File(Main.prop.getProperty("loginFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(login, 50, 50, 275, 275);
			login.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#root");
			backgroundLogin.getChildren().add(login);

			FadeOutRight fadeOutLeft = new FadeOutRight(registerPane);
			fadeOutLeft.setOnFinished(e -> {
				registerPane.setVisible(false);
			});
			new FadeInLeft(login).play();
			login.setVisible(true);
			fadeOutLeft.play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		profileImage.setFill(
				new ImagePattern(new Image(new File(Main.prop.getProperty("defaultAvatar")).toURI().toString())));
	}

	/***********************************************/
}
