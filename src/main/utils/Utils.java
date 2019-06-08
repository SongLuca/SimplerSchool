package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.swing.filechooser.FileSystemView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
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
import main.application.Main;
import main.application.models.Config;

public class Utils {
	private static String config = "/main/resources/config/config.properties";
	private static String appConfig;
	private static String databaseinfo;
	private static String userconfig;
	
	public static void readUserProperties() {
		String documentPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+
				"/Simpler_School/users/"+Main.utente.getUserid();
		File userFolder = new File(documentPath);
		Properties prop = new Properties();
		if(!userFolder.exists()) {
			Console.print("Documents user folder not found. Created new one", "config");
			userFolder.mkdirs();
		}
		try {
			userconfig = documentPath+"/userconfig.properties";
			File userCfg = new File(userconfig);
			if(!userCfg.exists()) {
				Console.print("userconfig.properties not found. Created new one", "config");
				userCfg.createNewFile();
				prop.setProperty("selectedOrarioSettimanale", "");
				prop.setProperty("votoMax", "10");
				prop.setProperty("votoMin", "0");
				prop.setProperty("compitiPerCasaNotifica", "true");
				prop.setProperty("verificaNotifica", "true");
				prop.setProperty("interrogazioneNotifica", "true");
				prop.setProperty("theme", "Theme1");
				FileOutputStream fos = new FileOutputStream(userCfg);
				prop.store(fos, null);
				fos.close();
			}
			FileInputStream fit = new FileInputStream(userCfg);
			prop.load(fit);
			fit.close();
			Config.userConfig = prop;
			Console.print(Main.utente.getUserid()+ " userconfig loaded", "config");
		} catch (Exception e) {
			Console.print("Exception: " + e, "exception");
		}
	}
	
	public static void saveUserProperties(boolean reload) {
		FileOutputStream fos;
		Properties p = Config.userConfig;
		try {
			fos = new FileOutputStream(userconfig);
			p.store(fos, null);
			fos.close();
			Console.print("user config saved", "config");
			if (reload) {
				readUserProperties();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readAppProperties() {
		try {
			Properties prop = new Properties();
			appConfig = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile()
					+"/config/appconfig.properties";
			File f = new File(appConfig);
			File cfgFolder = new File(f.getParentFile().getAbsolutePath());
			if(!cfgFolder.exists()) {
				cfgFolder.mkdirs();
			}
			if(!f.exists()) {
				f.createNewFile();
				prop.setProperty("selectedLanguage", "Italiano");
				prop.setProperty("rememberedUser", "");
				prop.setProperty("rememberMe", "false");
				FileOutputStream fos = new FileOutputStream(f);
				prop.store(fos, null);
				fos.close();
			}
			else {
				InputStream fit = new FileInputStream(f.getAbsolutePath());
				prop.load(fit);
				fit.close();
			}
			Config.appConfig = prop;
			Console.print("app config loaded", "config");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveAppProperties(boolean reload) {
		FileOutputStream fos;
		Properties p = Config.appConfig;
		try {
			fos = new FileOutputStream(appConfig);
			p.store(fos, null);
			fos.close();
			Console.print("app config saved", "config");
			if (reload) {
				readAppProperties();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readDBInfoProperties() {
		try {
			Properties prop = new Properties();
			databaseinfo = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile()
					+"/config/databaseinfo.properties";
			File f = new File(databaseinfo);
			File cfgFolder = new File(f.getParentFile().getAbsolutePath());
			if(!cfgFolder.exists()) {
				cfgFolder.mkdirs();
			}
			if(!f.exists()) {
				f.createNewFile();
				prop.setProperty("usernamesql", "admin");
				prop.setProperty("databasehost", "jdbc:mysql://localhost/simpler_school");
				prop.setProperty("passwordsql", "8RvrTMUSW2vNvxH");
				prop.setProperty("databaseFolder", "F:/Software/xampp/htdocs/Simpler_School");
				FileOutputStream fos = new FileOutputStream(f);
				prop.store(fos, null);
				fos.close();
			}
			else {
				InputStream fit = new FileInputStream(f.getAbsolutePath());
				prop.load(fit);
				fit.close();
			}
			Config.databaseinfo = prop;
			Console.print("databaseinfo config loaded", "config");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDBInfoProperties(boolean reload) {
		FileOutputStream fos;
		Properties p = Config.databaseinfo;
		try {
			fos = new FileOutputStream(databaseinfo);
			p.store(fos, null);
			fos.close();
			Console.print("daatabaseinfo config saved", "config");
			if (reload) {
				readDBInfoProperties();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readConfigProperties() {
		Properties prop = new Properties();
		try {
			InputStream fit = Utils.class.getResourceAsStream(config);
			prop.load(fit);
			fit.close();
			Config.config = prop;
			Console.print("config loaded", "config");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Object loadWindow(String fxmlProp, Stage primaryStage, boolean resizable, String appIconPath,
			String title) {
		Stage stage = null;
		FXMLLoader fxmlLoader = null;
		try {
			//URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			fxmlLoader = new FXMLLoader(Utils.class.getResource(Config.getString(Main.CONFIG, fxmlProp)));
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
				stage.getIcons().add(
						new Image(Utils.class.getResource(Config.getString(Main.CONFIG, "appIconPath")).toExternalForm()));
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
		//	URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(Config.getString(Main.CONFIG, fxmlProp)));
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
				stage.getIcons().add(
						new Image(Utils.class.getResource(Config.getString(Main.CONFIG, "appIconPath")).toExternalForm()));
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
		//	URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(Config.getString(Main.CONFIG, fxmlProp)));
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
				stage.getIcons().add(
						new Image(Utils.class.getResource(Config.getString(Main.CONFIG, "appIconPath")).toExternalForm()));
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
		//	URL fxmlURL = new File(Config.getString(Main.CONFIG, fxmlProp)).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(Config.getString(Main.CONFIG, fxmlProp)));
			Parent root = fxmlLoader.load();
			stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			String cssUrl = Utils.class.getResource("/main/resources/gui/css/"+Preferences.theme+".css").toExternalForm();
			Scene scene = new Scene(root);
			((AnchorPane) fxmlLoader.getNamespace().get("rootAnchor")).getStylesheets().add(cssUrl);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			stage.setMinWidth(minW);
			stage.setMinHeight(minH);
			stage.setWidth(minW);
			stage.setHeight(minH);
		//	HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titlebar");
		//	WindowStyle.allowDrag(titleBox, stage);
			if (resizable)
				new FXResizeHelper(stage, 10, 3);
				//ResizeHelper.addResizeListener(stage);
			if (appIconPath != null)
				stage.getIcons().add(
						new Image(Utils.class.getResource(Config.getString(Main.CONFIG, "appIconPath")).toExternalForm()));
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
		JFXButton button = new JFXButton(LanguageBundle.get("close"));
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

/*	public static void makeText(AnchorPane pane, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
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
	}*/

	public static boolean charArrayContains(char[] chars, char letter) {
		for (char x : chars) {
			if (x == letter) {
				return true;
			}
		}
		return false;
	}

	public static Background imgToBackground(String imgProp) {
		Image image = new Image(Utils.class.getResource(Config.getString(Main.CONFIG, imgProp)).getFile());
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
