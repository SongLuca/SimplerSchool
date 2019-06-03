package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
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
import main.application.models.CustomStage;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.application.models.SchoolTask;
import main.application.models.Utente;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Preferences;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerMain {
	private final int HAMMENUSIZE = 230;
	@FXML
	private StackPane rootStack;

	@FXML
	private AnchorPane contentPane, rootPane, menuPane;

	@FXML
	private Pane menuShadowPane, gridShadowPane;

	@FXML
	private VBox menuVBox, oreHeader;

	@FXML
	private JFXHamburger hamMenu;

	@FXML
	private HBox weekdayHeader;

	@FXML
	private GridPane calendarGrid;

	@FXML
	private Circle avatar;

	@FXML
	private Label nomeLbl, cognomeLbl, scuolaLbl, boardFilterLbl;

	@FXML
	private Label weekBoardLbl, osLbl, dataLbl;

	@FXML
	private JFXTabPane tabPane;

	@FXML
	private JFXButton statisticButton, settingsButton, profileButton, closeButton;

	@FXML
	private JFXButton lastWeekBtn, thisWeekBtn, nextWeekBtn;

	@FXML
	private JFXButton insertTaskBtn;

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

	@FXML
	private JFXCheckBox checkInt, checkComp, checkVer;

	@FXML
	private JFXRadioButton radioOggi, radioSett, radioSucc;

	private AnchorPane statistics;

	private HashMap<Integer, OrarioSettimanale> orariS;

	private OrarioSettimanale os;

	private ToggleGroup radiosGroup;

	private double prefHeight = 800, prefWidth = 1400;

	public void initialize() {
		Console.print("Initializing menu gui", "gui");
		Preferences.loadPreferences();
		Console.print(Preferences.showPreferences(), "");
		orariS = DataBaseHandler.getInstance().getOS();
		initTitleBox();
		initHamMenu();
		initTabPane();
		initCalendarWeekDayHeader(LocalDate.now(), false);
		initOrarioHeader();
		initCalendarGrid();
		initProfilePane();
		initNoteboardFilters();
		loadNoteBoard();
		initLangBindings();
		MetaData.cm = this;
	}

	@FXML
	public void hamclicked(MouseEvent event) {
		double width = hamMenu.getPrefWidth();
		double height = hamMenu.getPrefHeight();
		if (menuPane.getPrefWidth() == 300) {
			hamMenu.setPrefSize(width - HAMMENUSIZE, height);
			hamMenu.setPadding(new Insets(0, 0, 0, 0));
			menuVBox.setPrefSize(width - HAMMENUSIZE, menuVBox.getHeight());
			for (Node button : menuVBox.getChildren()) {
				((JFXButton) button).setPrefSize(width - HAMMENUSIZE, height);
			}
			hamMenuAnimation(menuPane.getPrefWidth() - HAMMENUSIZE, false);
		} else if (menuPane.getPrefWidth() == 70) {
			hamMenu.setPrefSize(width + HAMMENUSIZE, height);
			hamMenu.setPadding(new Insets(0, HAMMENUSIZE, 0, 0));
			menuVBox.setPrefSize(width + HAMMENUSIZE, menuVBox.getPrefHeight());
			for (Node button : menuVBox.getChildren()) {
				((JFXButton) button).setPrefSize(width + HAMMENUSIZE, height);
			}
			hamMenuAnimation(menuPane.getPrefWidth() + HAMMENUSIZE, true);
		}
	}

	public void hamMenuAnimation(double width, boolean expand) {
		hamMenu.setDisable(true);
		menuPane.setDisable(true);
		if (!expand) {
			menuShadowPane.setPrefWidth(menuShadowPane.getPrefWidth() - HAMMENUSIZE);
			settingsButton.textProperty().unbind();
			statisticButton.textProperty().unbind();
			profileButton.textProperty().unbind();
			
			settingsButton.setText("");
			profileButton.setText("");
			closeButton.setText("");
			statisticButton.setText("");
		}
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
				new KeyValue(menuPane.prefWidthProperty(), width, Interpolator.EASE_BOTH)));
		timeline.play();
		timeline.setOnFinished(event -> {
			hamMenu.setDisable(false);
			menuPane.setDisable(false);
			if (expand) {
				menuShadowPane.setPrefWidth(menuShadowPane.getPrefWidth() + HAMMENUSIZE);
				LanguageBundle.buttonForValue(statisticButton, () -> LanguageBundle.get("statisticsBtnTitle", 0));
				LanguageBundle.buttonForValue(settingsButton, () -> LanguageBundle.get("configBtnTitle", 0));
				LanguageBundle.buttonForValue(profileButton, () -> LanguageBundle.get("profiloBtnTitle", 0));
				closeButton.setText("Log out");
			}
		});
	}

	public StackPane getRootStack() {
		return rootStack;
	}

	public AnchorPane getRootPane() {
		return rootPane;
	}

	public JFXSpinner getLoading() {
		return loading;
	}

	public OrarioSettimanale getOs() {
		return os;
	}

	public Label getOsLbl() {
		return osLbl;
	}

	public void initLangBindings() {
		LanguageBundle.checkBoxForValue(checkInt, () -> LanguageBundle.get("interrogazioni", 0));
		LanguageBundle.checkBoxForValue(checkComp, () -> LanguageBundle.get("compitiPerCasa", 0));
		LanguageBundle.checkBoxForValue(checkVer, () -> LanguageBundle.get("verifiche", 0));

		LanguageBundle.radioButtonForValue(radioOggi, () -> LanguageBundle.get("radioBtnOggi", 0));
		LanguageBundle.radioButtonForValue(radioSett, () -> LanguageBundle.get("radioBtnSettimana", 0));
		LanguageBundle.radioButtonForValue(radioSucc, () -> LanguageBundle.get("radioBrnSuccssivi", 0));

		LanguageBundle.labelForValue(boardFilterLbl, () -> LanguageBundle.get("boardFilterLbl", 0));
		LanguageBundle.labelForValue(weekBoardLbl, () -> LanguageBundle.get("weekBoardLbl", 0));
		LanguageBundle.labelForValue(osLbl, () -> LanguageBundle.get("osLbl", 0));
		LanguageBundle.labelForValue(dataLbl, () -> LanguageBundle.get("dataLbl", 0));

		LanguageBundle.buttonForValue(insertTaskBtn, () -> LanguageBundle.get("insertTaskBtn", 0));
		LanguageBundle.buttonForValue(lastWeekBtn, () -> LanguageBundle.get("lastWeekBtn", 0));
		LanguageBundle.buttonForValue(thisWeekBtn, () -> LanguageBundle.get("thisWeekBtn", 0));
		LanguageBundle.buttonForValue(nextWeekBtn, () -> LanguageBundle.get("nextWeekBtn", 0));
	}

	public void initNoteboardFilters() {
		checkVer.setSelected(true);
		checkInt.setSelected(true);
		checkComp.setSelected(true);
		checkVer.setOnAction(e -> {
			loadNoteBoard();
		});
		checkInt.setOnAction(e -> {
			loadNoteBoard();
		});
		checkComp.setOnAction(e -> {
			loadNoteBoard();
		});

		radiosGroup = new ToggleGroup();
		radioOggi.setUserData("oggi");
		radioSett.setUserData("settimana");
		radioSucc.setUserData("successivi");
		radioOggi.setToggleGroup(radiosGroup);
		radioSett.setToggleGroup(radiosGroup);
		radioSucc.setToggleGroup(radiosGroup);
		radioSett.setSelected(true);

		radioOggi.setOnAction(e -> {
			loadNoteBoard();
		});
		radioSett.setOnAction(e -> {
			loadNoteBoard();
		});
		radioSucc.setOnAction(e -> {
			loadNoteBoard();
		});

	}

	public void radiosBtnSetDisable(boolean value) {
		radioOggi.setDisable(value);
		radioSett.setDisable(value);
		radioSucc.setDisable(value);
	}

	public boolean isThisWeek() {
		LocalDate selected = datePicker.getValue();
		LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
		LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
		if ((selected.isAfter(monday) || selected.isEqual(monday))
				&& (selected.isBefore(sunday) || selected.isEqual(sunday))) {
			this.radiosBtnSetDisable(false);
			return true;
		} else {
			this.radiosBtnSetDisable(true);
			return false;
		}

	}

	public void loadNoteBoard() {
		Console.print("Loading note board", "gui");
		noteBoard.clear();
		int attivitaCount = 0;
		boolean currWeek = isThisWeek();
		ArrayList<SchoolTask> attivita = DataBaseHandler.getInstance().getAttivita();
		if (attivitaIsEmpty(attivita)) {
			noteBoard.setText(LanguageBundle.get("noAttivita"));
		} else {
			boolean compiti = checkComp.isSelected(), verifiche = checkVer.isSelected(),
					interrogazioni = checkInt.isSelected(), ceCompiti = false, ceVerifiche = false,
					ceInterrogazioni = false;
			String verifica = LanguageBundle.get("verifiche") + ":\n",
					compito = LanguageBundle.get("compitiPerCasa") + ":\n",
					interrogazione = LanguageBundle.get("interrogazioni") + ":\n";
			for (SchoolTask task : attivita) {
				if (!currWeek) {
					if (task.getTipo().equalsIgnoreCase("Verifica") && verifiche) {
						verifica += printVerifiche(task);
						ceVerifiche = true;
						attivitaCount++;
					}
					if (task.getTipo().equalsIgnoreCase("Compiti per casa") && compiti) {
						compito += printCompiti(task);
						ceCompiti = true;
						attivitaCount++;
					}
					if (task.getTipo().equalsIgnoreCase("interrogazione") && interrogazioni) {
						interrogazione += printInterrogazioni(task);
						ceInterrogazioni = true;
						attivitaCount++;
					}
				} else {
					switch (radiosGroup.getSelectedToggle().getUserData().toString()) {
					case "oggi":
						if (task.getData().isEqual(LocalDate.now())) {
							if (task.getTipo().equalsIgnoreCase("Verifica") && verifiche) {
								verifica += printVerifiche(task);
								ceVerifiche = true;
								attivitaCount++;
							}
							if (task.getTipo().equalsIgnoreCase("Compiti per casa") && compiti) {
								compito += printCompiti(task);
								ceCompiti = true;
								attivitaCount++;
							}
							if (task.getTipo().equalsIgnoreCase("interrogazione") && interrogazioni) {
								interrogazione += printInterrogazioni(task);
								ceInterrogazioni = true;
								attivitaCount++;
							}
						}
						break;
					case "settimana":
						if (task.getTipo().equalsIgnoreCase("Verifica") && verifiche) {
							verifica += printVerifiche(task);
							ceVerifiche = true;
							attivitaCount++;
						}
						if (task.getTipo().equalsIgnoreCase("Compiti per casa") && compiti) {
							compito += printCompiti(task);
							ceCompiti = true;
							attivitaCount++;
						}
						if (task.getTipo().equalsIgnoreCase("interrogazione") && interrogazioni) {
							interrogazione += printInterrogazioni(task);
							ceInterrogazioni = true;
							attivitaCount++;
						}
						break;
					case "successivi":
						if (task.getData().isAfter(LocalDate.now()) || task.getData().isEqual(LocalDate.now())) {
							if (task.getTipo().equalsIgnoreCase("Verifica") && verifiche) {
								verifica += printVerifiche(task);
								ceVerifiche = true;
								attivitaCount++;
							}
							if (task.getTipo().equalsIgnoreCase("Compiti per casa") && compiti) {
								compito += printCompiti(task);
								ceCompiti = true;
								attivitaCount++;
							}
							if (task.getTipo().equalsIgnoreCase("interrogazione") && interrogazioni) {
								interrogazione += printInterrogazioni(task);
								ceInterrogazioni = true;
								attivitaCount++;
							}
						}
						break;
					}
				}
			}

			if (!ceVerifiche) {
				verifica = LanguageBundle.get("noVerifica") + "\n";
			}

			if (!ceCompiti) {
				compito = LanguageBundle.get("noCompitiPerCasa") + "\n";
			}
			if (!ceInterrogazioni) {
				interrogazione = LanguageBundle.get("noInterrogazione") + "\n";
			}
			noteBoard.setText(attivitaCount + " " + LanguageBundle.get("attivita") + ":\n");
			if (verifiche)
				noteBoard.appendText(verifica);
			if (compiti)
				noteBoard.appendText(compito);
			if (interrogazioni)
				noteBoard.appendText(interrogazione);
		}
		noteBoard.positionCaret(0);
	}

	public String printCompiti(SchoolTask task) {
		String compito = "";
		compito += "\t" + LanguageBundle.get("materia") + ": " + task.getMateriaNome() + "\n";
		compito += "\t" + LanguageBundle.get("data") + ": " + task.getData() + "\n";
		if (task.getComment().length() != 0)
			compito += "\t" + LanguageBundle.get("commento") + ": " + task.getComment() + "\n";
		else
			compito += "\t" + LanguageBundle.get("noCommento") + "\n";
		compito += "\t----------------------\n";
		return compito;
	}

	public String printVerifiche(SchoolTask task) {
		String verifica = "";
		verifica += "\t" + LanguageBundle.get("materia") + ": " + task.getMateriaNome() + "\n";
		verifica += "\t" + LanguageBundle.get("data") + ": " + task.getData() + "\n";
		if (task.getVoto() > -1)
			verifica += "\t" + LanguageBundle.get("voto") + ": " + task.getVoto() + "\n";
		else
			verifica += "\t" + LanguageBundle.get("noVoto") + "\n";
		if (task.getComment().length() != 0)
			verifica += "\t" + LanguageBundle.get("commento") + ": " + task.getComment() + "\n";
		else
			verifica += "\t" + LanguageBundle.get("noCommento") + "\n";
		verifica += "\t----------------------\n";
		return verifica;
	}

	public String printInterrogazioni(SchoolTask task) {
		String interrogazione = "";
		interrogazione += "\t" + LanguageBundle.get("materia") + ": " + task.getMateriaNome() + "\n";
		interrogazione += "\t" + LanguageBundle.get("data") + ": " + task.getData() + "\n";
		if (task.getVoto() > -1)
			interrogazione += "\t" + LanguageBundle.get("voto") + ": " + task.getVoto() + "\n";
		else
			interrogazione += "\t" + LanguageBundle.get("noVoto") + "\n";
		if (task.getComment().length() != 0)
			interrogazione += "\t" + LanguageBundle.get("commento") + ": " + task.getComment() + "\n";
		else
			interrogazione += "\t" + LanguageBundle.get("noCommento") + "\n";
		interrogazione += "\t----------------------\n";
		return interrogazione;
	}

	public boolean attivitaIsEmpty(ArrayList<SchoolTask> attivita) {
		int allegatiCount = 0;
		for (SchoolTask task : attivita) {
			if (task.getTipo().equalsIgnoreCase("Allegato file")) {
				allegatiCount++;
			}
		}
		return allegatiCount == attivita.size();
	}

	@FXML
	public void insertTask(MouseEvent e) {
		Console.print("Opening insert window", "gui");
		ControllerInsertTask cit = (ControllerInsertTask) Utils.loadWindow("insertTaskFXML",
				(Stage) ((Node) e.getSource()).getScene().getWindow(), false, null, null);
		cit.setTitle(LanguageBundle.get("insertTaskTitle"));
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
		statisticButton.setOnMouseClicked(e -> {
			if (contentPane.isVisible()) {
				try {
					URL fxmlURL = new File(Config.getString(Main.CONFIG, "statisticsFXML")).toURI().toURL();
					FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
					statistics = fxmlLoader.load();
					rootPane.getChildren().add(statistics);
					contentPane.setVisible(false);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				changeWeek(datePicker.getValue());
				rootPane.getChildren().remove(statistics);
				statistics = null;
				contentPane.setVisible(true);
			}
		});
		profileButton.setOnMouseClicked(e->{
			try {
				CustomStage window = new CustomStage((Stage) ((Node) e.getSource()).getScene().getWindow());
				FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "docentiFXML").toURL());
				AnchorPane contentPane = fxmlLoader.load();
				window.setContent(contentPane);
				window.setMinSize(Config.getDouble(Main.CONFIG, "minWidthSettings"), 
						Config.getDouble(Main.CONFIG, "minHeightSettings"));
				window.bindTitleLanguage("Settings-Lecturers");
				window.show();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	public void initTabPane() {
		updateOSPicker();
		datePicker.setValue(LocalDate.now());
		datePicker.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.getDayOfWeek() == DayOfWeek.SUNDAY);
			}
		});
		datePicker.setOnAction(e -> {
			initCalendarWeekDayHeader(datePicker.getValue(), true);
			changeWeek(datePicker.getValue());
		});

		lastWeekBtn.setOnAction(e -> {
			LocalDate lastWeek = datePicker.getValue().minusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().minusWeeks(1), true);
			datePicker.setValue(lastWeek);
			Console.print("Jumping to the last week " + lastWeek, "Gui");
		});

		thisWeekBtn.setOnAction(e -> {
			LocalDate today = LocalDate.now();
			if (datePicker.getValue().compareTo(today) != 0) {
				initCalendarWeekDayHeader(today, true);
				datePicker.setValue(today);
				Console.print("Jumping to current week " + today, "Gui");
			}
		});

		nextWeekBtn.setOnAction(e -> {
			LocalDate nextWeek = datePicker.getValue().plusWeeks(1);
			initCalendarWeekDayHeader(datePicker.getValue().plusWeeks(1), true);
			datePicker.setValue(nextWeek);
			Console.print("Jumping to the next week " + nextWeek, "Gui");
		});

		orarioSPicker.setOnAction(e -> {
			String selectedOS = orarioSPicker.getSelectionModel().getSelectedItem();
			if (selectedOS != null) {
				Console.print(selectedOS + " selected", "Gui");
				os = getOSbyName(selectedOS);
				Config.userConfig.setProperty(Main.utente.getUsername() + "-selectedOrarioSettimanale", selectedOS);
				Utils.saveProperties(Main.USERCONFIG, true);
				for (String giornoK : os.getSettimana().keySet()) {
					int dayCol = os.getColByGiorno(giornoK);
					fuseSubjects(calendarGrid, dayCol);
				}
				changeWeek(datePicker.getValue());
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
				return DataBaseHandler.getInstance().getAttivitaSettimanaleQuery(data, false);
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
				Utils.popUpDialog(rootStack, rootPane, "Error", DataBaseHandler.getInstance().getMsg());
				rootPane.setDisable(false);
			}
		});

		new Thread(changeWeekTask).start();
	}

	public void updateOSPicker() {
		Console.print("Updating OS picker", "gui");
		orariS = DataBaseHandler.getInstance().getOS();
		String selectedOrariS = Config.getString(Main.USERCONFIG,
				Main.utente.getUsername() + "-selectedOrarioSettimanale");
		orarioSPicker.getItems().clear();
		for (int key : orariS.keySet()) {
			orarioSPicker.getItems().add(orariS.get(key).getNomeOrario());
			if (orariS.get(key).getNomeOrario().equals(selectedOrariS)) {
				orarioSPicker.getSelectionModel().select(selectedOrariS);
				os = getOSbyName(selectedOrariS);
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
			LocalDate data = datePicker.getValue().with(DayOfWeek.of(col + 1));
			openDetailsWindow(e, m.getNome(), data);
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

	@FXML
	public void openSettingsWindow(MouseEvent event) {
		Console.print("Opening settings window", "gui");
		Stage settings = Utils.loadWindowS("settingsFXML", (Stage) ((Node) event.getSource()).getScene().getWindow(),
				true, null, null);
		settings.setMinHeight(Config.getDouble(Main.CONFIG, "minHeightSettings"));
		settings.setMinWidth(Config.getDouble(Main.CONFIG, "minWidthSettings"));
		Console.print(settings.getHeight() + "", "");
		Console.print(settings.getWidth() + "", "");
		settings.setOnHiding(e -> {
			WindowStyle.stageDimension(prefWidth, prefHeight);
		});
	}

	@FXML
	public void openCloseWindow(MouseEvent event) {
		Console.print("Opening logout dialog", "gui");
		Stage owner = (Stage) rootPane.getScene().getWindow();
		ConfirmDialog cd = new ConfirmDialog(owner, "exitConfirmLbl");
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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-LLL", LanguageBundle.getLocale());
		int weekdays = 6;
		String[] weekDays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		for (int i = 0; i < weekdays; i++) {
			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getStyleClass().add("weekday-header");
			HBox.setHgrow(box, Priority.ALWAYS);
			box.setMaxWidth(Double.MAX_VALUE);
			box.setMinWidth(weekdayHeader.getPrefWidth() / weekdays);
			String day = weekDays[i];
			Label weekDay = LanguageBundle.newLabelForValue(() -> LanguageBundle.get(day, 0));
			box.getChildren().add(weekDay);
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
		titleHideButton.setOnMouseEntered(e1 -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleHideHoverImagePath").toString();
			titleHideImage.setImage(new Image(img));
			rootPane.getScene().setCursor(Cursor.DEFAULT);
		});
		titleHideButton.setOnMouseExited(e1 -> {
			String img = Utils.getFileURIByPath(Main.CONFIG, "titleHideImagePath").toString();
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
