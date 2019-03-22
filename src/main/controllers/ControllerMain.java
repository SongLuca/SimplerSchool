package main.controllers;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import animatefx.animation.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.application.Main;
import main.application.customGUI.ConfirmDialog;
import main.application.models.Config;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.application.models.Utente;
import main.database.DataBaseHandler;
import main.utils.Console;
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
	private VBox oreHeader;

	@FXML
	private GridPane calendarGrid;

	@FXML
	private Circle avatar;

	@FXML
	private Label nomeLbl;

	@FXML
	private Label cognomeLbl;

	@FXML
	private Label scuolaLbl;

	@FXML
	private JFXTabPane tabPane;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private JFXButton profileButton;

	@FXML
	private JFXButton closeButton;

	@FXML
	private JFXButton lastWeekBtn;

	@FXML
	private JFXButton thisWeekBtn;

	@FXML
	private JFXButton nextWeekBtn;

	@FXML
	private TextInputDialog inputSubject;

	@FXML
	private JFXDatePicker datePicker;
	
	@FXML
	private JFXComboBox<String> orarioSPicker;
	
	private HashMap<Integer, OrarioSettimanale> orariS ;
	
	private OrarioSettimanale os;
	
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
		new Wobble(avatar).play();
	}
	
	public void initialize() {
		Console.print("Initializing menu gui", "gui");
		orariS = DataBaseHandler.getInstance().getOS();
		initTitleBox();
		initHamMenu();
		initTabPane();
		initCalendarWeekDayHeader(LocalDate.now(), false);
		initOrarioHeader();
		initCalendarGrid();
		initProfilePane();
		MetaData.cm = this;
	}
	
	public void initProfilePane() {
		Utente u = Main.utente;
		File avatarFile = new File(Config.getString("config", "databaseFolder") + "/" + u.getAvatar_path());
		avatar.setFill(new ImagePattern(new Image(avatarFile.toURI().toString())));
		nomeLbl.setText((u.getNome() == null) ? "null" : u.getNome());
		cognomeLbl.setText((u.getCognome() == null) ? "null" : u.getCognome());
		scuolaLbl.setText((u.getScuola() == null) ? "null" : u.getScuola());
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
		updateOSPicker();
		datePicker.setValue(LocalDate.now());
		datePicker.setOnAction(e -> {
			initCalendarWeekDayHeader(datePicker.getValue(), true);
		});
		
		lastWeekBtn.setOnAction(e->{
			LocalDate lastWeek = datePicker.getValue().minusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().minusWeeks(1), true);
			datePicker.setValue(lastWeek);
			Console.print("Jumping to last week " + lastWeek, "Gui");
		});
		
		thisWeekBtn.setOnAction(e->{
			LocalDate today = LocalDate.now();
			if(datePicker.getValue().compareTo(today) != 0) {
				initCalendarWeekDayHeader(today, true);
				datePicker.setValue(today);
				Console.print("Jumping to current week " + today, "Gui");
			}
		});
		
		nextWeekBtn.setOnAction(e->{
			LocalDate nextWeek = datePicker.getValue().plusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().plusWeeks(1), true);
			datePicker.setValue(nextWeek);
			Console.print("Jumping to next week " + nextWeek, "Gui");
		});
		
		orarioSPicker.setOnAction(e->{
			String selectedOS = orarioSPicker.getSelectionModel().getSelectedItem();
			if (selectedOS != null){
				Console.print(selectedOS+" selected", "Gui");
				os = getOSbyName(selectedOS);
				Config.userConfig.setProperty("selectedOrarioSettimanale", selectedOS);
				SimplerSchoolUtil.saveProperties(Config.userConfig, "userconfig", true);
				for (String giornoK : os.getSettimana().keySet()) {
					int dayCol = os.getColByGiorno(giornoK);
					fuseSubjects(calendarGrid, dayCol);
				}
			}
		});
		
		for (String giornoK : os.getSettimana().keySet()) {
			int dayCol = os.getColByGiorno(giornoK);
			fuseSubjects(calendarGrid, dayCol);
		}
	}
	
	public void updateOSPicker() {
		Console.print("Updating OS picker", "gui");
		orariS = DataBaseHandler.getInstance().getOS();
		String selectedOrariS = Config.getString("userconfig","selectedOrarioSettimanale");
		orarioSPicker.getItems().clear();
		for(int key : orariS.keySet()) {
			orarioSPicker.getItems().add(orariS.get(key).getNomeOrario());
			if(orariS.get(key).getNomeOrario().equals(selectedOrariS)) {
				orarioSPicker.getSelectionModel().select(selectedOrariS);
				os = getOSbyName(selectedOrariS);
			}
				
		}
		
	}
	
	public OrarioSettimanale getOSbyName(String nome) {
		for(int key : orariS.keySet()) {
			if(orariS.get(key).getNomeOrario().equals(nome))
				return orariS.get(key);
		}
		return null;
	}
	
	public Materia getMateriaByNome(String nome) {
		HashMap<Integer, Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for (int key : materie.keySet()) {
			if (materie.get(key).getNome().equals(nome))
				return materie.get(key);
		}
		return null;
	}
	
	public void addVBoxToCell(GridPane osGrid, String nomeMateria, int row, int col, int rowSpan) {
		Materia m = getMateriaByNome(nomeMateria);
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setStyle("-fx-background-color:" + m.getColore() + ";");
		Label lbl = new Label();
		lbl.setText(m.getNome());
		lbl.setId("#nomeM");
		pane.getChildren().add(lbl);
		if (rowSpan != 1)
			osGrid.add(pane, col, row, 1, rowSpan);
		else
			osGrid.add(pane, col, row);
	}

	public void fuseSubjects(GridPane osGrid, int col) {
		LinkedHashMap<String, String> giorno = os.getOrarioGiorno(col);
		osGrid.getChildren().removeIf(node -> (node instanceof VBox) && GridPane.getColumnIndex(node) == col);
		int count = 0;
		int length = 1;
		int startPos = 0;
		String materiaPrec = "";
		for (String ora : giorno.keySet()) {
			if (count == 0) {
				if (!giorno.get(ora).equals("null"))
					addVBoxToCell(osGrid, giorno.get(ora), startPos, col, length);
				else {
					VBox vPane = new VBox();
					osGrid.add(vPane, col, count);
				}
			}
			if (count != 0) {
				if (!giorno.get(ora).equals("null") && giorno.get(ora).equals(materiaPrec)) {
					length++;
					if (length == 1)
						startPos = count;
				} else {
					if (length != 1) {
						addVBoxToCell(osGrid, materiaPrec, startPos, col, length);
						if (giorno.get(ora).equals("null")) {
							VBox vPane = new VBox();
							osGrid.add(vPane, col, count);
						} else
							addVBoxToCell(osGrid, giorno.get(ora), count, col, 1);
					} else {
						length = 1;
						startPos = count;
						if (!giorno.get(ora).equals("null"))
							addVBoxToCell(osGrid, giorno.get(ora), startPos, col, length);
						else {
							VBox vPane = new VBox();
							osGrid.add(vPane, col, startPos);
						}
					}
					length = 1;
					startPos = count;
				}
			}
			materiaPrec = giorno.get(ora);
			count++;
		}
	}
	
	@FXML
	public void setOrarioSettimanale(ActionEvent a) {
		Console.print(orarioSPicker.getSelectionModel().getSelectedItem(), "");
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
		Console.print("Opening settings window", "gui");
		Stage settings = SimplerSchoolUtil.loadWindow("settingsFXML",
				(Stage) ((Node) event.getSource()).getScene().getWindow(), true, null, null);
		settings.setMinHeight(Config.getDouble("config", "minHeightSettings"));
		settings.setMinWidth(Config.getDouble("config", "minWidthSettings"));
		settings.setOnHiding(e -> {
			WindowStyle.stageDimension(prefWidth, prefHeight);
		});
	}

	@FXML
	public void openCloseWindow(MouseEvent event) {
		Console.print("Opening logout dialog", "gui");
		Stage owner = (Stage) rootPane.getScene().getWindow();
		ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure you want to log out?");
		if (cd.getResult()) {
			Console.print("User " + Main.utente.getUsername() + "[" + Main.utente.getUserid() + "]" + " has logged out",
					"App");
			WindowStyle.close(owner);
			SimplerSchoolUtil.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		}
	}

	public void initCalendarWeekDayHeader(LocalDate data, boolean clear) {
		if(clear)
			weekdayHeader.getChildren().clear();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-LLL");
		int weekdays = 7;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		for (int i = 0; i < weekdays; i++) {
			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getStyleClass().add("weekday-header");
			HBox.setHgrow(box, Priority.ALWAYS);
			box.setMaxWidth(Double.MAX_VALUE);
			box.setMinWidth(weekdayHeader.getPrefWidth() / weekdays);
			weekdayHeader.getChildren().add(box);
			box.getChildren().add(new Label(weekDays[i]));
			box.getChildren().add(new Label(data.with(DayOfWeek.of(i + 1)).format(dtf)));

		}
	}

	public void initOrarioHeader() {
		int ore = 11;
		for (int i = 1; i <= ore; i++) {
			StackPane pane = new StackPane();
			pane.getStyleClass().add("weekday-header");
			VBox.setVgrow(pane, Priority.ALWAYS);
			pane.setMaxHeight(Double.MAX_VALUE);
			pane.setMinHeight(oreHeader.getPrefHeight() / ore);
			oreHeader.getChildren().add(pane);
			pane.getChildren().add(new Label("" + i));
		}
	}

	public void initCalendarGrid() {
		int rows = 11;
		int cols = 7;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					
				});
				GridPane.setVgrow(vPane, Priority.ALWAYS);
				calendarGrid.add(vPane, j, i);
			}
		}
		for (int i = 0; i < rows; i++) {
			RowConstraints row = new RowConstraints();
			calendarGrid.getRowConstraints().add(row);
		}
		for (int i = 0; i < cols; i++) {
			ColumnConstraints col = new ColumnConstraints();
			calendarGrid.getColumnConstraints().add(col);
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
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleMaxmizeImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseEntered(e1 -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleHideHoverImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseExited(e1 -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleHideImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseClicked(e -> {
			WindowStyle.hidde((Stage) tabPane.getScene().getWindow());
		});
		titleMaxmizeButton.setOnMouseClicked(e -> {
			WindowStyle.MaxMinScreen((Stage) tabPane.getScene().getWindow());
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) tabPane.getScene().getWindow());
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					WindowStyle.MaxMinScreen((Stage) tabPane.getScene().getWindow());
				}
			}
		});
	}

	/***********************************************/
}
