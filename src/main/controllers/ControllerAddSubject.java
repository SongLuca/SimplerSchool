package main.controllers;
import java.util.ArrayList;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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
	
	private String materia;
	
	private boolean insegnaMod, materiaMod;

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
			if(!materiaBox.getValue().equals(materia))
				materiaMod = true;
		} else {
			materiaMod = true;
			MetaData.os.addMateria(ora, giorno, "");
		}
		if(insegnaMod)
			MetaData.cos.setInsegna(insegna);
		if(materiaMod || insegnaMod)
			MetaData.cos.updateOSTask("saved", true);
		
		cancel(event);
	}

	public void checkProfBoxes(JFXComboBox<String> box, Insegna copy, int idM) {
		Docente d = getDocenteByNomeCognome(box.getValue());
		if (copy == null && !box.getValue().equals("")) {
			insegna.add(new Insegna(idM, d.getIdDocente(), "insert"));
			insegnaMod = true;
		} else if (copy != null) {
			if (box.getValue().equals("")) {
				insegna.get(insegna.indexOf(copy)).setStato("delete");
				insegnaMod = true;
			} else {
				Insegna updateIn = insegna.get(insegna.indexOf(copy));
				if (updateIn.getProfId() != d.getIdDocente()) {
					updateIn.setStato("delete");
					insegna.add(new Insegna(idM, d.getIdDocente(), "insert"));
					insegnaMod = true;
				}
			}
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
		
		materia = currMateria;
		if (!currMateria.equals("")) {
			materiaBox.getSelectionModel().select(getMateriaById(currMateria).getNome());
			
		}	

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
		insegnaMod = false;
		initComboBoxes();
	}
}
