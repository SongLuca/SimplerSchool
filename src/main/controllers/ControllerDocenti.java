package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeIn;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.customGUI.ConfirmDialog;
import main.application.models.Config;
import main.application.models.Docente;
import main.application.models.MetaData;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.Utils;

public class ControllerDocenti {
	@FXML
	private AnchorPane subContentPane;

	@FXML
	private VBox docentiBox;

	@FXML
	private JFXSpinner loading;
	
	private ArrayList<Docente> docenti;
	
	private List<Integer> toRemove;

	private List<HBox> emptyBoxList;

	private boolean modificato;

	public void initialize() {
		modificato = false;
		toRemove = new ArrayList<Integer>();
		emptyBoxList = new ArrayList<HBox>();
		docenti = DataBaseHandler.getInstance().getDocenti();
		MetaData.cd = this;
		Console.print("Opening docente gui", "gui");
		initDocentiBox();
		AnchorPane.setBottomAnchor(subContentPane, 0.0);
		AnchorPane.setTopAnchor(subContentPane, 0.0);
		AnchorPane.setLeftAnchor(subContentPane, 0.0);
		AnchorPane.setRightAnchor(subContentPane, 0.0);
	}
	
	public void addToRemove(int idDocente) {
		this.toRemove.add(idDocente);
	}
	
	public void initDocentiBox() {
		docentiBox.getChildren().clear();
		Console.print(docenti.toString(), "");
		for(Docente d : docenti) {
			addDocente(d);
		}
	}
	
	public void addDocente(Docente d) {
		HBox box = loadDocenteBox();
		box.setId(String.valueOf(d.getIdDocente()));
		JFXTextField nome = (JFXTextField) box.lookup("#nomeField");
		JFXTextField cognome = (JFXTextField) box.lookup("#cognomeField");
		Label idLbl = (Label) box.lookup("#idDocente");
		nome.setText(d.getNome());
		cognome.setText(d.getCognome());
		idLbl.setText(String.valueOf(d.getIdDocente()));
	}
	
	public HBox loadDocenteBox() {
		try {
			URL fxmlURL = new File(Config.getString("config", "docenteBoxFXML")).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			HBox box = fxmlLoader.load();
			docentiBox.getChildren().add(box);
			new FadeIn(box).play();
			return box;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML
	void apply(MouseEvent event) {
		//HashSet<String> nomi = new HashSet<String>();
		for (Node component : docentiBox.getChildren()) {
			if (component instanceof HBox) {
				HBox docBox = (HBox) component;
				String id = ((Label) docBox.lookup("#idDocente")).getText();
				String nome = ((JFXTextField) docBox.lookup("#nomeField")).getText();
				String cognome = ((JFXTextField) docBox.lookup("#cognomeField")).getText();
			/*	if(nomi.add(nomeM) == false) {
					StackPane root = (StackPane) subContentPane.getScene().lookup("#rootStack");
					AnchorPane pane = (AnchorPane) subContentPane.getScene().lookup("#rootPane");
					Utils.popUpDialog(root, pane, "Message", "Duplicate names!");
					docenti = DataBaseHandler.getInstance().getDocenti();
					modificato = false;
				}*/
				/*else {*/
					if(id.isEmpty() && (!nome.trim().isEmpty() && !cognome.trim().isEmpty())) {  // se id e' vuoto e nome non e'vuoto -> la materia e' insert
						Docente d = new Docente(nome, cognome);
						d.setStato("insert");
						docenti.add(d);
						modificato = true;
					}
					else if(!id.isEmpty() && (nome.trim().isEmpty() && cognome.trim().isEmpty())) {  // se id non e' vuoto e il nome e' vuoto -> la materia e' delete
						int idM = Integer.parseInt(id);
						getDocenteById(idM).setStato("delete");
						modificato = true;
					}
					else if(id.isEmpty() && (nome.trim().isEmpty() || cognome.trim().isEmpty())) {
						emptyBoxList.add(docBox);
					}
					else if(!id.isEmpty()){
						int idD = Integer.parseInt(id);
						Docente d = getDocenteById(idD);
						if(!d.equals(new Docente(idD, nome, cognome))) {
							d.setNome(nome);
							d.setCognome(cognome);
							d.setStato("update");	
							modificato = true;
						}
					}
				}
		/*	}*/
		}
		if (modificato || !toRemove.isEmpty()) {
			if(!toRemove.isEmpty()) {
				for(int idD : toRemove) {
					getDocenteById(idD).setStato("delete");
				}
			}
			updateDocentiInDB();
			modificato = false;
			emptyBoxList.clear();
			toRemove.clear();
		}
		
		else {
			if(!emptyBoxList.isEmpty()) {
				docentiBox.getChildren().removeAll(emptyBoxList);
				emptyBoxList = new ArrayList<HBox>();
			}
		}
	}
	
	public Docente getDocenteById(int id) {
		for(Docente d : docenti) {
			if(d.getIdDocente() == id)
				return d;
		}
		return null;
	}
	
	@FXML
	void clear(MouseEvent event) {
		if(!docenti.isEmpty()) {
			Stage owner = (Stage)subContentPane.getScene().getWindow();
			ConfirmDialog cd = new ConfirmDialog(owner, "Are you sure to clear this?");
			if(cd.getResult()) { 
				docentiBox.getChildren().removeAll();
				for(Docente d : docenti)
					d.setStato("delete");
				updateDocentiInDB();
			}
		}
	}

	@FXML
	void newDocente(MouseEvent event) {
		HBox box = loadDocenteBox();
		Label idLbl = (Label) box.lookup("#idDocente");
		idLbl.setText("");
	}
	
	public void updateDocentiInDB() { 
		StackPane root = (StackPane) subContentPane.getScene().lookup("#rootStack");
		AnchorPane pane = (AnchorPane) subContentPane.getScene().lookup("#rootPane");
		Task<Boolean> updateDocentiTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				subContentPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().updateDocenteTable(docenti);
			}
		};

		updateDocentiTask.setOnFailed(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			Utils.errorMsg("Failed to get docenti from db");
			updateDocentiTask.getException().printStackTrace();
		});

		updateDocentiTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			if (updateDocentiTask.getValue()) {
				Utils.popUpDialog(root, pane, "Message", "Changes saved");
				MetaData.cm.updateOSPicker();
				MetaData.cm.loadNoteBoard();
				docenti = DataBaseHandler.getInstance().getDocenti();
				initDocentiBox();
			} else {
				Console.print("Error on updating docenti table", "db");
			}
		});
		new Thread(updateDocentiTask).start();
	}
}
