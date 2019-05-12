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
import main.application.models.Docente;
import main.application.models.Insegna;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.database.DataBaseHandler;
import main.utils.Utils;

public class ControllerAddSubject {

	@FXML
	private JFXComboBox<String> materiaBox;

	@FXML
	private JFXComboBox<String> profBox;

	@FXML
	private JFXComboBox<String> prof2Box;

	private ArrayList<Materia> materie;

	private ArrayList<Insegna> insegna;

	private ArrayList<Docente> docenti;

	private Insegna copy1, copy2;

	private boolean modificato;

	@FXML
	void cancel(MouseEvent event) {
		Stage stage = (Stage) materiaBox.getScene().getWindow();
		stage.close();
	}

	@FXML
	void save(MouseEvent event) {
		String ora = (MetaData.sub_row + 1) + "ora";
		String giorno = Utils.numToDay(MetaData.sub_col);
		if (!materiaBox.getValue().equals("")) {
			Materia m = getMateriaByNome(materiaBox.getValue());
			MetaData.os.addMateria(ora, giorno, m.getId() + "");
			checkProfBoxes(profBox, copy1, m.getId());
			checkProfBoxes(prof2Box, copy2, m.getId());
		} else {
			MetaData.os.addMateria(ora, giorno, "");
		}
		fuseSubjects(MetaData.OrarioSGrid, MetaData.sub_col);
		if (modificato)
			MetaData.cos.setInsegna(insegna);
		cancel(event);
	}

	public void checkProfBoxes(JFXComboBox<String> box, Insegna copy, int idM) {
		Docente d = getDocenteByNomeCognome(box.getValue());
		if (copy == null && !box.getValue().equals("")) {
			insegna.add(new Insegna(idM, d.getIdDocente(), "insert"));
			modificato = true;
		} else if (copy != null) {
			if (box.getValue().equals("")) {
				insegna.get(insegna.indexOf(copy)).setStato("delete");
				modificato = true;
			} else {
				Insegna updateIn = insegna.get(insegna.indexOf(copy));
				if (updateIn.getProfId() != d.getIdDocente()) {
					updateIn.setStato("delete");
					insegna.add(new Insegna(idM, d.getIdDocente(), "insert"));
					modificato = true;
				}
			}
		}

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
		if (rowSpan != 1)
			osGrid.add(pane, col, row, 1, rowSpan);
		else
			osGrid.add(pane, col, row);
	}

	public void fuseSubjects(GridPane osGrid, int col) {
		LinkedHashMap<String, String> giorno = MetaData.os.getOrarioGiorno(col);
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
						((ControllerOrarioS) MetaData.controller).addMateria(vPane);
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
								((ControllerOrarioS) MetaData.controller).addMateria(vPane);
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
		for (Materia m : materie) {
			if (m.getNome().equals(nome))
				return m;
		}
		return null;
	}

	public Materia getMateriaById(String id) {
		for (Materia m : materie) {
			if (m.getId() == Integer.parseInt(id))
				return m;
		}

		return null;
	}

	public Docente getDocenteById(int id) {
		for (Docente d : docenti) {
			if (d.getIdDocente() == id)
				return d;
		}

		return null;
	}

	public Docente getDocenteByNomeCognome(String nomeCognome) {
		for (Docente d : docenti) {
			if (d.getNomeCognome().equals(nomeCognome))
				return d;
		}
		return null;
	}

	public void initComboBoxes() {
		materiaBox.getItems().add("");
		profBox.getItems().add("");
		prof2Box.getItems().add("");
		materiaBox.getSelectionModel().selectFirst();
		profBox.getSelectionModel().selectFirst();
		prof2Box.getSelectionModel().selectFirst();
		String currMateria = MetaData.os.getMateriaByPos(MetaData.sub_row, MetaData.sub_col);
		materiaBox.setOnAction(e->{
			profBox.getSelectionModel().selectFirst();
			prof2Box.getSelectionModel().selectFirst();
			int idM = (materiaBox.getValue().equals("")) ? 0 : getMateriaByNome(materiaBox.getValue()).getId();
			if (idM > 0) {
				for (Insegna i : insegna) {
					if (i.getMateriaId() == idM) {
						if (profBox.getValue().equals(""))
							profBox.getSelectionModel().select(getDocenteById(i.getProfId()).getNomeCognome());
						else
							prof2Box.getSelectionModel().select(getDocenteById(i.getProfId()).getNomeCognome());
					}
				}
				copy1 = (profBox.getValue().equals("")) ? null
						: new Insegna(idM, getDocenteByNomeCognome(profBox.getValue()).getIdDocente(), "fresh");
				copy2 = (prof2Box.getValue().equals("")) ? null
						: new Insegna(idM, getDocenteByNomeCognome(prof2Box.getValue()).getIdDocente(), "fresh");
			}
		});
		
		int idM = (currMateria.equals("")) ? 0 : Integer.parseInt(currMateria);

		for (Materia m : materie) { // popolare il combobox di materie
			materiaBox.getItems().add(m.getNome());
		}

		for (Docente d : docenti) { // popolare il combobox di docenti
			profBox.getItems().add(d.getNome() + " " + d.getCognome());
			prof2Box.getItems().add(d.getNome() + " " + d.getCognome());
		}

		if (!currMateria.equals(""))
			materiaBox.getSelectionModel().select(getMateriaById(currMateria).getNome());

		if (idM > 0) {
			for (Insegna i : insegna) {
				if (i.getMateriaId() == idM) {
					if (profBox.getValue().equals(""))
						profBox.getSelectionModel().select(getDocenteById(i.getProfId()).getNomeCognome());
					else
						prof2Box.getSelectionModel().select(getDocenteById(i.getProfId()).getNomeCognome());
				}
			}
			copy1 = (profBox.getValue().equals("")) ? null
					: new Insegna(idM, getDocenteByNomeCognome(profBox.getValue()).getIdDocente(), "fresh");
			copy2 = (prof2Box.getValue().equals("")) ? null
					: new Insegna(idM, getDocenteByNomeCognome(prof2Box.getValue()).getIdDocente(), "fresh");
		}
	}

	public void initialize() {
		materie = DataBaseHandler.getInstance().getMaterie();
		insegna = DataBaseHandler.getInstance().getNewInsegna();
		docenti = DataBaseHandler.getInstance().getDocenti();
		modificato = false;
		initComboBoxes();
	}
}
