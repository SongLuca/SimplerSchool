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

public class ControllerLogin {
	@FXML
	private Label registerLabel;
	@FXML
	private AnchorPane loginPane;

	@FXML
	void animation(MouseEvent event) {
		// new FadeOutRight(loginPane).play();
	}

	@FXML
	void openRegister(MouseEvent e1) {
		try {
			AnchorPane register = FXMLLoader.load(new File(Main.prop.getProperty("registerFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(register, 50, 50, 275, 275);
			register.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#root");
			backgroundLogin.getChildren().add(register);

			FadeOutLeft fadeOutLeft = new FadeOutLeft(loginPane);
			fadeOutLeft.setOnFinished(e -> {
				loginPane.setVisible(false);
			});
			new FadeInRight(register).play();
			register.setVisible(true);
			fadeOutLeft.play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void openRecover(MouseEvent e1) {
		try {
			AnchorPane recover = FXMLLoader
					.load(new File(Main.prop.getProperty("passwordRecoverFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(recover, 50, 50, 275, 275);
			recover.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#root");
			backgroundLogin.getChildren().add(recover);

			FadeOutRight fadeOutRight = new FadeOutRight(loginPane);
			fadeOutRight.setOnFinished(e -> {
				loginPane.setVisible(false);
			});
			new FadeInLeft(recover).play();
			recover.setVisible(true);
			fadeOutRight.play();
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
