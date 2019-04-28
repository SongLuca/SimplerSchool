package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeIn;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.application.customGUI.ConfirmDialog;
import main.application.models.Config;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerMaterie {

	@FXML
	private AnchorPane subContentPane;

	@FXML
	private JFXButton addButton;

	@FXML
	private VBox materieBox;

	@FXML
	private JFXSpinner loading;

	private ArrayList<Materia> materie;

	private List<Integer> toRemove;
	
	private List<HBox> emptyBoxList;

	private boolean modificato;

	public void initialize() {
		modificato = false;
		toRemove = new ArrayList<Integer>();
		emptyBoxList = new ArrayList<HBox>();
		materie = DataBaseHandler.getInstance().getMaterie();
		MetaData.cmat = this;
		Console.print("Opening materie gui", "gui");
		initMaterieBox();
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
	}
	
	public void addToRemove(int idMateria) {
		this.toRemove.add(idMateria);
	}
	
	public void apply() {
		HashSet<String> nomi = new HashSet<String>();
		for (Node component : materieBox.getChildren()) {
			if (component instanceof HBox) {
				HBox matBox = (HBox) component;
				Label idLbl = (Label) matBox.lookup("#idMateria");
				JFXTextField nomeField = (JFXTextField) matBox.lookup("#nomeMateria");
				String id = idLbl.getText();
				String nomeM = nomeField.getText();
				if(nomi.add(nomeM) == false) {
					StackPane root = (StackPane) subContentPane.getScene().lookup("#rootStack");
					AnchorPane pane = (AnchorPane) subContentPane.getScene().lookup("#rootPane");
					Utils.popUpDialog(root, pane, "Message", "Duplicate names!");
					materie = DataBaseHandler.getInstance().getMaterie();
					modificato = false;
				}
				else {
					if(id.isEmpty() && !nomeM.trim().isEmpty()) {  // se id e' vuoto e nome non e'vuoto -> la materia e' insert
						ColorPicker colore = (ColorPicker) matBox.lookup("#coloreMateria");
						String hexColor = Utils.toRGBCode(colore.getValue());
						Materia m = new Materia();
						m.setNome(nomeM);
						m.setColore(hexColor);
						m.setStato("insert");
						// aggiungere alla lista
						materie.add(m);
						modificato = true;
					}
					else if(!id.isEmpty() && nomeM.trim().isEmpty()) {  // se id non e' vuoto e il nome e' vuoto -> la materia e' delete
						int idM = Integer.parseInt(id);
						getMaterieById(idM).setStato("delete");
						modificato = true;
					}
					else if(id.isEmpty() && nomeM.trim().isEmpty()) {
						emptyBoxList.add(matBox);
					}
					else if(!id.isEmpty()){
						int idM = Integer.parseInt(id);
						ColorPicker colore = (ColorPicker) matBox.lookup("#coloreMateria");
						String hexColor = Utils.toRGBCode(colore.getValue());
						Materia m = getMaterieById(idM);
						if(!m.getColore().equals(hexColor) || !m.getNome().equals(nomeM)) {
							m.setNome(nomeM);
							m.setColore(hexColor);
							m.setStato("update");	
						}
						modificato = true;
					}
				}
			}
		}
		
		if (modificato || !toRemove.isEmpty()) {
			if(!toRemove.isEmpty()) {
				for(int idM : toRemove) {
					getMaterieById(idM).setStato("delete");
				}
			}
			updateMaterieInDB();
			modificato = false;
			emptyBoxList.clear();
			toRemove.clear();
		}
		
		else {
			if(!emptyBoxList.isEmpty()) {
				materieBox.getChildren().removeAll(emptyBoxList);
				emptyBoxList = new ArrayList<HBox>();
			}
		}
	}
	
	public Materia getMaterieById(int id) {
		for(Materia m : materie)
			if(m.getId() == id)
				return m;
		return null;
	}
	
	
	public void exit() {
		WindowStyle.close((Stage) materieBox.getScene().getWindow());
	}
	
	@FXML
	public void clear() {
		if(!materie.isEmpty()) {
			Stage owner = (Stage)subContentPane.getScene().getWindow();
			ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure to clear this?");
			if(cd.getResult()) { 
				materieBox.getChildren().removeAll();
				for(Materia m : materie)
					m.setStato("delete");
				updateMaterieInDB();
			}
		}
	}
	
	@FXML
	public void newMateria() { // aggiunge un nuovo materiabox con nome = "" e colore = ffffff
		HBox box = loadMateriaBox();
		Label idLbl = (Label) box.lookup("#idMateria");
		//materie.put(id, new Materia(id));
		idLbl.setText("");
	}

	public void addMateria(Materia m) { // aggiunge i materiabox a seconda di materia dato passato in parametro
		HBox box = loadMateriaBox();
		box.setId(box.getId() + m.getId());
		JFXTextField nome = (JFXTextField) box.lookup("#nomeMateria");
		ColorPicker colore = (ColorPicker) box.lookup("#coloreMateria");
		Label idLbl = (Label) box.lookup("#idMateria");
		nome.setText(m.getNome());
		colore.setValue(Color.valueOf(m.getColore()));
		idLbl.setText("" + m.getId());
	}


	public void updateMaterieInDB() { // un attivita in background per aggiornare tutte le materie nel db
		StackPane root = (StackPane) subContentPane.getScene().lookup("#rootStack");
		AnchorPane pane = (AnchorPane) subContentPane.getScene().lookup("#rootPane");
		Task<Boolean> updateMaterieTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				return DataBaseHandler.getInstance().updateMateriaTable(materie);
			}
		};

		updateMaterieTask.setOnFailed(event -> {
			loading.setVisible(false);
			Utils.errorMsg("Failed to get Materie from db");
			updateMaterieTask.getException().printStackTrace();
		});

		updateMaterieTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			if (updateMaterieTask.getValue()) {
				Utils.popUpDialog(root, pane, "Message", "Changes saved");
				MetaData.cm.updateOSPicker();
				materie = DataBaseHandler.getInstance().getMaterie();
				initMaterieBox();
			} else {
				Console.print("Error on updating materie table", "db");
			}
		});
		new Thread(updateMaterieTask).start();
	}

	public HBox loadMateriaBox() { // legge il materiabox dal file fxml
		try {
			URL fxmlURL = new File(Config.getString("config", "materiaBoxFXML")).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			HBox box = fxmlLoader.load();
			materieBox.getChildren().add(box);
			new FadeIn(box).play();
			return box;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void initMaterieBox() { // aggiunge i materiabox a seconda della collezione delle materie
		materieBox.getChildren().clear();
		for (Materia m : materie) {
			addMateria(m);
		}
	}
	
}
