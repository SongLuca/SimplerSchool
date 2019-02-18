package main.controllers;

import java.util.HashMap;

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
import main.utils.SimplerSchoolUtil;

public class ControllerAddSubject {

	@FXML
	private JFXComboBox<String> materiaBox;

	@FXML
	private JFXComboBox<String> profBox;

	@FXML
	private JFXComboBox<String> prof2Box;
	
	private HashMap<Integer, Materia> materie;
	
	@FXML
	void cancel(MouseEvent event) {
		Stage stage = (Stage) materiaBox.getScene().getWindow();
		stage.close();
	}

	@FXML
	void save(MouseEvent event) {
		Materia m = getMateriaByNome(materiaBox.getValue());
		String ora = (MetaData.sub_row+1)+"ora";
		String giorno = SimplerSchoolUtil.numToDay(MetaData.sub_col);
		System.out.println("adding "+m.getNome()+" to "+ora +" at "+ giorno);
		MetaData.os.addMateria(ora,giorno, m.getNome());
		GridPane osGrid = MetaData.OrarioSGrid;
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setStyle("-fx-background-color:"+m.getColore()+";");
		Label lbl = new Label();
		lbl.setText(m.getNome());
		pane.getChildren().add(lbl);
		pane.getStyleClass().add("calendar_pane");
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			((ControllerOrarioS) MetaData.controller).addMateria(pane);
		});
		osGrid.add(pane, MetaData.sub_col, MetaData.sub_row);
		cancel(event);
	}
	
	public Materia getMateriaByNome(String nome) {
		for(int key : materie.keySet()) {
			if(materie.get(key).getNome().equals(nome))
				return materie.get(key);
		}
		return null;
	}
	
	public void initComboBoxes() {
		for(int key : materie.keySet()) {
			materiaBox.getItems().add(materie.get(key).getNome());
		}
	}
	
	public void initialize() {
		materie = DataBaseHandler.getInstance().getMaterie();
		initComboBoxes();
	}
}
