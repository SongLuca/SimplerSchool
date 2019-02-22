package main.controllers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.database.DataBaseHandler;
import main.utils.SimplerSchoolUtil;

public class ControllerOrarioS {
	@FXML
	private HBox weekdayHeader;

	@FXML
	private VBox oreHeader;

	@FXML
	private GridPane calendarGrid;

	@FXML
	private AnchorPane subContentPane;

	@FXML
	private VBox calendarioBox;

	@FXML
	private JFXMasonryPane calendarioPane;
	
	@FXML
	private AnchorPane rootCalendar;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton deleteButton;

	@FXML
	private JFXButton backButton;

	@FXML
	private JFXButton clearButton;

	private OrarioSettimanale os;

	private HashMap<Integer, Materia> materie;

	public void initialize() {
		saveButton.setDisable(true);
		deleteButton.setDisable(true);
		backButton.setDisable(true);
		clearButton.setDisable(true);

		clearButton.setOnMouseClicked(e -> {
			initOSCalendarGrid();
			os = new OrarioSettimanale(os.getNomeOrario());
			MetaData.os = this.os;
			MetaData.os.toString();
		});

		backButton.setOnMouseClicked(e -> {
			MetaData.os = null;
			rootCalendar.setVisible(false);
			calendarioPane.setVisible(true);

			saveButton.setDisable(true);
			deleteButton.setDisable(true);
			backButton.setDisable(true);
			clearButton.setDisable(true);

			new FadeIn(calendarioPane).play();
			subContentPane.requestFocus();
		});

		deleteButton.setOnMouseClicked(e -> {
			rootCalendar.setVisible(false);
			calendarioPane.setVisible(true);
			saveButton.setDisable(false);
			deleteButton.setDisable(false);
			backButton.setDisable(false);
			clearButton.setDisable(false);
			new FadeIn(calendarioPane).play();
		});

		saveButton.setOnMouseClicked(e -> {
			os.toXML();
			StackPane root = (StackPane) calendarGrid.getScene().lookup("#rootStack");
			AnchorPane pane = (AnchorPane) calendarGrid.getScene().lookup("#rootPane");
			SimplerSchoolUtil.popUpDialog(root, pane, "Message", "done!");
		});
	}

	public void initCalendar(String nomeOS) {
		calendarioPane.setVisible(false);
		initOSCalendarWeekDayHeader();
		initOSCalendarGrid();
		initOrarioHeader();
		rootCalendar.setVisible(true);
		new FadeIn(rootCalendar).play();
		os = new OrarioSettimanale(nomeOS);
		MetaData.os = this.os;
		MetaData.OrarioSGrid = calendarGrid;
		subContentPane.requestFocus();
	}

	public void reRenderCalendario() {
		System.out.println("rerendering calendario");
		initOSCalendarGrid();
		materie = DataBaseHandler.getInstance().getMaterie();
		this.os = MetaData.os;
		for (String giornoK : os.getSettimana().keySet()) {
			int dayCol = os.getColByGiorno(giornoK);
			fuseSubjects(calendarGrid, dayCol);
		}
	}

	public Materia getMateriaByNome(String nome) {
		materie = DataBaseHandler.getInstance().getMaterie();
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
		pane.getStyleClass().add("calendar_pane");
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			addMateria(pane);
		});
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
					vPane.getStyleClass().add("calendar_pane");
					vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
						addMateria(vPane);
					});
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
							vPane.getStyleClass().add("calendar_pane");
							vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
								addMateria(vPane);
							});
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
							vPane.getStyleClass().add("calendar_pane");
							vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
								addMateria(vPane);
							});
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
	public void newOS(MouseEvent e) {
		MetaData.controller = this;
		SimplerSchoolUtil.loadWindow("addOSFXML", (Stage) ((Node) e.getSource()).getScene().getWindow(), false, null,
				null);
		saveButton.setDisable(false);
		deleteButton.setDisable(false);
		backButton.setDisable(false);
		clearButton.setDisable(false);
	}

	@FXML
	void openMaterie(MouseEvent event) {
		SimplerSchoolUtil.loadWindow("materieFXML", (Stage) ((Node) event.getSource()).getScene().getWindow(), false,
				null, null);
	}

	public void initOSCalendarWeekDayHeader() {
		weekdayHeader.getChildren().clear();
		int weekdays = 7;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		for (int i = 0; i < weekdays; i++) {
			StackPane pane = new StackPane();
			pane.getStyleClass().add("weekday-header");
			HBox.setHgrow(pane, Priority.ALWAYS);
			pane.setMaxWidth(Double.MAX_VALUE);
			pane.setMinWidth(weekdayHeader.getPrefWidth() / weekdays);
			weekdayHeader.getChildren().add(pane);
			pane.getChildren().add(new Label(weekDays[i]));
		}
	}

	public void initOSCalendarGrid() {
		calendarGrid.getChildren().retainAll(calendarGrid.getChildren().get(0));
		int rows = 11;
		int cols = 7;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				VBox vPane = new VBox();
				vPane.getStyleClass().add("calendar_pane");
				vPane.setMinWidth(weekdayHeader.getPrefWidth() / cols);
				vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					addMateria(vPane);
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

	public void initOrarioHeader() {
		oreHeader.getChildren().clear();
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

	public void addMateria(VBox vPane) {
		MetaData.sub_row = GridPane.getRowIndex(vPane);
		MetaData.sub_col = GridPane.getColumnIndex(vPane);
		SimplerSchoolUtil.loadNoTitleWindow("addSubjectFXML", null, false, null, null);
	}

}
