package main.controllers;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import main.application.Main;
import main.application.models.MetaData;
import main.application.models.Utente;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerProfile {
	@FXML
	private AnchorPane subContentPane;

	@FXML
	private JFXTextField nomeField, cognomeField, scuolField;

	@FXML
	private Circle avatar;

	@FXML
	private JFXButton editAvatarBtn, saveBtn, closeBtn;

	@FXML
	private JFXSpinner loading;

	private Utente user;

	private File avatarFile;

	public void initialize() {
		Console.print("Opening profile window", "gui");
		this.user = Main.utente;
		editAvatarBtn.setOnAction(e -> selectAvatar());
		initComponents();
		initLangBindings();
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
	}

	public void initComponents() {
		File currAvatar = DataBaseHandler.getInstance().getAvatarFile(user);
		avatar.setFill(new ImagePattern(new Image(currAvatar.toURI().toString())));
		if (user.getNome() != null)
			nomeField.setText(user.getNome());
		if (user.getCognome() != null)
			cognomeField.setText(user.getCognome());
		if (user.getScuola() != null)
			scuolField.setText(user.getScuola());
	}

	public void initLangBindings() {
		saveBtn.setText(LanguageBundle.get("saveBtn"));
		editAvatarBtn.setText(LanguageBundle.get("edit"));
		closeBtn.setText(LanguageBundle.get("close"));
    }
	
	@FXML
	void save(MouseEvent event) {
		Utente newUser = new Utente(user.getUserid(), user.getUsername());
		String app = nomeField.getText();
		newUser.setNome((app.trim().equals("")) ? null : app);

		app = cognomeField.getText();
		newUser.setCognome((app.trim().equals("")) ? null : app);

		app = scuolField.getText();
		newUser.setScuola((app.trim().equals("")) ? null : app);

		newUser.setAvatar_path((avatarFile != null) ? 
				"users/" + newUser.getUserid() + "/" + avatarFile.getName() : user.getAvatar_path());

		if (!user.equals(newUser)) {
			updateUserTask(newUser);
		}
			
	}
	
	@FXML
	public void close() {
		WindowStyle.close((Stage) subContentPane.getScene().getWindow());
	}
	
	public void updateUserTask(Utente u) {
		StackPane root = (StackPane) subContentPane.getScene().lookup("#dialogStack");
		Task<Boolean> updatTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				subContentPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().updateUtenteQuery(u, null);
			}
		};

		updatTask.setOnFailed(event1 -> {
			loading.setVisible(false);
			subContentPane.setDisable(false);
			updatTask.getException().printStackTrace();
		});

		updatTask.setOnSucceeded(event1 -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			if (updatTask.getValue()) {
				Utils.popUpDialog(root, subContentPane, LanguageBundle.get("message"),LanguageBundle.get("changesSaved"));
				Main.utente = u;
				MetaData.cm.initProfilePane();
			} else {
				Utils.popUpDialog(root, subContentPane, "Error", DataBaseHandler.getInstance().getMsg());
				subContentPane.setDisable(false);
			}
		});
		new Thread(updatTask).start();
	}

	public void selectAvatar() {
		Stage stage = (Stage) subContentPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			avatarFile = selectedFile;
			avatar.setFill(new ImagePattern(new Image(selectedFile.toURI().toString())));
		}
	}

}
