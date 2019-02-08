package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.event.ActionEvent;
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
import main.database.DataBaseHandler;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerRegister {
	@FXML
	private Label registerLabel;
	
	@FXML
	private AnchorPane registerPane;
	
	@FXML
	private Circle profileImage;
	
	@FXML
	private JFXTextField usernameField;
	
	@FXML
	private JFXPasswordField passField, confirmPassField;
	
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
	
	@FXML
	public boolean register(ActionEvent event) {
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray(),
				   confirmpw = confirmPassField.getText().toCharArray();
		if(username.trim().length() < 5) {
			SimplerSchoolUtil.errorMsg("Username atleast 5 chars!");
			return false;
		}
		if(!SimplerSchoolUtil.isAlpha(username)) {
			SimplerSchoolUtil.errorMsg("Username is not valid!");
			return false;
		}
		if(SimplerSchoolUtil.charArrayContains(confirmpw, ' ')) {
			SimplerSchoolUtil.errorMsg("Password contains space!");
			return false;
		}
		if(confirmpw.length < 5) {
			SimplerSchoolUtil.errorMsg("Password atleast 5 chars!");
			return false;
		}
		if(!Arrays.equals(password, confirmpw)) {
			SimplerSchoolUtil.errorMsg("Password not match!");
			return false;
		}	
		
		if(!DataBaseHandler.getInstance().runRegisterValidateQuery(username)) {
			SimplerSchoolUtil.errorMsg("Username already taken!");
			return false;
		}
		
		DataBaseHandler.getInstance().updateUtenteTable(username, password);
		//SimplerSchoolUtil.confirmMsg("Registration completed");
		openRegCompleted(event);
		return true;
	}
	
	public void openRegCompleted(ActionEvent e1) {
		try {
			AnchorPane regCompleted = FXMLLoader.load(new File(Main.prop.getProperty("regCompletedFXML")).toURI().toURL());
			WindowStyle.setAnchorPaneConstraints(regCompleted, 50, 50, 275, 275);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#root");
			backgroundLogin.getChildren().add(regCompleted);
			registerPane.setVisible(false);
			new ZoomIn(regCompleted).play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectAvatar() {
		
	}
	
	public void initialize() {
		profileImage.setFill(
				new ImagePattern(new Image(new File(Main.prop.getProperty("defaultAvatar")).toURI().toString())));
	}

	/***********************************************/
}
