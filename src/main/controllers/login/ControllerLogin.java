package main.controllers.login;

import java.io.IOException;
import java.net.MalformedURLException;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import main.application.Main;
import main.application.models.Config;
import main.database.DataBaseHandler;
import main.utils.Effect;
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
	private JFXSpinner loading;
	
	@FXML
	private JFXCheckBox rememberMe;

	@FXML
	void openRegister(MouseEvent e1) {
		try {
			AnchorPane register = FXMLLoader.load(SimplerSchoolUtil.getFileURIByPath("config", "registerFXML").toURL());
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
			AnchorPane recover = FXMLLoader.load(SimplerSchoolUtil.getFileURIByPath("config", "passwordRecoverFXML").toURL());
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
		loginPane.setDisable(true);
		StackPane root = (StackPane) ((Node) e.getSource()).getScene().lookup("#rootStack");
		AnchorPane rootPane = (AnchorPane) ((Node) e.getSource()).getScene().lookup("#rootPane");
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray();

		if (username.trim().length() < 5) {
			SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", "Username is too short!");
			loginPane.setDisable(false);
			return false;
		}
		if (password.length < 5) {
			SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", "Password can not be empty!");
			loginPane.setDisable(false);
			return false;
		}
		Task<Boolean> loginValidateTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				loginPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().runValidateUserQuery(username, password);
			}
		};

		loginValidateTask.setOnFailed(event -> {
			loading.setVisible(false);
			loginPane.setDisable(false);
			loginValidateTask.getException().printStackTrace();
		});

		loginValidateTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			loginPane.setEffect(null);
			if (loginValidateTask.getValue()) {
				DataBaseHandler.getInstance().preLoadUserData();
				endAnimation(e, root);
				if(rememberMe.isSelected()) {
					Config.userConfig.setProperty("rememberMe", "true");
					Config.userConfig.setProperty("rememberedUser", username);
				}
				else{
					Config.userConfig.setProperty("rememberMe", "false");
					Config.userConfig.setProperty("rememberedUser", "");
				}
				SimplerSchoolUtil.saveProperties(Config.userConfig, "userconfig", true);
			} else {
				SimplerSchoolUtil.popUpDialog(root, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				loginPane.setDisable(false);
			}
		});
		new Thread(loginValidateTask).start();

		return false;
	}

	public void endAnimation(ActionEvent e, Parent root) {
		Timeline timeline = new Timeline();
		KeyFrame key = new KeyFrame(Duration.millis(1500), new KeyValue(loginPane.opacityProperty(), 0));
		timeline.getKeyFrames().add(key);
		timeline.setOnFinished((ae) -> {
			WindowStyle.closeByRoot(root);
			SimplerSchoolUtil.newWindow("mainFXML", true, "appIconPath", "Simpler School -"+Main.utente.getUserid(),1300,800);
		});
		timeline.play();
	}
	
	public void initialize() {
		if(Config.getBoolean("userconfig", "rememberMe")) {
			rememberMe.setSelected(true);
			usernameField.setText(Config.getString("userconfig", "rememberedUser"));
		}
		else {
			rememberMe.setSelected(false);
		}
		loading.setVisible(false);
		passField.setText("12345");
		
	}

	/***********************************************/
}
