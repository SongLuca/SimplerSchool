package main.controllers.login;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Config;
import main.application.models.Utente;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.Utils;
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
	private JFXSpinner loading;

	@FXML
	private JFXButton selectAvatarButton;
	
	private String avatarPath;
	
	@FXML
	void animation(MouseEvent event) {
		// new FadeOutRight(loginPane).play();
	}

	@FXML
	void openLogin(MouseEvent e1) {
		try {
			AnchorPane login = FXMLLoader.load(Utils.getFileURIByPath(Main.CONFIG, "loginFXML").toURL());
			WindowStyle.setAnchorPaneConstraints(login, 50, 50, 275, 275);
			login.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
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
		StackPane root = (StackPane) ((Node) event.getSource()).getScene().lookup("#rootStack");
		AnchorPane rootPane = (AnchorPane) ((Node) event.getSource()).getScene().lookup("#rootPane");
		String username = usernameField.getText();
		char[] password = passField.getText().toCharArray(), confirmpw = confirmPassField.getText().toCharArray();
		if (username.length() > 15) {
			Utils.popUpDialog(root, rootPane, "Error", "Username must be less than 15 chars!");
			return false;
		}
		if (username.trim().length() < 5) {
			Utils.popUpDialog(root, rootPane, "Error", "Username atleast 5 chars!");
			return false;
		}
		if (!Utils.isAlpha(username)) {
			Utils.popUpDialog(root, rootPane, "Error", "Username is not valid!");
			return false;
		}
		if (Utils.charArrayContains(confirmpw, ' ')) {
			Utils.popUpDialog(root, rootPane, "Error", "Password contains space!");
			return false;
		}
		if (confirmpw.length < 5) {
			Utils.popUpDialog(root, rootPane, "Error", "Password atleast 5 chars!");
			return false;
		}
		if (!Arrays.equals(password, confirmpw)) {
			Utils.popUpDialog(root, rootPane, "Error", "Password not match!");
			return false;
		}
		
		Utente u = new Utente();
		u.setUsername(username);
		u.setAvatar_path(avatarPath);
		
		Task<Boolean> registerTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				registerPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().runRegisterValidateQuery(u, password);
			}
		};

		registerTask.setOnFailed(event1 -> {
			loading.setVisible(false);
			registerPane.setDisable(false);
			registerTask.getException().printStackTrace();
		});

		registerTask.setOnSucceeded(event1 -> {
			loading.setVisible(false);
			registerPane.setEffect(null);
			if (registerTask.getValue()) {
				Console.print(u.getUsername(), "");
				Config.userConfig.setProperty(u.getUsername()+"-selectedOrarioSettimanale", "");
				Utils.saveProperties(Config.userConfig, "userconfig", true);
				openRegCompleted(event);
			} else {
				Utils.popUpDialog(root, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				registerPane.setDisable(false);
			}
		});
		new Thread(registerTask).start();

		return false;
	}

	public void openRegCompleted(ActionEvent e1) {
		try {
			AnchorPane regCompleted = FXMLLoader
					.load(Utils.getFileURIByPath(Main.CONFIG, "regCompletedFXML").toURL());
			WindowStyle.setAnchorPaneConstraints(regCompleted, 50, 50, 275, 275);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
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
		Stage stage = (Stage) registerPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			avatarPath = selectedFile.getAbsolutePath().replace("\\", "/");
			profileImage.setFill(
					new ImagePattern(new Image(selectedFile.toURI().toString())));
		}
	}
	
	
	
	public void initialize() {
		loading.setVisible(false);
		selectAvatarButton.setOnMouseClicked(e -> {
			selectAvatar();
		});
		avatarPath = "default/defaultAvatar.jpg";
		profileImage.setFill(
				new ImagePattern(new Image(Utils.getFileURIByPath(Main.CONFIG, "defaultAvatar").toString())));
	}

	/***********************************************/
}
