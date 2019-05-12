package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXSpinner;
import animatefx.animation.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import main.application.customGUI.ConfirmDialog;
import main.application.customGUI.TextFieldDialog;
import main.application.models.Config;
import main.application.models.Insegna;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.controllers.orariosettimanale.ControllerOrarioSBox;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.Utils;

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
	
	@FXML
    private JFXSpinner loading;
	
	private OrarioSettimanale os;

	private ArrayList<Materia> materie;
	
	private HashMap<Integer, OrarioSettimanale> orariS;
	
	private ArrayList<Insegna> insegna;
	
	private boolean updateInsegna;
	
	public void initialize() {
		setOSButtonVisible(false);
		initOSList();
		updateInsegna = false;
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
		MetaData.cos = this;
		clearButton.setOnMouseClicked(e -> {
			Stage owner = (Stage)calendarioPane.getScene().getWindow();
			ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure to clear this?");
			calendarioPane.requestFocus();
			if(cd.getResult()) { 
				initOSCalendarGrid();
				os.initMaps();
				updateOSTask("saved", false);
				MetaData.os = this.os;
				MetaData.os.toString();
			}
		});

		backButton.setOnMouseClicked(e -> {
			MetaData.os = null;
			rootCalendar.setVisible(false);
			calendarioPane.setVisible(true);
			setOSButtonVisible(false);
			Label titolo = (Label)calendarioPane.getScene().lookup("#title");
			titolo.setText("Settings - Orario Settimanale");
			initOSList();
			new FadeIn(calendarioPane).play();
			subContentPane.requestFocus();
		});

		deleteButton.setOnMouseClicked(e -> {
			if(!os.getStato().equals("insert")) {
				Stage owner = (Stage)calendarioPane.getScene().getWindow();
				ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure to delete this?");
				calendarioPane.requestFocus();
				if(cd.getResult()) { 
					os.setStato("delete");
					updateOSTask(os.getNomeOrario() + " deleted", true); 
					rootCalendar.setVisible(false);
					calendarioPane.setVisible(true);
					setOSButtonVisible(false);
					new FadeIn(calendarioPane).play();
				}
			}
		});
		
		saveButton.setOnMouseClicked(e -> {
			updateOSTask("saved", false);
		});
	}
	
	public void setInsegna(ArrayList<Insegna> insegna) {
		this.insegna = insegna;
		updateInsegna = true;
	}
	
	public void updateOSTask(String doneMsg, boolean refreshList) {
		StackPane root = (StackPane) calendarGrid.getScene().lookup("#rootStack");
		AnchorPane pane = (AnchorPane) calendarGrid.getScene().lookup("#rootPane");
		Task<Boolean> updateOSTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				subContentPane.setEffect(Effect.blur());
				if(!updateInsegna)
					return DataBaseHandler.getInstance().updateOSTable(os);
				else
					return DataBaseHandler.getInstance().updateOSTable(os) &&
							DataBaseHandler.getInstance().updateInsegnaTable(insegna);
			}
		};

		updateOSTask.setOnFailed(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			Utils.popUpDialog(root, pane, "Message", "RIP");
			updateOSTask.getException().printStackTrace();
		});

		updateOSTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			if (updateOSTask.getValue()) {
				orariS = DataBaseHandler.getInstance().getOS();
				if(!os.getStato().equals("fresh")) {
					os = this.getOSByNome(os.getNomeOrario());
					MetaData.os = os;
				}
				if(refreshList)
					initOSList();
				MetaData.cm.updateOSPicker();
				Utils.popUpDialog(root, pane, "Message", doneMsg);
			}
		});
		new Thread(updateOSTask).start();
	}
	
	public void setTitle(String title) {
		Label titolo = (Label)calendarioPane.getScene().lookup("#title");
		titolo.setText(title);
	}
	
	public void initOSList() {
		calendarioPane.getChildren().retainAll(calendarioPane.getChildren().get(0));
		orariS = DataBaseHandler.getInstance().getOS();
		for(int key : orariS.keySet()) {
			try {
				URL fxmlURL = new File(Config.getString("config", "orarioSBoxFXML")).toURI().toURL();
				FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
				JFXButton osBox = fxmlLoader.load();
				ControllerOrarioSBox c = fxmlLoader.<ControllerOrarioSBox>getController();
				c.setNome(orariS.get(key).getNomeOrario());
				c.setOpenAction(e->{
					setTitle("Settings - Orario Settimanale: "+orariS.get(key).getNomeOrario());
					loadCalendar(orariS.get(key).getNomeOrario());
					setOSButtonVisible(true);
				});
				c.setDeleteAction(e->{
					Stage owner = (Stage)calendarioPane.getScene().getWindow();
					ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure you want to delete this?");
					calendarioPane.requestFocus();
					if(cd.getResult()) { 
						os = orariS.get(key);
						os.setStato("delete");
						updateOSTask(os.getNomeOrario() + " deleted", true); 
					}
				});
				c.setRenameAction(e->{
					Stage owner = (Stage)calendarioPane.getScene().getWindow();
					TextFieldDialog tfd = new TextFieldDialog(owner, orariS , orariS.get(key).getNomeOrario(), "Rename to:");
					calendarioPane.requestFocus();
					String newN = tfd.getResult();
					String oldN = orariS.get(key).getNomeOrario();
					if(!tfd.getResult().equals("")) {
						os = orariS.get(key);
						os.setNomeOrario(newN);
						os.setStato("update");
						updateOSTask(oldN + "changed to " + newN, true);
					}
				});
				calendarioPane.getChildren().add(osBox);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadCalendar(String nomeOS) {
		setOSButtonVisible(false);
		calendarioPane.setVisible(false);
		os = getOSByNome(nomeOS);
		MetaData.os = this.os;
		MetaData.OrarioSGrid = calendarGrid;
		initOSCalendarWeekDayHeader();
		initOrarioHeader();
		reRenderCalendario();
		rootCalendar.setVisible(true);
		new FadeIn(rootCalendar).play();
		subContentPane.requestFocus();
	}
	
	public void initCalendar(String nomeOS) {
		setOSButtonVisible(false);
		calendarioPane.setVisible(false);
		initOSCalendarWeekDayHeader();
		initOSCalendarGrid();
		initOrarioHeader();
		rootCalendar.setVisible(true);
		new FadeIn(rootCalendar).play();
		os = new OrarioSettimanale(nomeOS);
		os.setStato("insert");
		MetaData.os = this.os;
		MetaData.OrarioSGrid = calendarGrid;
		initOSList();
		subContentPane.requestFocus();
	}
	
	public void setOSButtonVisible(boolean visible) {
		saveButton.setVisible(visible);
		deleteButton.setVisible(visible);
		backButton.setVisible(visible);
		clearButton.setVisible(visible);
	}
	
	public void reRenderCalendario() {
		Console.print("Rerendering calendario","gui");
		initOSCalendarGrid();
		materie = DataBaseHandler.getInstance().getMaterie();
		this.os = MetaData.os;
		for (String giornoK : os.getSettimana().keySet()) {
			int dayCol = os.getColByGiorno(giornoK);
			fuseSubjects(calendarGrid, dayCol);
		}
	}

	public Materia getMateriaById(String id) {
		materie = DataBaseHandler.getInstance().getMaterie();
		for (Materia m : materie) {
			if (m.getId() == Integer.parseInt(id))
				return m;
		}
		return null;
	}
	
	public OrarioSettimanale getOSByNome(String nome) {
		for(int key : orariS.keySet()){
			if(orariS.get(key).getNomeOrario().equals(nome))
				return orariS.get(key);
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
				if (!giorno.get(ora).equals(""))
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
				if (!giorno.get(ora).equals("") && giorno.get(ora).equals(materiaPrec)) {
					length++;
					if (length == 1)
						startPos = count;
				} else {
					if (length != 1) {
						addVBoxToCell(osGrid, materiaPrec, startPos, col, length);
						if (giorno.get(ora).equals("")) {
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
						if (!giorno.get(ora).equals(""))
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
		Utils.loadNoTitleWindow("addOSFXML", (Stage) ((Node) e.getSource()).getScene().getWindow(), false, null,
				null);
	}

	public void initOSCalendarWeekDayHeader() {
		weekdayHeader.getChildren().clear();
		int weekdays = 6;
		String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
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
		int rows = 10;
		int cols = 6;
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

	public void addMateria(VBox vPane) {
		MetaData.controller = this;
		MetaData.sub_row = GridPane.getRowIndex(vPane);
		MetaData.sub_col = GridPane.getColumnIndex(vPane);
		Utils.loadNoTitleWindow("addSubjectFXML", null, false, null, null);
	}

}
