package main.controllers;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import main.application.models.SchoolTask;
import main.application.models.Utente;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.Utils;
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

	@FXML
	private JFXTextArea noteBoard;
	
	@FXML
	private JFXSpinner loading;

	private HashMap<Integer, OrarioSettimanale> orariS;

	private OrarioSettimanale os;

	private double prefHeight = 800, prefWidth = 1400;

	@FXML
	public void hamclicked(MouseEvent event) {

		if (menuPane.getPrefWidth() == 300) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth() - HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0, 0, 0, 0));

			menuVBox.setPrefSize(menuVBox.getPrefWidth() - HAMMENUSIZE, menuVBox.getPrefHeight());
			settingsButton.setPrefSize(settingsButton.getPrefWidth() - HAMMENUSIZE, settingsButton.getPrefHeight());
			profileButton.setPrefSize(profileButton.getPrefWidth() - HAMMENUSIZE, profileButton.getPrefHeight());
			closeButton.setPrefSize(closeButton.getPrefWidth() - HAMMENUSIZE, closeButton.getPrefHeight());

			hamMenuAnimation(menuPane, menuPane.getPrefWidth() - HAMMENUSIZE, false);
			hamMenuAnimation(menuShadowPane, menuShadowPane.getPrefWidth() - HAMMENUSIZE, false);
		} else if (menuPane.getPrefWidth() == 70) {
			hamMenu.setPrefSize(hamMenu.getPrefWidth() + HAMMENUSIZE, hamMenu.getPrefHeight());
			hamMenu.setPadding(new Insets(0, HAMMENUSIZE, 0, 0));

			menuVBox.setPrefSize(menuVBox.getPrefWidth() + HAMMENUSIZE, menuVBox.getPrefHeight());
			settingsButton.setPrefSize(settingsButton.getPrefWidth() + HAMMENUSIZE, settingsButton.getPrefHeight());
			profileButton.setPrefSize(profileButton.getPrefWidth() + HAMMENUSIZE, profileButton.getPrefHeight());
			closeButton.setPrefSize(closeButton.getPrefWidth() + HAMMENUSIZE, closeButton.getPrefHeight());

			hamMenuAnimation(menuPane, menuPane.getPrefWidth() + HAMMENUSIZE, true);
			hamMenuAnimation(menuShadowPane, menuShadowPane.getPrefWidth() + HAMMENUSIZE, true);
		}
		// new Wobble(avatar).play();
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
		loadNoteBoard();
		MetaData.cm = this;
	}

	public void loadNoteBoard() {
		Console.print("Loading note board", "gui");
		noteBoard.clear();
		ArrayList<SchoolTask> attivita = DataBaseHandler.getInstance().getAttivita();
		if (attivita == null || attivita.size() == 0) {
			noteBoard.setText("Nessuna attivita in questa settimana");
		} else {
			noteBoard.setText(attivita.size() + " attivita:\n");
			String verifica = "Verifica:\n";
			String compito = "Compito:\n";
			String interrogazione = "Interrogazione:\n";
			for (SchoolTask task : attivita) {
				if (task.getTipo().equalsIgnoreCase("Verifica")) {
					verifica += "\tMateria: " + task.getMateriaNome()+"\n";
					verifica += "\tData: " + task.getData()+"\n";
					if(task.getComment().length() != 0)
						verifica += "\tCommento: " + task.getComment()+"\n";
					else {
						verifica += "\tCommento: nessun commento\n";
					}
					verifica += "\t-----------------\n";
				}
				if (task.getTipo().equalsIgnoreCase("Compito")) {
					compito += "\tMateria: " + task.getMateriaNome()+"\n";
					compito += "\tData: " + task.getData()+"\n";
					if(task.getComment().length() != 0)
						compito += "\tCommento: " + task.getComment()+"\n";
					else {
						compito += "\tCommento: nessun commento\n";
					}
					compito += "\t-----------------\n";
				}

				if (task.getTipo().equalsIgnoreCase("interrogazione")) {
					interrogazione += "\tMateria: " + task.getMateriaNome()+"\n";
					interrogazione += "\tData: " + task.getData()+"\n";	
					if(task.getComment().length() != 0)
						interrogazione += "\tCommento: " + task.getComment()+"\n";
					else {
						interrogazione += "\tCommento: nessun commento\n";
					}
					interrogazione += "\t-----------------\n";
				}
				
			}

			if (verifica.equals("Verifica:\n")) {
				verifica = "Nessuna verifica\n";
			}

			if (compito.equals("Compito:\n")) {
				compito = "Nessun compito\n";
			}
			if (interrogazione.equals("Interrogazione:\n")) {
				interrogazione = "Nessuna interrogazione\n";
			}
		
			noteBoard.appendText(verifica);
			noteBoard.appendText(compito);
			noteBoard.appendText(interrogazione);
		}
		noteBoard.positionCaret(0);
	}

	@FXML
	public void insertTask(MouseEvent e) {
		Console.print("Opening insert window", "gui");
		ControllerInsertTask cit = (ControllerInsertTask)Utils.loadWindow("insertTaskFXML", 
				(Stage) ((Node) e.getSource()).getScene().getWindow(), false, null, null);
		cit.setTitle("Inserimento attivita");
		cit.setMode("insert");
	}

	public void initProfilePane() {
		Utente u = Main.utente;
		DataBaseHandler.getInstance().getAvatarFile(u);
		File avatarFile = DataBaseHandler.getInstance().getAvatarFile(u);
		if (!avatarFile.exists())
			Console.print("Error!!! Profile avatar file not found", "fileio");
		else {
			Image avatarImage = new Image(avatarFile.toURI().toString());
			avatar.setFill(new ImagePattern(avatarImage));
		}
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

		lastWeekBtn.setOnAction(e -> {
			LocalDate lastWeek = datePicker.getValue().minusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().minusWeeks(1), true);
			datePicker.setValue(lastWeek);
			Console.print("Jumping to the last week " + lastWeek, "Gui");
			changeWeek(datePicker.getValue());
		});

		thisWeekBtn.setOnAction(e -> {
			LocalDate today = LocalDate.now();
			if (datePicker.getValue().compareTo(today) != 0) {
				initCalendarWeekDayHeader(today, true);
				datePicker.setValue(today);
				Console.print("Jumping to current week " + today, "Gui");
				changeWeek(datePicker.getValue());
				
			}
		});

		nextWeekBtn.setOnAction(e -> {
			LocalDate nextWeek = datePicker.getValue().plusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().plusWeeks(1), true);
			datePicker.setValue(nextWeek);
			Console.print("Jumping to the next week " + nextWeek, "Gui");
			changeWeek(datePicker.getValue());
		});

		orarioSPicker.setOnAction(e -> {
			String selectedOS = orarioSPicker.getSelectionModel().getSelectedItem();
			if (selectedOS != null) {
				Console.print(selectedOS + " selected", "Gui");
				os = getOSbyName(selectedOS);
				Config.userConfig.setProperty(Main.utente.getUsername()+"-selectedOrarioSettimanale", selectedOS);
				Utils.saveProperties(Config.userConfig, "userconfig", true);
				for (String giornoK : os.getSettimana().keySet()) {
					int dayCol = os.getColByGiorno(giornoK);
					fuseSubjects(calendarGrid, dayCol);
				}
			}
		});

		if (os != null) {
			for (String giornoK : os.getSettimana().keySet()) {
				int dayCol = os.getColByGiorno(giornoK);
				fuseSubjects(calendarGrid, dayCol);
			}
		}

	}
	
	public void changeWeek(LocalDate data) {
		Task<Boolean> changeWeekTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				rootPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().getAttivitaS(data);
			}
		};

		changeWeekTask.setOnFailed(event -> {
			loading.setVisible(false);
			rootPane.setDisable(false);
			changeWeekTask.getException().printStackTrace();
		});

		changeWeekTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			rootPane.setEffect(null);
			if (changeWeekTask.getValue()) {
				loadNoteBoard();
			} else {
				if(DataBaseHandler.getInstance().getMsg() == null)
					loadNoteBoard();
				else {
					Utils.popUpDialog(rootStack, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
					rootPane.setDisable(false);
				}
			}
		});

		new Thread(changeWeekTask).start();
	}
	
	public void updateOSPicker() {
		Console.print("Updating OS picker", "gui");
		orariS = DataBaseHandler.getInstance().getOS();
		String selectedOrariS = Config.getString("userconfig", Main.utente.getUsername()+"-selectedOrarioSettimanale");
		orarioSPicker.getItems().clear();
		for (int key : orariS.keySet()) {
			orarioSPicker.getItems().add(orariS.get(key).getNomeOrario());
			if (orariS.get(key).getNomeOrario().equals(selectedOrariS)) {
				orarioSPicker.getSelectionModel().select(selectedOrariS);
				os = getOSbyName(selectedOrariS);
				MetaData.os = os;
			}
		}
	}

	public OrarioSettimanale getOSbyName(String nome) {
		for (int key : orariS.keySet()) {
			if (orariS.get(key).getNomeOrario().equals(nome))
				return orariS.get(key);
		}
		return null;
	}

	public Materia getMateriaById(String id) {
		ArrayList<Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for (Materia m : materie) {
			if (m.getId() == Integer.parseInt(id))
				return m;
		}
		return null;
	}

	public void addVBoxToCell(GridPane osGrid, String idMateria, int row, int col, int rowSpan) {
		Materia m = getMateriaById(idMateria);
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setStyle("-fx-background-color:" + m.getColore() + ";");
		Label lbl = new Label();
		lbl.setText(m.getNome());
		lbl.setId("#nomeM");
		pane.getChildren().add(lbl);
		VBox bPane = new VBox();
		pane.getChildren().add(bPane);
		bPane.setAlignment(Pos.BASELINE_RIGHT);
		Button details = new Button();
		details.setBackground(Utils.imgToBackground("detailsImagePath"));
		details.setOnAction(e -> {
			LocalDate data = LocalDate.now().with(DayOfWeek.of(col + 1));
			openDetailsWindow(e,m.getNome(), data);
		});
		bPane.getChildren().add(details);
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
				if (!giorno.get(ora).equals(""))
					addVBoxToCell(osGrid, giorno.get(ora), startPos, col, length);
				else {
					VBox vPane = new VBox();
					osGrid.add(vPane, col, count);
				}
			}
			if (count != 0) {
				if (!giorno.get(ora).equals("") && giorno.get(ora).equals(materiaPrec)) {
					length++;
					if (length == 1)
						startPos = count;
				} else {
					if (length != 1) {
						addVBoxToCell(osGrid, materiaPrec, startPos, col, length);
						if (giorno.get(ora).equals("")) {
							VBox vPane = new VBox();
							osGrid.add(vPane, col, count);
						} else
							addVBoxToCell(osGrid, giorno.get(ora), count, col, 1);
					} else {
						length = 1;
						startPos = count;
						if (!giorno.get(ora).equals(""))
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

	public void openDetailsWindow(ActionEvent event, String materia, LocalDate data) {
		Console.print("Opening details window " + materia, "gui");
		MetaData.materiaSelected = materia;
		ControllerOreDetails cod = (ControllerOreDetails) Utils.loadWindow("oreDetailsFXML", 
				(Stage) ((Node) event.getSource()).getScene().getWindow(), false, null, null);
		cod.setTitle(materia + " - " + data.getDayOfWeek() + " - " + data);
		cod.setDate(data);
	}

	public void hamMenuAnimation(Pane pane, double width, boolean expand) {
		hamMenu.setDisable(true);
		if(!expand) {
			settingsButton.setText("");
			profileButton.setText("");
			closeButton.setText("");
		}
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
				new KeyValue(pane.prefWidthProperty(), width, Interpolator.EASE_BOTH)));
		timeline.play();
		timeline.setOnFinished(event -> {
			hamMenu.setDisable(false);
			if (expand) {
				settingsButton.setText("Settings");
				profileButton.setText("Profile");
				closeButton.setText("Log out");
			}
		});
	}

	@FXML
	public void openSettingsWindow(MouseEvent event) {
		Console.print("Opening settings window", "gui");
		Stage settings = Utils.loadWindowS("settingsFXML", (Stage) ((Node) event.getSource()).getScene().getWindow(),
				true, null, null);
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
			Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		}
	}

	public void initCalendarWeekDayHeader(LocalDate data, boolean clear) {
		if (clear)
			weekdayHeader.getChildren().clear();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-LLL");
		int weekdays = 6;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		for (int i = 0; i < weekdays; i++) {
			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getStyleClass().add("weekday-header");
			HBox.setHgrow(box, Priority.ALWAYS);
			box.setMaxWidth(Double.MAX_VALUE);
			box.setMinWidth(weekdayHeader.getPrefWidth() / weekdays);

			box.getChildren().add(new Label(weekDays[i]));
			Label timeLbl = new Label(data.with(DayOfWeek.of(i + 1)).format(dtf));
			timeLbl.setId("#time");
			box.getChildren().add(timeLbl);

			weekdayHeader.getChildren().add(box);
		}
	}

	public void initOrarioHeader() {
		int ore = 10;
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
		int rows = 10;
		int cols = 6;
		/*
		 * for (int i = 0; i < rows; i++) { for (int j = 0; j < cols; j++) { VBox vPane
		 * = new VBox(); vPane.getStyleClass().add("calendar_pane");
		 * vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
		 * vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
		 * 
		 * }); GridPane.setVgrow(vPane, Priority.ALWAYS); calendarGrid.add(vPane, j, i);
		 * } }
		 */
		for (int i = 0; i < rows; i++) {
			RowConstraints row = new RowConstraints();
			calendarGrid.getRowConstraints().add(row);
		}
		for (int i = 0; i < cols; i++) {
			ColumnConstraints col = new ColumnConstraints();
			calendarGrid.getColumnConstraints().add(col);
		}
	}

	public LocalDate getSelectedDate() {
		return datePicker.getValue();
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
			String img = Utils.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseEntered(e1 -> {
			String img = Utils.getFileURIByPath("config", "titleMaxmizeHoverImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleMaxmizeButton.setOnMouseExited(e1 -> {
			String img = Utils.getFileURIByPath("config", "titleMaxmizeImagePath").toString();
			titleMaxmizeImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseEntered(e1 -> {
			String img = Utils.getFileURIByPath("config", "titleHideHoverImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseExited(e1 -> {
			String img = Utils.getFileURIByPath("config", "titleHideImagePath").toString();
			titleHideImage.setImage(new Image(img));
		});
		titleHideButton.setOnMouseClicked(e -> {
			WindowStyle.hidde((Stage) rootPane.getScene().getWindow());
		});
		titleMaxmizeButton.setOnMouseClicked(e -> {
			WindowStyle.MaxMinScreen((Stage) rootPane.getScene().getWindow());
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) rootPane.getScene().getWindow());
			Console.print("Terminating application", "app");
		});
		titleHBox.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					WindowStyle.MaxMinScreen((Stage) rootPane.getScene().getWindow());
				}
			}
		});
	}

	/***********************************************/
}
