package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.application.Main;
import main.database.DataBaseHandler;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerLogin {
	@FXML
	private Label registerLabel;
	@FXML
	private AnchorPane loginPane;
	@FXML
	private JFXTextField usernameField;
	@FXML
	private JFXPasswordField passField;

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
			StackPane backgroundLogin = (StackPane) ((Node) e1.getSource()).getScene().lookup("#root");
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
			StackPane backgroundLogin = (StackPane) ((Node) e1.getSource()).getScene().lookup("#root");
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
	
	@FXML
	public boolean login(ActionEvent e) {
		StackPane root = (StackPane) ((Node) e.getSource()).getScene().lookup("#root");
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray();
		
		if(username.trim().length() < 5) {
			SimplerSchoolUtil.popUpDialog(root,"Error","Username atleast 5 chars!");
			return false;
		}
		if(password.length < 5) {
			SimplerSchoolUtil.popUpDialog(root,"Error","Password atleast 5 chars!");
			return false;
		}
		
		if(!DataBaseHandler.getInstance().runValidateUserQuery(username,password)) {
			SimplerSchoolUtil.popUpDialog(root,"Error","Nice try ;)");
			return false;
		}
		SimplerSchoolUtil.popUpDialog(root,"GG","WP");
		endAnimation(e);
		return true;
	}
	
	public void endAnimation(ActionEvent e) {
		
	}
	
	public void initialize() {
	}

	/***********************************************/
}
