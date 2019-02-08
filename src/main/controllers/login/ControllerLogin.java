package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
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
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
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
		StackPane root = (StackPane) ((Node) e.getSource()).getScene().lookup("#rootStack");
		AnchorPane rootPane = (AnchorPane) ((Node) e.getSource()).getScene().lookup("#rootPane"); 
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray();
		
		if(username.trim().length() < 5) {
			SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", "Username is too short!");
			return false;
		}
		if(password.length < 5) {
			SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", "Password can not be empty!");
			return false;
		}
		
		if(!DataBaseHandler.getInstance().runValidateUserQuery(username,password)) {
			SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
			return false;
		}
		
		endAnimation(e,root);
		return true;
	}
	
	public void endAnimation(ActionEvent e,Parent root) {
		 Timeline timeline = new Timeline();
         KeyFrame key = new KeyFrame(Duration.millis(1500),
                        new KeyValue (loginPane.opacityProperty(), 0)); 
         timeline.getKeyFrames().add(key);   
         timeline.setOnFinished((ae) -> {
        	 WindowStyle.closeByRoot(root);
        	 SimplerSchoolUtil.newWindow("mainFXML", true, "appIconPath", "Simpler School");
         }); 
         timeline.play();
	}
	
	public void initialize() {
		
	}

	/***********************************************/
}
