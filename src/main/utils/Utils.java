package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.application.Main;
import main.application.models.Config;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.application.models.SchoolTask;

public class Utils {
	private static String config = "src/main/resources/config/config.properties";
	private static String userConfig = "src/main/resources/config/userconfig.properties";
	private static String databaseinfo = "src/main/resources/config/databaseinfo.properties";
	
	public static Properties readProperties(String configName) {
		Properties prop = new Properties();
		String configPath = "";
		switch (configName) {
		case "config":
			configPath = config;
			break;
		case "userconfig":
			configPath = userConfig;
			break;
		case "databaseinfo":
			configPath = databaseinfo;
			Console.print("read db", "");
			break;
		default:
			Console.print(configName + " - no such properties file found", "config");
			break;
		}
		try {
			FileInputStream fit = new FileInputStream(configPath);
			prop.load(fit);
			fit.close();
			Console.print(configName + " loaded", "config");
		} catch (Exception e) {
			Console.print("Exception: " + e, "exception");
		}
		return prop;
	}

	public static void saveProperties(Properties p, String configName, boolean reload) {
		FileOutputStream fos;
		String configPath = "";
		switch (configName) {
		case "config":
			configPath = config;
			break;
		case "userconfig":
			configPath = userConfig;
			break;
		case "databaseinfo":
			configPath = databaseinfo;
			break;
		default:
			Console.print(configName + " - no such properties file found", "config");
			break;
		}
		try {
			fos = new FileOutputStream(configPath);
			p.store(fos, null);
			fos.close();
			Console.print(configName + " saved", "config");
			if (reload) {
				readProperties(configName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object loadWindow(String fxmlProp, Stage primaryStage, boolean resizable, String appIconPath,
			String title) {
		Stage stage = null;
		FXMLLoader fxmlLoader = null;
		try {
			URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = fxmlLoader.load();
			stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titleHBox");
			WindowStyle.allowDrag(titleBox, stage);
			if (primaryStage != null)
				stage.initOwner(primaryStage);
			if (resizable)
				new FXResizeHelper(stage, 5, 5);
			if (appIconPath != null)
				stage.getIcons().add(new Image(new File(Config.getString("config", "appIconPath")).toURI().toString()));
			if (title != null)
				stage.setTitle(title);
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fxmlLoader.getController();
	}

	public static Stage loadWindowS(String fxmlProp, Stage primaryStage, boolean resizable, String appIconPath,
			String title) {
		Stage stage = null;
		try {
			URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = fxmlLoader.load();
			stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titleHBox");
			WindowStyle.allowDrag(titleBox, stage);
			if (primaryStage != null)
				stage.initOwner(primaryStage);
			if (resizable)
				new FXResizeHelper(stage, 5, 5);
			if (appIconPath != null)
				stage.getIcons().add(new Image(new File(Config.getString("config", "appIconPath")).toURI().toString()));
			if (title != null)
				stage.setTitle(title);
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stage;
	}

	public static Stage loadNoTitleWindow(String fxmlProp, Stage primaryStage, boolean resizable, String appIconPath,
			String title) {
		Stage stage = null;
		try {
			URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = fxmlLoader.load();
			stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(root));
			if (primaryStage != null)
				stage.initOwner(primaryStage);
			if (resizable)
				new FXResizeHelper(stage, 5, 5);
			if (appIconPath != null)
				stage.getIcons().add(new Image(new File(Config.getString("config", "appIconPath")).toURI().toString()));
			if (title != null)
				stage.setTitle(title);
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stage;
	}

	public static Stage newWindow(String fxmlProp, boolean resizable, String appIconPath, String title, double minW,
			double minH) {
		Stage stage = null;
		try {
			URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Parent root = fxmlLoader.load();
			stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			stage.setMinWidth(minW);
			stage.setMinHeight(minH);
			stage.setWidth(minW);
			stage.setHeight(minH);
			HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titleHBox");
			WindowStyle.allowDrag(titleBox, stage);
			if (resizable)
				new FXResizeHelper(stage, 5, 5);
			if (appIconPath != null)
				stage.getIcons().add(new Image(new File(Config.getString("config", "appIconPath")).toURI().toString()));
			if (title != null)
				stage.setTitle(title);
			stage.show();
			root.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stage;
	}

	public static VBox getCellByPos(GridPane os, int row, int col) {
		for (Node node : os.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				return (VBox) node;
			}
		}
		return null;
	}

	public static boolean isAlpha(String name) {
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	public static void errorMsg(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		// alert.setTitle(titleBar);
		// alert.setHeaderText(headerMessage);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public static void confirmMsg(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		// alert.setTitle(titleBar);
		// alert.setHeaderText(headerMessage);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public static void popUpDialog(StackPane root, AnchorPane pane, String header, String body) {
		root.setVisible(true);
		BoxBlur bb = new BoxBlur(3, 3, 3);
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(header));
		content.setBody(new Text(body));
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.TOP);
		JFXButton button = new JFXButton("Close");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
			}
		});
		dialog.setOnDialogClosed(e -> {
			pane.setEffect(null);
			root.setVisible(false);
		});
		content.setActions(button);
		dialog.show();
		pane.setEffect(bb);
	}

	public static Popup createPopup(final String message) {
		final Popup popup = new Popup();
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		Label label = new Label(message);
		label.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				popup.hide();
			}
		});
		label.getStylesheets().add("/css/styles.css");
		label.getStyleClass().add("popup");
		popup.getContent().add(label);
		return popup;
	}

	public static void showPopupMessage(final String message, final Stage stage) {
		final Popup popup = createPopup(message);

		popup.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
				popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
			}
		});
		popup.show(stage);
	}

	public static void makeText(AnchorPane pane, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
		Label text = new Label(toastMsg);
		StackPane root = new StackPane(text);
		ImageView iv = new ImageView(new Image(Utils.getFileURIByPath(Main.CONFIG, "doneImagePath").toString()));
		iv.setFitWidth(15);
		iv.setFitHeight(15);
		text.setGraphic(iv);
		text.setContentDisplay(ContentDisplay.RIGHT);
		root.setStyle("-fx-background-color: white;");
		root.setOpacity(0);
		
		pane.getChildren().add(root);
		AnchorPane.setTopAnchor(root, 0.0);
		AnchorPane.setRightAnchor(root, 0.0);

		Timeline fadeInTimeline = new Timeline();
		KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay),
				new KeyValue(root.opacityProperty(), 1));
		fadeInTimeline.getKeyFrames().add(fadeInKey1);
		fadeInTimeline.setOnFinished((ae) -> {
			new Thread(() -> {
				try {
					Thread.sleep(toastDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Timeline fadeOutTimeline = new Timeline();
				KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay),
						new KeyValue(root.opacityProperty(), 0));
				fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
				fadeOutTimeline.setOnFinished((aeb) -> pane.getChildren().remove(pane));
				fadeOutTimeline.play();
			}).start();
		});
		fadeInTimeline.play();
	}

	/*
	 * Questo metodo serve per controllare e validare le attivita' se la materia di
	 * un'attivita' viene spostata oppure rimossa nell'orario settimanale tale
	 * attivita' verra considerata invalida e quindi viene rimossa dal database
	 */
	public static boolean checkInvalidTasks(ArrayList<SchoolTask> attivita) {
		OrarioSettimanale os = MetaData.os;
		os.toString();
		return false;
	}

	public static boolean charArrayContains(char[] chars, char letter) {
		for (char x : chars) {
			if (x == letter) {
				return true;
			}
		}
		return false;
	}

	public static URI getFileURIByPath(String configName, String path) {
		return new File(Config.getString(configName, path)).toURI();
	}

	public static Background imgToBackground(String imgProp) {
		Image image = new Image(getFileURIByPath(Main.CONFIG, imgProp).toString());
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		return new Background(backgroundImage);
	}

	public static String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	public static String numToDay(int n) {
		switch (n) {
		case 0:
			return "lunedi";
		case 1:
			return "martedi";
		case 2:
			return "mercoledi";
		case 3:
			return "giovedi";
		case 4:
			return "venerdi";
		case 5:
			return "sabato";
		case 6:
			return "domenica";
		}
		return null;
	}
}
