package main.controllers;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.application.Main;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerCustomStage {
	@FXML
    private AnchorPane root, content;
	
	@FXML
    private Label title;

	public void initialize() {
		initTitleBox();
	}
	
	
	/*********** Custom Window title bar ************/
	@FXML
	private HBox titleHBox;

	@FXML
	private JFXButton titleCloseButton;

	@FXML
	private ImageView titleCloseImage;

	public void initTitleBox() {
		titleCloseButton.setOnMouseEntered(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
			root.getScene().setCursor(Cursor.DEFAULT);
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) root.getScene().getWindow());
		});
	}
	/***********************************************/
}
