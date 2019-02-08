package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.application.Main;
import main.utils.WindowStyle;

public class ControllerPasswordRecover {
	@FXML
	private Label backLabel;
	@FXML
	private AnchorPane recoverPane;

	@FXML
	void openLogin(MouseEvent e1) {
		try {
			AnchorPane login = FXMLLoader.load(new File(Main.prop.getProperty("loginFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(login, 50, 50, 275, 275);
			login.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
			backgroundLogin.getChildren().add(login);

			FadeOutLeft fadeOutLeft = new FadeOutLeft(recoverPane);
			fadeOutLeft.setOnFinished(e -> {
				recoverPane.setVisible(false);
			});
			new FadeInRight(login).play();
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

	}

	/***********************************************/
}
