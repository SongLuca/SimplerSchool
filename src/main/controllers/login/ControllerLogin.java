package main.controllers.login;

import java.io.File;
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
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerLogin {
	
	@FXML
	private Label registerLabel;
	
	@FXML
	private Label notRegLbl;
	
	@FXML
	private Label forgotPassLbl;
	
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
			AnchorPane register = FXMLLoader.load(getClass().getResource(Config.getString(Main.CONFIG, "registerFXML")));
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
			AnchorPane recover = FXMLLoader.load(getClass().getResource(Config.getString(Main.CONFIG, "passwordRecoverFXML")));
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
			Utils.popUpDialog(root, rootPane, "Error", "Username is too short!");
			loginPane.setDisable(false);
			return false;
		}
		if (password.length < 5) {
			Utils.popUpDialog(root, rootPane, "Error", "Password can not be empty!");
			loginPane.setDisable(false);
			return false;
		}
		
		File dbFoler = new File(Config.getString(Main.DBINFO, "databaseFolder"));
		if(!dbFoler.exists()) {
			Utils.popUpDialog(root, rootPane, "Error", "Could not locate local server folder! Run SSTool to fix the problem");
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
			loginPane.setEffect(null);
		});

		loginValidateTask.setOnSucceeded(event -> {
			if (loginValidateTask.getValue()) {
				Console.print("Login successfully", "app");
				Utils.readUserProperties();
				DataBaseHandler.getInstance().preLoadUserData();
				endAnimation(e, root);
				if(rememberMe.isSelected()) {
					Config.appConfig.setProperty("rememberMe", "true");
					Config.appConfig.setProperty("rememberedUser", username);
				}
				else{
					Config.appConfig.setProperty("rememberMe", "false");
					Config.appConfig.setProperty("rememberedUser", "");
				}
				Utils.saveAppProperties(true);;
			} else {
				Utils.popUpDialog(root, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				loading.setVisible(false);
				loginPane.setEffect(null);
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
			loading.setVisible(false);
			loginPane.setEffect(null);
			WindowStyle.closeByRoot(root);
			Utils.newWindow("mainFXML", true, "appIconPath", "Simpler School -"+Main.utente.getUserid(),1400,800);
		});
		timeline.play();
	}

	
	public void initLangBindings() {
		LanguageBundle.labelForValue(notRegLbl, ()->LanguageBundle.get("notRegLbl", 0));
		LanguageBundle.labelForValue(registerLabel, ()->LanguageBundle.get("registerLabel", 0));
		LanguageBundle.labelForValue(forgotPassLbl, ()->LanguageBundle.get("forgotPassLbl", 0));
		LanguageBundle.textFieldForValue(usernameField, ()->LanguageBundle.get("usernameField", 0));
	}
	
	public void initialize() {
		initLangBindings();
		if(Config.getBoolean(Main.APPCONFIG, "rememberMe")) {
			rememberMe.setSelected(true);
			usernameField.setText(Config.getString(Main.APPCONFIG, "rememberedUser"));
		}
		else {
			rememberMe.setSelected(false);
		}
		loading.setVisible(false);
	}

	/***********************************************/
}
