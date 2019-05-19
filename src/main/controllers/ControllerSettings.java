package main.controllers;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Config;
import main.utils.FXResizeHelper;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerSettings {
	@FXML
	private Pane pop;

	@FXML
	private JFXButton backButton;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane rootStack;

	@FXML
	private AnchorPane contentPane;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private ImageView backImage;

	private double prefHeight = 630, prefWidth = 800;

	public void initialize() {
		initTitleBox();
		backButton.setVisible(false);
		backButton.setOnMouseEntered(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "backHoverImagePath").toString();
			backImage.setImage(new Image(img));
		});
		backButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "backImagePath").toString();
			backImage.setImage(new Image(img));
		});
		new ZoomOut(pop).play();
	}

	@FXML
	public void backToSettings() {
		try {
			Parent fxml = FXMLLoader.load(Utils.getFileURIByPath(Main.CONFIG, "settingsFXML").toURL());
			Stage currentStage = (Stage) pop.getScene().getWindow();
			currentStage.setScene(new Scene(fxml));
			currentStage.setHeight(prefHeight);
			currentStage.setWidth(prefWidth);
			new FXResizeHelper(currentStage, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openOrarioSettimanale() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "orarioSettimanaleFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			title.setText("Settings - Orario Settimanale");
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "prefHeightOS"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "prefWidthOS"));
			stage.setHeight(Config.getDouble(Main.CONFIG, "prefHeightOS"));
			stage.setWidth(Config.getDouble(Main.CONFIG, "prefWidthOS"));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openDocenti(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "docentiFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			title.setText("Settings - Docenti");
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(700);
			stage.setMinWidth(700);
			stage.setHeight(700);
			stage.setWidth(700);
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void openAbout(MouseEvent event) {

	}
	
	@FXML
	void openMaterie(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "materieFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			title.setText("Settings - Materie");
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(700);
			stage.setMinWidth(700);
			stage.setHeight(700);
			stage.setWidth(700);
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openProfile(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "profiloFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			title.setText("Settings - Profilo");
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			stage.setHeight(600);
			stage.setWidth(600);
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*********** Custom Window title bar ************/
	@FXML
	private HBox titleHBox;

	@FXML
	private Label title;

	@FXML
	private JFXButton titleCloseButton;

	@FXML
	private JFXButton titleMaxmizeButton;

	@FXML
	private ImageView titleCloseImage;

	@FXML
	private ImageView titleMaxmizeImage;

	public void initTitleBox() {
		WindowStyle.stageDimension(prefWidth, prefHeight);
		titleCloseButton.setOnMouseEntered(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleMaxmizeImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) pop.getScene().getWindow());
		});
		titleMaxmizeButton.setOnMouseClicked(e -> {
			WindowStyle.MaxMinScreen((Stage) pop.getScene().getWindow());
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					WindowStyle.MaxMinScreen((Stage) pop.getScene().getWindow());
				}
			}
		});
	}
	/***********************************************/
}
