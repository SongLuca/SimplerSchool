package main.controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import animatefx.animation.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.utils.PropertyParse;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerMain {
	private final int HAMMENUSIZE = 230;
	@FXML
	private StackPane rootStack;
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private AnchorPane menuPane;
	
	@FXML
	private Pane menuShadowPane;
	
	@FXML
	private Pane gridShadowPane;
	
	@FXML
	private VBox menuVBox;
	
	@FXML
	private JFXHamburger hamMenu;
	
	@FXML
	private HBox weekdayHeader;
	
	@FXML
	private GridPane calendarGrid;
	
	@FXML
	private ImageView profilePaneAvatar;
	
	@FXML
	private Circle cccc;
	
	@FXML
	private JFXTabPane tabPane;
	
	@FXML
	private JFXButton settingsButton;
	
	@FXML
	private JFXButton profileButton;
	
	@FXML
	private JFXButton closeButton;
	
	@FXML
	private ImageView image;

	@FXML
	private TextInputDialog inputSubject;
	
	private double prefHeight = 700, prefWidth = 1200;
	
	@FXML
	public void hamclicked(MouseEvent event) {
		if (menuPane.getPrefWidth() == 300) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth() - HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0, 0, 0, 0));

			menuVBox.setPrefSize(menuVBox.getPrefWidth() - HAMMENUSIZE, menuVBox.getPrefHeight());

			settingsButton.setPrefSize(settingsButton.getPrefWidth() - HAMMENUSIZE, settingsButton.getPrefHeight());
			settingsButton.setText("");

			profileButton.setPrefSize(profileButton.getPrefWidth() - HAMMENUSIZE, profileButton.getPrefHeight());
			profileButton.setText("");

			closeButton.setPrefSize(closeButton.getPrefWidth() - HAMMENUSIZE, closeButton.getPrefHeight());
			closeButton.setText("");

			hamMenuAnimation(menuPane, menuPane.getPrefWidth() - HAMMENUSIZE);
			hamMenuAnimation(menuShadowPane, menuShadowPane.getPrefWidth() - HAMMENUSIZE);
		} else if (menuPane.getPrefWidth() == 70) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth() + HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0, HAMMENUSIZE, 0, 0));

			menuVBox.setPrefSize(menuVBox.getPrefWidth() + HAMMENUSIZE, menuVBox.getPrefHeight());

			settingsButton.setPrefSize(settingsButton.getPrefWidth() + HAMMENUSIZE, settingsButton.getPrefHeight());
			settingsButton.setText("Settings");

			profileButton.setPrefSize(profileButton.getPrefWidth() + HAMMENUSIZE, profileButton.getPrefHeight());
			profileButton.setText("Profile");

			closeButton.setPrefSize(closeButton.getPrefWidth() + HAMMENUSIZE, closeButton.getPrefHeight());
			closeButton.setText("Log out");

			hamMenuAnimation(menuPane, menuPane.getPrefWidth() + HAMMENUSIZE);
			hamMenuAnimation(menuShadowPane, menuShadowPane.getPrefWidth() + HAMMENUSIZE);
		}
		new Wobble(cccc).play();
	}

	public void initialize() {
		System.out.println("init menu gui");
		initTitleBox();
		initHamMenu();
		SimplerSchoolUtil.initCalendarWeekDayHeader(weekdayHeader);
		SimplerSchoolUtil.initCalendarGrid(weekdayHeader,calendarGrid);
		initProfilePane();
	}

	public void initProfilePane() {
		cccc.setFill(new ImagePattern(profilePaneAvatar.getImage()));
	}
	
	public void initHamMenu() {
		HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamMenu);
		transition.setRate(-1);
		hamMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();
		});
		VBox.setVgrow(menuPane, Priority.ALWAYS);
	}

	public void initTabPane() {

	}

	public void hamMenuAnimation(Pane pane, double width) {
		hamMenu.setDisable(true);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
				new KeyValue(pane.prefWidthProperty(), width, Interpolator.EASE_BOTH)));
		timeline.play();
		timeline.setOnFinished(event -> {
			hamMenu.setDisable(false);
		});

	}
	
	@FXML
	public void openSettingsWindow(MouseEvent event) {
		System.out.println("opening settings window");
		Stage settings = SimplerSchoolUtil.loadWindow("settingsFXML",
				(Stage)((Node)event.getSource()).getScene().getWindow(), true, null, null);
		settings.setMinHeight(PropertyParse.getInt("minHeightSettings"));
		settings.setMinWidth(PropertyParse.getInt("minWidthSettings"));
	}
	
	@FXML
	public void openCloseWindow(MouseEvent event) {
		System.out.println("opening close window");
		Stage close = SimplerSchoolUtil.loadWindow("closeFXML",
				(Stage)((Node)event.getSource()).getScene().getWindow(), true, null, null);
		close.setMinHeight(PropertyParse.getInt("minHeightSettings"));
		close.setMinWidth(PropertyParse.getInt("minWidthSettings"));
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
	private JFXButton titleHideButton;
	
	@FXML
	private ImageView titleCloseImage;
	
	@FXML
	private ImageView titleMaxmizeImage;
	
	@FXML
	private ImageView titleHideImage;
	
	public void initTitleBox() {
		WindowStyle.stageDimension(prefWidth, prefHeight);
		titleCloseButton.setOnMouseEntered(e -> {
			String img = SimplerSchoolUtil.getFileURI("titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURI("titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("titleMaxmizeImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseEntered(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("titleHideHoverImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseExited(e1 -> {
			String img = SimplerSchoolUtil.getFileURI("titleHideImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseClicked(e -> {
			WindowStyle.hidde((Stage) tabPane.getScene().getWindow());
		});
		titleMaxmizeButton.setOnMouseClicked(e ->{
			WindowStyle. MaxMinScreen((Stage) tabPane.getScene().getWindow());
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) tabPane.getScene().getWindow());
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					WindowStyle. MaxMinScreen((Stage) tabPane.getScene().getWindow());
				}
			}
		});
	}
	
	/***********************************************/
}
