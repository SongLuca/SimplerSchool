package main.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Utils;

public class ControllerAddSubject {

	@FXML
	private JFXComboBox<String> materiaBox;

	@FXML
	private JFXComboBox<String> profBox;

	@FXML
	private JFXComboBox<String> prof2Box;
	
	private ArrayList<Materia> materie;
	
	@FXML
	void cancel(MouseEvent event) {
		Stage stage = (Stage) materiaBox.getScene().getWindow();
		stage.close();
	}

	@FXML
	void save(MouseEvent event) {
		String ora = (MetaData.sub_row+1) + "ora";
		String giorno = Utils.numToDay(MetaData.sub_col);
		if(!materiaBox.getValue().equals("")) {
			Materia m = getMateriaByNome(materiaBox.getValue());
			Console.print("Os: " + MetaData.os.getNomeOrario() + " adding " + m.getNome()+m.getId() + " to " + ora + " at " + giorno, "app");
			MetaData.os.addMateria(ora, giorno, m.getId()+"");
		}
		else {
			MetaData.os.addMateria(ora, giorno, "");
		}
		fuseSubjects(MetaData.OrarioSGrid, MetaData.sub_col);
		cancel(event);
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
			((ControllerOrarioS) MetaData.controller).addMateria(pane);
		});
		if(rowSpan != 1) 
			osGrid.add(pane, col, row, 1, rowSpan);
		else
			osGrid.add(pane, col, row);
	}
	
	public void fuseSubjects (GridPane osGrid, int col) {
		LinkedHashMap<String, String> giorno = MetaData.os.getOrarioGiorno(col);
		osGrid.getChildren().removeIf(node -> (node instanceof VBox) && GridPane.getColumnIndex(node) == col);
		int count = 0;
		int length = 1;
		int startPos = 0;
		String materiaPrec = "";
		for(String ora : giorno.keySet()) {
			if(count == 0) {
				if(!giorno.get(ora).equals(""))
					addVBoxToCell(osGrid, giorno.get(ora), startPos, col, length);
				else {
					VBox vPane = new VBox();
					vPane.getStyleClass().add("calendar_pane");
					vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
						((ControllerOrarioS) MetaData.controller).addMateria(vPane);
					});
					osGrid.add(vPane, col, count);
				}
			}
			if(count != 0) {
				if(!giorno.get(ora).equals("") && giorno.get(ora).equals(materiaPrec)) {
					length ++;
					if(length == 1)
						startPos = count;
				}
				else {
					if(length != 1) {
						addVBoxToCell(osGrid, materiaPrec, startPos, col, length);
						if(giorno.get(ora).equals("")) {
							VBox vPane = new VBox();
							vPane.getStyleClass().add("calendar_pane");
							vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
								((ControllerOrarioS) MetaData.controller).addMateria(vPane);
							});
							osGrid.add(vPane, col, count);
						}
						else
							addVBoxToCell(osGrid, giorno.get(ora), count, col, 1);
					}
					else {
						length = 1;
						startPos = count;
						if(!giorno.get(ora).equals(""))
							addVBoxToCell(osGrid, giorno.get(ora), startPos, col, length);
						else {
							VBox vPane = new VBox();
							vPane.getStyleClass().add("calendar_pane");
							vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
								((ControllerOrarioS) MetaData.controller).addMateria(vPane);
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
	
	public Materia getMateriaByNome(String nome) {
		for(Materia m : materie) {
			if(m.getNome().equals(nome))
				return m;
		}
		return null;
	}
	
	public Materia getMateriaById(String id) {
		for(Materia m : materie) {
			if(m.getId() == Integer.parseInt(id))
				return m;
		}
		
		return null;
	}
	
	public void initComboBoxes() {
		materiaBox.getItems().add("");
		if(MetaData.os.getMateriaByPos(MetaData.sub_row, MetaData.sub_col).equals(""))
			materiaBox.getSelectionModel().selectFirst();
		for(Materia m : materie) {
			materiaBox.getItems().add(m.getNome());
			if(MetaData.os.getMateriaByPos(MetaData.sub_row, MetaData.sub_col).equals(m.getNome()))
				materiaBox.getSelectionModel().select(m.getNome());
		}
	}
	
	public void initialize() {
		materie = DataBaseHandler.getInstance().getMaterie();
		initComboBoxes();
	}
}
