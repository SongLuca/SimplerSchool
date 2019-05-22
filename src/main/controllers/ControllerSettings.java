package main.controllers;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerSettings {

	@FXML
	private AnchorPane rootPane, contentPane, mainPane;

	@FXML
	private StackPane rootStack;
	
	@FXML
	private Pane pop;

	@FXML
	private Label windowTitle, title;
	
	@FXML
	private Label materieBtnTitle, materieBtnInfoLbl;
	
	@FXML
	private Label osBtnTitle, osBtnInfoLbl;
	
	@FXML
	private Label profiloBtnTitle, profiloBtnInfoLbl;
	
	@FXML
	private Label docentiBtnTitle, docentiBtnInfoLbl;
	
	@FXML
	private Label configBtnTitle, configBtnInfoLbl;
	
	@FXML
	private Label aboutBtnTitle, aboutBtnInfoLbl;
	
	@FXML
	private JFXButton backButton;
	
	@FXML
	private ImageView backImage;

	private double prefHeight = 630, prefWidth = 800;

	public void initialize() {
		initTitleBox();
		initLangBindings();
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

	public void initLangBindings() {
		LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("settingsTitle", 0));
		LanguageBundle.labelForValue(title, ()->LanguageBundle.get("settingsTitle", 0));
		LanguageBundle.labelForValue(materieBtnTitle, ()->LanguageBundle.get("materieBtnTitle", 0));
		LanguageBundle.labelForValue(materieBtnInfoLbl, ()->LanguageBundle.get("materieBtnInfoLbl", 0));
		LanguageBundle.labelForValue(osBtnTitle, ()->LanguageBundle.get("osBtnTitle", 0));
		LanguageBundle.labelForValue(osBtnInfoLbl, ()->LanguageBundle.get("osBtnInfoLbl", 0));
		LanguageBundle.labelForValue(profiloBtnTitle, ()->LanguageBundle.get("profiloBtnTitle", 0));
		LanguageBundle.labelForValue(profiloBtnInfoLbl, ()->LanguageBundle.get("profiloBtnInfoLbl", 0));
		LanguageBundle.labelForValue(docentiBtnTitle, ()->LanguageBundle.get("docentiBtnTitle", 0));
		LanguageBundle.labelForValue(docentiBtnInfoLbl, ()->LanguageBundle.get("docentiBtnInfoLbl", 0));
		LanguageBundle.labelForValue(configBtnTitle, ()->LanguageBundle.get("configBtnTitle", 0));
		LanguageBundle.labelForValue(configBtnInfoLbl, ()->LanguageBundle.get("configBtnInfoLbl", 0));
		LanguageBundle.labelForValue(aboutBtnTitle, ()->LanguageBundle.get("aboutBtnTitle", 0));
		LanguageBundle.labelForValue(aboutBtnInfoLbl, ()->LanguageBundle.get("aboutBtnInfoLbl", 0));
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
			FXMLLoader fxmlLoader = new FXMLLoader(
					Utils.getFileURIByPath(Main.CONFIG, "orarioSettimanaleFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-Weekly_Schedules", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "prefHeightOS"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "prefWidthOS"));
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
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-Lecturers", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openAbout(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "aboutFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-About", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openMaterie(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "materieFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-Subjects", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
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
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-Profile", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
			WindowStyle.stageDimension(stage.getMinWidth(), stage.getMinHeight());
			new FadeInUp(contentPane).play();
			mainPane.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openConfiguration(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "configurationFXML").toURL());
			AnchorPane osPane = fxmlLoader.load();
			contentPane.getChildren().removeAll();
			contentPane.getChildren().setAll(osPane);
			LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get("Settings-Configuration", 0));
			backButton.setPrefWidth(40);
			backButton.setVisible(true);

			Stage stage = (Stage) pop.getScene().getWindow();
			stage.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
			stage.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
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
			rootPane.getScene().setCursor(Cursor.DEFAULT);
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
			rootPane.getScene().setCursor(Cursor.DEFAULT);
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
