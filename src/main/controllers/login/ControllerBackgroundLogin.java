package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
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
		openLogin();
	}
}
