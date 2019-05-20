package main.controllers.login;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.application.Main;
import main.database.DataBaseHandler;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerPasswordRecover {
	
	@FXML
	private Label backLabel;
	
	@FXML
	private AnchorPane recoverPane;
	
	@FXML
	private JFXSpinner loading;

	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXPasswordField passField;

	@FXML
	private JFXPasswordField confirmPassField;
	
	@FXML
    private JFXButton recoverBtn;

	@FXML
	void openLogin(MouseEvent e1) {
		try {
			AnchorPane login = FXMLLoader.load(Utils.getFileURIByPath(Main.CONFIG, "loginFXML").toURL());
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

	@FXML
	public boolean recover(MouseEvent e1) {
		StackPane root = (StackPane) ((Node) e1.getSource()).getScene().lookup("#rootStack");
		AnchorPane rootPane = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane"); 
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray(),
				confirmPass = confirmPassField.getText().toCharArray();
		if(!Arrays.equals(password, confirmPass)) {
			Utils.popUpDialog(root, rootPane, "Error","Password not match!");
			return false;
		}
		if(username.trim().length() < 5) {
			Utils.popUpDialog(root, rootPane, "Error","Username atleast 5 chars!");
			return false;
		}
		if(!Utils.isAlpha(username)) {
			Utils.popUpDialog(root, rootPane, "Error","Username is not valid!");
			return false;
		}
		if(password.length < 5) {
			Utils.popUpDialog(root, rootPane, "Error","Password atleast 5 chars!");
			return false;
		}
		if(Utils.charArrayContains(confirmPass, ' ')) {
			Utils.popUpDialog(root, rootPane, "Error","Password contains space!");
			return false;
		}
		Task<Boolean> resetPassTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				return DataBaseHandler.getInstance().runResetPassQuery(username, password);
			}
		};

		resetPassTask.setOnFailed(event -> {
			loading.setVisible(false);
			resetPassTask.getException().printStackTrace();
		});

		resetPassTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			if (resetPassTask.getValue()) {
				openRecoverCompleted(e1);
			} else {
				Utils.popUpDialog(root, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
			}
		});
		new Thread(resetPassTask).start();
		return true;
	}
	
	public void openRecoverCompleted(MouseEvent e1) {
		try {
			AnchorPane regCompleted = FXMLLoader.load(Utils.getFileURIByPath(Main.CONFIG, "regCompletedFXML").toURL());
			WindowStyle.setAnchorPaneConstraints(regCompleted, 50, 50, 275, 275);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
			Label msgLbl = (Label)regCompleted.lookup("#infoLbl");
			msgLbl.setText("Your password has been reset");
			backgroundLogin.getChildren().add(regCompleted);
			recoverPane.setVisible(false);
			new ZoomIn(regCompleted).play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initLangBindings() {
		LanguageBundle.labelForValue(backLabel, ()->LanguageBundle.get("backLabel", 0));
		LanguageBundle.passFieldForValue(passField, ()->LanguageBundle.get("newPassField", 0));
		LanguageBundle.passFieldForValue(confirmPassField, ()->LanguageBundle.get("confirmPassField", 0));
		LanguageBundle.textFieldForValue(usernameField, ()->LanguageBundle.get("usernameField", 0));
		LanguageBundle.buttonForValue(recoverBtn, ()->LanguageBundle.get("recoverBtn", 0));
	}
	
	public void initialize() {
		initLangBindings();
		loading.setVisible(false);
	}

}
