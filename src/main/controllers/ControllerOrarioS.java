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
	private GridPane calendarGrid;

	@FXML
	private AnchorPane subContentPane;
	
	@FXML
	private VBox calendarioBox;

	@FXML
	private JFXMasonryPane calendarioPane;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton deleteButton;

	@FXML
	private JFXButton backButton;
	@FXML
	private JFXButton clearButton;
	
	private OrarioSettimanale os;

	public void initialize() {
		weekdayHeader.setVisible(false);
		calendarGrid.setVisible(false);
		clearButton.setOnMouseClicked(e->{
			initOSCalendarGrid(weekdayHeader, calendarGrid);
			os = new OrarioSettimanale(os.getNomeOrario());
			MetaData.os = this.os;
			MetaData.os.toString();
		});
		backButton.setOnMouseClicked(e->{
			calendarioBox.setVisible(false);
			calendarioPane.setVisible(true);
			new FadeIn(calendarioPane).play();
		});
		deleteButton.setOnMouseClicked(e->{
			calendarioBox.setVisible(false);
			calendarioPane.setVisible(true);
			new FadeIn(calendarioPane).play();
		});
		saveButton.setOnMouseClicked(e->{
			os.toXML(); 
			StackPane root = (StackPane) calendarGrid.getScene().lookup("#rootStack");
			AnchorPane pane = (AnchorPane) calendarGrid.getScene().lookup("#rootPane");
			SimplerSchoolUtil.popUpDialog(root, pane, "Message", "done!");
		});
	}

	public void initCalendar(String nomeOS) {
		calendarioPane.setVisible(false);
		initOSCalendarWeekDayHeader(weekdayHeader);
		initOSCalendarGrid(weekdayHeader, calendarGrid);
		calendarioBox.setVisible(true);
		new FadeIn(calendarioBox).play();
		os = new OrarioSettimanale(nomeOS);
		MetaData.os = this.os;
		MetaData.OrarioSGrid = calendarGrid;
		subContentPane.requestFocus();
	}
	
	public void reRenderCalendario() {
		System.out.println("rerendering calendario");
		calendarGrid.getChildren().retainAll(calendarGrid.getChildren().get(0));
		initOSCalendarGrid(weekdayHeader, calendarGrid);
		HashMap<Integer, Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for(String giornoK : os.getSettimana().keySet()) {
			LinkedHashMap<String, String> giornoM = os.getSettimana().get(giornoK);
			for(String oraK : giornoM.keySet()) {
				if(giornoM.get(oraK) != null) {
					Materia m = os.findMateriaByNome(materie, giornoM.get(oraK));
					int ora = os.getRowByOra(oraK);
					int giorno = os.getColByGiorno(giornoK);
					VBox pane = new VBox();
					pane.setAlignment(Pos.CENTER);
					pane.setStyle("-fx-background-color:"+m.getColore()+";");
					Label lbl = new Label();
					lbl.setText(m.getNome());
					pane.getChildren().add(lbl);
					pane.getStyleClass().add("calendar_pane");
					pane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
						addMateria(pane);
					});
					calendarGrid.add(pane, giorno, ora);
				}
			}
		}
	}
	
	@FXML
	public void newOS(MouseEvent e) {
		MetaData.controller = this;
		SimplerSchoolUtil.loadWindow("addOSFXML", (Stage) ((Node) e.getSource()).getScene().getWindow(), false, null,
				null);
	}

	@FXML
	void openMaterie(MouseEvent event) {
		SimplerSchoolUtil.loadWindow("materieFXML", (Stage) ((Node) event.getSource()).getScene().getWindow(), false,
				null, null);
	}

	public void initOSCalendarWeekDayHeader(HBox weekdayHeader) {
		weekdayHeader.getChildren().clear();
		weekdayHeader.setVisible(true);
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

	public void initOSCalendarGrid(HBox weekdayHeader, GridPane calendarGrid) {
		calendarGrid.getChildren().retainAll(calendarGrid.getChildren().get(0));
		calendarGrid.setVisible(true);
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
		for (int i = 0; i < cols; i++) {
			RowConstraints row = new RowConstraints();
			calendarGrid.getRowConstraints().add(row);
		}
	}

	public void addMateria(VBox vPane) {
		MetaData.sub_row = GridPane.getRowIndex(vPane);
		MetaData.sub_col = GridPane.getColumnIndex(vPane);
		SimplerSchoolUtil.loadNoTitleWindow("addSubjectFXML", null, false, null, null);
	}

}
